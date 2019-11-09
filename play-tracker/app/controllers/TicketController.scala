package controllers

import java.text.SimpleDateFormat
import java.util.Date

import javax.inject._
import play.api.Logger
import play.api.i18n.Lang
import play.api.libs.json._
import play.api.libs.ws._
import play.api.mvc._
import play.filters.csrf._
import play.filters.headers.SecurityHeadersFilter

import scala.concurrent.{ExecutionContext, Future}


case class TicketData(ticketName: String, ticketDetails: String)

case class TicketList(ticketId: Int, ticketUserName: String, ticketName: String, ticketCreate: Date, ticketDetails: String)


/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class TicketController @Inject()(cc: ControllerComponents, callws: CallWebservice,
                                 implicit val addToken: CSRFAddToken,
                                 implicit val checkToken: CSRFCheck,
                                 implicit val ec: ExecutionContext) extends AbstractController(cc) with Common {


  val logger: Logger = Logger(this.getClass())


  def isLoggedIn(implicit request: Request[AnyContent]): Boolean = {
    request.session.get("token").map { token =>
      true
    }.getOrElse {
      false
    }
  }

  def ticketdata() = Action.async {
    implicit request =>
      implicit val messages = messagesApi.preferred(Seq(Lang.defaultLang))
      callws.getService(
        service = "TICKET-CONTROL-SERVICE", url = "/alltickets",
        onError = { e => Ok(views.html.index(s"Error dude ${e}", ticketForm, loginForm)) },
        onSuccess = { resp =>

          logger.info(s"all ticket data")
          createTicketHtml(request, resp)
        }
      )
  }

  def myticketdata() = Action.async {
    implicit request =>
      if (isLoggedIn) {

        val url = s"/mytickets?userId=${request.session.get("userId").get}"
        implicit val messages = messagesApi.preferred(Seq(Lang.defaultLang))
        callws.getService(
          service = "TICKET-CONTROL-SERVICE", url = url,
          onError = { e => Ok(views.html.index(s"Error dude ${e}", ticketForm, loginForm)) },
          onSuccess = { resp =>

            logger.info(s"myticket ${url} data")
            createTicketHtml(request, resp)
          }
        )
      } else {
        logger.info("Not logged on")
        val ticketData = List()
        Future {
          implicit val messages = messagesApi.preferred(Seq(Lang.defaultLang))
          BadRequest(views.html.displaytickets(ticketData))
        }
      }
  }


  private def createTicketHtml(implicit request: Request[AnyContent], resp: WSResponse) = {
    implicit val messages = messagesApi.preferred(Seq(Lang.defaultLang))
    import play.api.libs.functional.syntax._

    def toTickerList(tickerId: Int, ticketUserName: String, ticketName: String, ticketCreate: String, ticketDetails: String): TicketList = {
      val sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
      val toDate = sdf.parse(ticketCreate)
      new TicketList(tickerId, ticketUserName, ticketName, toDate, ticketDetails)
    }

    implicit val ticketReader: Reads[TicketList] = (
      (JsPath \ "ticketId").read[Int] and
        (JsPath \ "ticketUserName").read[String] and
        (JsPath \ "ticketName").read[String] and
        (JsPath \ "ticketCreated").readWithDefault[String]("1971-01-01T01:01:01.000") and
        (JsPath \ "ticketDetails").read[String]
      ) (toTickerList _)

    val ticketData = resp.json.as[List[TicketList]]
    logger.info(s"ticket data ${ticketData.length} done")
    Ok(views.html.displaytickets(ticketData)).withHeaders(SecurityHeadersFilter.X_FRAME_OPTIONS_HEADER -> "sameorigin")
  }


  def newticketpage() = Action {
    implicit request =>
      implicit val messages = messagesApi.preferred(Seq(Lang.defaultLang))
      Ok(views.html.newticket(ticketForm))
  }


  def ticketPost = Action.async {
    implicit request =>
      ticketForm.bindFromRequest.fold(
        formWithErrors => {
          logger.debug(s"${formWithErrors}")
          implicit val messages = messagesApi.preferred(Seq(Lang.defaultLang))
          Future {
            BadRequest(views.html.newticket(formWithErrors))
          }
        },
        ticketData => {
          logger.info(s"ticket data ${ticketData}")

          callws.postService(
            service = "TICKET-CONTROL-SERVICE", url = "/newticket",
            data = Json.obj("ticketName" -> ticketData.ticketName,
              "ticketDetails" -> ticketData.ticketDetails),
            onError = { e =>
              BadRequest("").flashing("error" -> s"new ticket failed")
            },
            onSuccess = {
              resp =>
                logger.info(s"new ticket ${resp.json}")
                Redirect(routes.HomeController.index())
                if ( resp.status == 200 ) {
                  logger.info(s"updated ticket ${resp}")
                  Ok("")
                } else {
                  BadRequest("").flashing("error" -> s"new ticket failed")
                }
            }
          )
        }
      )

  }

  def ticketPut = Action.async {
    implicit request =>
      val json = request.body.asJson.get

      val ticketId = (json \ "ticketId").get
      val ticketDetails = (json \ "ticketDetails").get
      val ticketName = (json \ "ticketName").get
      val ticketCreateString = (json \ "ticketCreate").get

      val sdf = new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")
      val sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
      val ticketCreated = sdf2.format(sdf.parse(ticketCreateString.as[String]))

      val userId = request.session.get("userId").get
      val data = Json.obj(
        "ticketId" -> ticketId,
        "ticketName" -> ticketName,
        "ticketUserId" -> userId,
        "ticketCreated" -> ticketCreated,
        "ticketDetails" -> ticketDetails)

      logger.info(s"PUT input ${json} will send data ${data}")
      callws.putService(
        service = "TICKET-CONTROL-SERVICE", url = "/ticket",
        data = data,
        onError = { e =>
          BadRequest("").flashing("error" -> s"failed update")
        },
        onSuccess = {
          resp =>
            logger.info(s"updated ticket ${resp.status}")
            if ( resp.status == 200 ) {
              logger.info(s"updated ticket ${resp}")
              Ok("")
            } else {
              BadRequest("").flashing("error" -> s"failed update")
            }
        }
      )
  }

  def ticketDone(ticketId: Long) = Action.async {
    implicit request =>

      request.session.get("token").map { token =>

        callws.deleteService(
          service = "TICKET-CONTROL-SERVICE", url = s"/ticket/${ticketId}?token=${token}",
          onError = { e =>
            logger.info(s"deleted failed ${e}")
            Redirect(routes.HomeController.login()).withNewSession.flashing("error" -> s"failed delete")
          },
          onSuccess = {
            resp =>
              if ( resp.status == 200 ) {
                logger.info(s"deleted ticket ${resp}")
                Ok("")
              } else {
                BadRequest("").flashing("error" -> s"failed delete")
              }
          }
        )
      }.getOrElse {
        Future( BadRequest("").flashing("error" -> s"no session") )
      }


  }

}

