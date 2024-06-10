package modules

import com.google.inject.{AbstractModule, Provides}
import net.codingwell.scalaguice.ScalaModule
import play.api.Configuration
import play.api.mvc.{CookieHeaderEncoding, SessionCookieBaker}
import play.silhouette.api.repositories.AuthInfoRepository
import play.silhouette.api.services.IdentityService
import play.silhouette.api.util._
import play.silhouette.api.{EventBus, Silhouette, SilhouetteProvider, Environment => SilhouetteEnvironment}
import models.User
import play.api.cache.AsyncCacheApi
import play.silhouette.api.crypto.{AuthenticatorEncoder, Base64AuthenticatorEncoder}
import play.silhouette.impl.authenticators.{SessionAuthenticatorService, SessionAuthenticatorSettings}
import play.silhouette.impl.providers._
import play.silhouette.impl.util._
import play.silhouette.password.BCryptPasswordHasher
import play.silhouette.persistence.daos.DelegableAuthInfoDAO
import play.silhouette.persistence.repositories.DelegableAuthInfoRepository
import services.{PasswordService, UserService}
import utils.{DefaultEnv, CacheImplementation}

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.{Configuration, Environment}

class SilhouetteModule(environment: Environment, configuration: Configuration) extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[Silhouette[DefaultEnv]].to[SilhouetteProvider[DefaultEnv]]
    bind[DelegableAuthInfoDAO[PasswordInfo]].to[PasswordService]
    bind[IdentityService[User]].to[UserService]
    bind[CacheLayer].to[PlayCacheLayer]
    bind[IDGenerator].toInstance(new SecureRandomIDGenerator())
    bind[PasswordHasher].toInstance(new BCryptPasswordHasher)
    bind[FingerprintGenerator].toInstance(new DefaultFingerprintGenerator(includeRemoteAddress = false))
    bind[EventBus].toInstance(EventBus())
    bind[Clock].toInstance(Clock())
    bind[AuthenticatorEncoder].toInstance(new Base64AuthenticatorEncoder)
    bind[AsyncCacheApi].to[CacheImplementation]

  }

  @Provides
  def provideEnvironment(
                          userService: UserService,
                          authenticatorService: SessionAuthenticatorService,
                          eventBus: EventBus
                        ): SilhouetteEnvironment[DefaultEnv] = {
    SilhouetteEnvironment[DefaultEnv](userService, authenticatorService, Seq(), eventBus)
  }

  @Provides
  def provideAuthenticatorService(
                                   fingerprintGenerator: FingerprintGenerator,
                                   authenticatorEncoder: AuthenticatorEncoder,
                                   sessionCookieBaker: SessionCookieBaker,
                                   clock: Clock
                                 ): SessionAuthenticatorService = {
    new SessionAuthenticatorService(SessionAuthenticatorSettings(), fingerprintGenerator, authenticatorEncoder, sessionCookieBaker, clock)
  }

  @Provides
  def provideAuthInfoRepository(passwordInfoDAO: DelegableAuthInfoDAO[PasswordInfo]): AuthInfoRepository = {
    new DelegableAuthInfoRepository(passwordInfoDAO)
  }

  @Provides
  def provideCredentialsProvider(authInfoRepository: AuthInfoRepository, passwordHasherRegistry: PasswordHasherRegistry): CredentialsProvider = {
    new CredentialsProvider(authInfoRepository, passwordHasherRegistry)
  }

  @Provides
  def providePasswordHasherRegistry(passwordHasher: PasswordHasher): PasswordHasherRegistry = {
    new PasswordHasherRegistry(passwordHasher)
  }
}
