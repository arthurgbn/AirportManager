package controllers

import forms.LoginForm

import javax.inject._
import play.api.mvc._
import play.api.i18n.I18nSupport
import play.silhouette.api.actions.SecuredRequest
import play.silhouette.api.exceptions.ProviderException
import play.silhouette.api.{LoginEvent, Silhouette}
import play.silhouette.api.repositories.AuthInfoRepository
import play.silhouette.impl.providers._
import play.silhouette.api.util._
import services.UserService
import utils.DefaultEnv
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import play.api.Logging

@Singleton
class AuthController @Inject()(
                                silhouette: Silhouette[DefaultEnv],
                                credentialsProvider: CredentialsProvider,
                                userService: UserService,
                                authInfoRepository: AuthInfoRepository,
                                passwordHasherRegistry: PasswordHasherRegistry,
                                components: ControllerComponents
                              )(implicit ec: ExecutionContext) extends AbstractController(components) with I18nSupport with Logging {

  def showLoginForm = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login())
  }

  def login = Action.async { implicit request: Request[AnyContent] =>
    LoginForm.form.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.login())),
      data => {
        val credentials = Credentials(data.email, data.password)
        credentialsProvider.authenticate(credentials).flatMap { loginInfo =>
          userService.retrieve(loginInfo).flatMap {
            case Some(user) =>
              for {
                authenticator <- silhouette.env.authenticatorService.create(loginInfo)
                value <- silhouette.env.authenticatorService.init(authenticator)
                result <- silhouette.env.authenticatorService.embed(value, Redirect(routes.HomeController.index))
              } yield {
                silhouette.env.eventBus.publish(LoginEvent(user, request))
                result
              }
            case None =>
              Future.successful(Redirect(routes.AuthController.showLoginForm).flashing("error" -> "Email not found"))
          }
        }.recover {

          case e: ProviderException =>
            logger.error("Invalid credentials", e)
            Redirect(routes.AuthController.showLoginForm).flashing("error" -> "Invalid credentials")
        }
      }
    )
  }

  def logout = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    silhouette.env.authenticatorService.discard(request.authenticator, Redirect(routes.AuthController.showLoginForm))
  }
}

case class LoginData(email: String, password: String)
