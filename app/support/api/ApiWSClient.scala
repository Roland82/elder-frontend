package support.api

import javax.inject.{Inject, Named}

import play.api.libs.ws.{WSResponse, WSClient}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import play.api.http.Writeable.wString


class ApiWSClient @Inject() (ws: WSClient, @Named( "apiRoot" ) baseUrl: String) extends ApiClient {
  def updateDatapoints[S](symbol: String): Future[WSResponse] = {
    ws.url(s"$baseUrl/datapoints/$symbol").post("")
  }

  def updateFundamentals(symbol: String): Future[WSResponse] = {
    ws.url(s"$baseUrl/company/fundamentals/$symbol").post("")
  }

  def getDatapoints(symbol: String): Future[WSResponse] = {
    ws.url(s"$baseUrl/datapoints/$symbol").get
  }

  def getCompany(symbol: String): Future[WSResponse] = {
    ws.url(s"$baseUrl/companies/$symbol").get
  }
}


trait ApiClient {
  def updateDatapoints[S](symbol: String): Future[WSResponse]

  def updateFundamentals(symbol: String): Future[WSResponse]

  def getDatapoints(symbol: String): Future[WSResponse]

  def getCompany(symbol: String): Future[WSResponse]
}