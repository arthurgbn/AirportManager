package controllers

import forms.LoginForm
import play.api.i18n.I18nSupport

import javax.inject._
import play.api.mvc._
import play.silhouette.api.Silhouette
import play.silhouette.impl.providers.CredentialsProvider
import services.UserService
import utils.DefaultEnv

import scala.concurrent.ExecutionContext


@Singleton
class AuthController @Inject()(credentialsProvider: CredentialsProvider,
                               userService: UserService,
                               components: ControllerComponents,
                               silhouette: Silhouette[DefaultEnv],
                               loginTemplate: views.html.login)
                              (implicit ec: ExecutionContext)
  extends AbstractController(components) with I18nSupport {



  def showLoginForm = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login())
  }

  def login = Action { implicit request: Request[AnyContent] =>

  }


  def logout = Action {
    Redirect(routes.AuthController.showLoginForm).withNewSession.flashing("success" -> "You have been logged out")
  }
}
