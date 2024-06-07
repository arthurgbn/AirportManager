package modules


import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import play.silhouette.api.util.{CacheLayer, Clock, FingerprintGenerator, IDGenerator, PasswordHasher, PasswordInfo}
import play.silhouette.api.{EventBus, Silhouette, SilhouetteProvider}
import play.silhouette.impl.util.{DefaultFingerprintGenerator, PlayCacheLayer, SecureRandomIDGenerator}
import play.silhouette.password.BCryptPasswordHasher
import play.silhouette.persistence.daos.DelegableAuthInfoDAO
import services.{PasswordService, UserService}
import utils.DefaultEnv
import scala.concurrent.ExecutionContext.Implicits.global


class SilhouetteModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[Silhouette[DefaultEnv]].to[SilhouetteProvider[DefaultEnv]]
    bind[DelegableAuthInfoDAO[PasswordInfo]].to[PasswordService]
    bind[CacheLayer].to[PlayCacheLayer]
    bind[PasswordHasher].toInstance(new BCryptPasswordHasher)
    bind[FingerprintGenerator].toInstance(new DefaultFingerprintGenerator(includeRemoteAddress = false))
    bind[EventBus].toInstance(EventBus())
    bind[Clock].toInstance(Clock())
    bind[IDGenerator].toInstance(new SecureRandomIDGenerator())
  }



}


