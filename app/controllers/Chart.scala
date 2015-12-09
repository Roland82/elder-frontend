package controllers

import javax.inject.Inject

import net.liftweb.json.Serialization.read
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws.WSResponse
import play.api.mvc.{Action, Call, Controller}
import support.api.ApiClient

import scala.concurrent.Future

class Chart @Inject() (apiClient: ApiClient) extends Controller {
  implicit val formats = net.liftweb.json.DefaultFormats

  def get(symbol: String) = Action.async { request =>

    val companyResponse = apiClient.getCompany(symbol)

    companyResponse.map { r =>
      r.status match {
        case 200 => {
          val c = read[CompanyApiResponse](r.body)
          Ok(views.html.charting.chart_display(Some(ChartViewModel(c.companyName, c.symbol))))
        }
        case 404 => Ok(views.html.charting.chart_display(None))
        case _ => Ok(views.html.error())
      }
    }
  }

  def updateTechnicalsFromChart(symbol: String) = updateAndRedirect(symbol, apiClient.updateDatapoints)

  def updateFundamentalsFromChart(symbol: String) = updateAndRedirect(symbol, apiClient.updateFundamentals)

  private def updateAndRedirect(symbol: String,f: String => Future[WSResponse]) = Action.async {
    f(symbol) map { r =>
      r.status match {
        case 201 => Redirect(Call("GET", s"/charting/${symbol}"), 302)
        case _ => Redirect(Call("GET", s"/charting/${symbol}"), 302)
      }
    }
  }
}


case class ChartViewModel(companyName: String, symbol: String)
case class CompanyApiResponse(companyName: String, symbol: String)