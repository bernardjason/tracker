package controllers

import javax.inject._
import play.api.Logger
import play.api.i18n.Lang
import play.api.libs.json._
import play.api.mvc._
import play.filters.csrf._

import scala.concurrent.{ExecutionContext, Future}

case class LoginData(username: String, password: String)


/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, callws:CallWebservice,
                               implicit val addToken: CSRFAddToken,
                               implicit val checkToken: CSRFCheck,
                               implicit val ec: ExecutionContext) extends AbstractController(cc) with Common {


  val logger: Logger = Logger(this.getClass())

  def index() = Action.async { implicit request: Request[AnyContent] =>

    request.session.get("token").map { token =>
      implicit val messages = messagesApi.preferred(Seq(Lang.defaultLang))
      val data = Json.obj("token" -> token)
      callws.postService(
        service = "USER-CONTROL-SERVICE", url = "/valid",
        data = data,
        onError = { e => Ok(views.html.index(s"Error dude ${e}", ticketForm, loginForm)).withNewSession },
        onSuccess = { resp => Ok(views.html.index(s"logged in token = ${token} ${resp.body}", ticketForm, loginForm)) }
      )
    }.getOrElse {
      Future {
        implicit val messages = messagesApi.preferred(Seq(Lang.defaultLang))
        Ok(views.html.index(s"Not logged in yet", ticketForm, loginForm))
      }
    }
  }

  def logout(): Action[AnyContent] = Action.async { implicit request =>
    Future {
      implicit val messages = messagesApi.preferred(Seq(Lang.defaultLang))
      Redirect(routes.HomeController.index()).withNewSession
    }
  }

  def login() = Action {
    implicit request =>
      implicit val messages = messagesApi.preferred(Seq(Lang.defaultLang))
      Ok(views.html.login(loginForm))
  }


  def loginPost = Action.async {
    implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors => {
          logger.info(s"bad login ${formWithErrors}")
          implicit val messages = messagesApi.preferred(Seq(Lang.defaultLang))
          Future {
            BadRequest(views.html.login(formWithErrors))
          }
        },
        loginData => {

          callws.postService(
            service = "USER-CONTROL-SERVICE", url = "/login",
            data = Json.obj("userName" -> loginData.username,
              "userPassword" -> loginData.password),
            onError = { e =>
              logger.info(s"bad login ${e}")
              //Redirect(routes.HomeController.login()).withNewSession.flashing("error" -> s"bad login")
              Redirect(routes.HomeController.index()).withNewSession.flashing("error" -> s"bad login")
            },
            onSuccess = {
              resp =>
                logger.info(s"Login worked ${resp.json}")
                val token = (resp.json \ "token").as[String]
                val userId = (resp.json \ "userId").as[Int]
                Redirect(routes.HomeController.index()).withNewSession.withSession(
                  "token" -> token,
                  "userId" -> s"${userId}"
                )
            }
          )
        }
      )

  }

}

