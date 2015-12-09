package controllers

import javax.inject.Inject

import play.api.data._
import play.api.data.Forms._
import play.api.libs.ws.{WS}
import play.api.mvc.{Call, Action, Controller}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import support.api.{ApiClient}

import scala.concurrent.Future

class DataUpdate @Inject() (apiClient: ApiClient) extends Controller {


  def technicalsIndex = Action {
    Ok(views.html.data_update.technicals(UserMessageViewModel(None, None)))
  }

  def updateTechnicals = Action.async { implicit request =>
    symbolForm.bindFromRequest.fold(
      formWithErrors => Future { Ok(views.html.data_update.technicals(UserMessageViewModel(None, Some("Bad Form Submission")))) },
      userData => {
        apiClient.updateDatapoints(userData.symbol).map { r =>
          r.status match {
            case 201 => Ok(views.html.data_update.technicals(UserMessageViewModel(Some(s"Successfully got technicals for ${userData.symbol}"), None)))
            case _ => Ok(views.html.data_update.technicals(UserMessageViewModel(None, Some("Something went wrong API response code: " + r.status))))
          }
        }
      }
    )
  }

  def fundamentalsIndex = Action {
    Ok(views.html.data_update.fundamentals())
  }

  case class SymbolForm(symbol: String)
    val symbolForm = Form(mapping("symbol" -> text)(SymbolForm.apply)(SymbolForm.unapply)
  )
}

case class UserMessageViewModel(successMessage: Option[String], errorMessage: Option[String])