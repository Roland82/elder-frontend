package support.api

import javax.inject.{Inject, Named}

import play.api.libs.json.{JsPath, Reads}
import play.api.libs.functional.syntax._
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class FacebookAccessTokenResponse(accessToken: String, tokenType: String, expiresIn: Int)
case class FacebookProfileResponse(name: String, id: String)

class AuthApiWSClient @Inject() (ws: WSClient, @Named( "facebookClientId" ) facebookClientId: String, @Named( "facebookSecret" )facebookSecret: String) extends AuthApiClient {

  implicit val facebookProfileReadJson: Reads[FacebookProfileResponse] = (
      (JsPath \ "name").read[String] and
      (JsPath \ "id").read[String]
    )(FacebookProfileResponse.apply _)

  implicit val accessTokenReadJson: Reads[FacebookAccessTokenResponse] = (
    (JsPath \ "access_token").read[String] and
      (JsPath \ "token_type").read[String] and
      (JsPath \ "expires_in").read[Int])(FacebookAccessTokenResponse.apply _)

  def getFacebookAccessToken(code: String): Future[Option[FacebookAccessTokenResponse]] = {
    val redirectUrl = "http://eldr.dev:9000/socialauth/receive"
    val url = s"https://graph.facebook.com/v2.3/oauth/access_token?client_id=$facebookClientId&redirect_uri=$redirectUrl&client_secret=$facebookSecret&code=$code&scope=email,public_profile"
    ws.url(url).get().map { _.json.asOpt[FacebookAccessTokenResponse] }
  }

  def getProfileDetails[S](accessToken: String): Future[Option[FacebookProfileResponse]] = {
    ws.url(s"https://graph.facebook.com/me?access_token=$accessToken").get().map { _.json.asOpt[FacebookProfileResponse] }
  }
}

trait AuthApiClient {
  def getFacebookAccessToken(code: String): Future[Option[FacebookAccessTokenResponse]]
  def getProfileDetails[S](accessToken: String): Future[Option[FacebookProfileResponse]]
}