package support.config

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import play.api.{ Configuration, Environment }
import support.api.{AuthApiWSClient, AuthApiClient, ApiWSClient, ApiClient}

class AppModule(environment: Environment, configuration: Configuration) extends AbstractModule {
  def configure() = {
    val conf = configuration.getConfig("elder").getOrElse(Configuration.empty)
    val apiRoot = conf.getString("api.root", None)
    val facebookConfig = conf.getConfig("facebook").getOrElse(Configuration.empty)
    val facebookClientId = facebookConfig.getString("client.id", None)
    val facebookClientSecret= facebookConfig.getString("client.secret", None)

    bindConstant.annotatedWith(Names.named("apiRoot")).to(apiRoot.getOrElse(""))
    bindConstant.annotatedWith(Names.named("facebookClientId")).to(facebookClientId.getOrElse(""))
    bindConstant.annotatedWith(Names.named("facebookSecret")).to(facebookClientSecret.getOrElse(""))

    bind(classOf[ApiClient]).to(classOf[ApiWSClient])
    bind(classOf[AuthApiClient]).to(classOf[AuthApiWSClient])
  }
}
