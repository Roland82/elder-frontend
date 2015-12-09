package controllers

import com.google.inject.Inject
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{Action, Controller}
import support.api.AuthApiClient

import scala.concurrent.Future

class Registration @Inject() (authClient: AuthApiClient) extends Controller {
  def registrationIndex() = Action {
    Ok(views.html.registration.login())
  }

  def registerFacebook() = Action.async {
    val redirectUrl = "https://www.facebook.com/dialog/oauth?client_id=1689446957937836&redirect_uri=http://eldr.dev:9000/socialauth/receive"
    Future { Redirect(redirectUrl, 302) }
  }

  def registerFacebookReceive(code: String) = Action.async {
    val accessTokenValue = authClient.getFacebookAccessToken(code)

    accessTokenValue.flatMap { r => r match {
        case Some(v) => authClient.getProfileDetails(v.accessToken) map { s => Ok(views.html.registration.login_success(s.get.name)) }
        case None => Future {Ok(views.html.error())}
      }
    }
  }
}
