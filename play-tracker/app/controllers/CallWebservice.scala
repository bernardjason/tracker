package controllers

import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.json.JsObject
import play.api.libs.ws.{WSClient, WSRequest}
import play.api.mvc.{ControllerComponents, Result}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CallWebservice@Inject()(cc: ControllerComponents, eureka: Eureka, ws: WSClient,implicit val ec: ExecutionContext) {

  val logger: Logger = Logger(this.getClass())

  def putService(service: String, url: String,
                         data: JsObject,
                         onError: Exception => Result,
                         onSuccess: => (WSRequest#Response => Result)
                        ): Future[Result] = {
    eureka.ifServiceAvailable(service, url).map { eurekaInstance =>
      logger.info(s"PUT Call ${eurekaInstance.url}")
      ws.url(eurekaInstance.url).put(data).map(onSuccess)
        .recover { case e: Exception =>
          logger.error("Error ", e)
          eurekaInstance.markDown()
          onError(e)
        }
    }.getOrElse(Future {
      onError(null)
    })
  }

  def deleteService(service: String, url: String,
                            onError: Exception => Result,
                            onSuccess: => (WSRequest#Response => Result)
                           ): Future[Result] = {
    eureka.ifServiceAvailable(service, url).map { eurekaInstance =>
      logger.info(s"DELETE Call ${eurekaInstance.url}")
      ws.url(eurekaInstance.url).delete().map(onSuccess)
        .recover { case e: Exception =>
          logger.error("Error ", e)
          eurekaInstance.markDown()
          onError(e)
        }
    }.getOrElse(Future {
      onError(null)
    })
  }

  def getService(service: String, url: String,
                         onError: Exception => Result,
                         onSuccess: => (WSRequest#Response => Result)
                        ): Future[Result] = {
    eureka.ifServiceAvailable(service, url).map { eurekaInstance =>
      logger.info(s"GET Call ${eurekaInstance.url}")
      ws.url(eurekaInstance.url).get().map(onSuccess)
        .recover { case e: Exception =>
          logger.error("Error ", e)
          eurekaInstance.markDown()
          onError(e)
        }
    }.getOrElse(Future {
      onError(null)
    })
  }

   def postService(service: String, url: String,
                          data: JsObject,
                          onError: Exception => Result,
                          onSuccess: => (WSRequest#Response => Result)
                         ): Future[Result] = {
    eureka.ifServiceAvailable(service, url).map { eurekaInstance =>
      logger.info(s"POST Call ${eurekaInstance.url}")
      ws.url(eurekaInstance.url).post(data).map(onSuccess)
        .recover { case e: Exception =>
          logger.error("Error ", e)
          eurekaInstance.markDown()
          onError(e)
        }
    }.getOrElse(Future {
      onError(null)
    })
  }
}
