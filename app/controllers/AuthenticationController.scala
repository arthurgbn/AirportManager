package controllers

import models.User
import play.api.mvc._
import services.AuthentificationService
import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton class AuthenticationController  @Inject()(val controllerComponents: ControllerComponents, val authentificationService: AuthentificationService) extends BaseController {

  def authentication: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val username = request.body.asFormUrlEncoded.get("username").head
    val password = request.body.asFormUrlEncoded.get("password").head
    if (authentificationService.authentification(username, password)) {
      Redirect(routes.HomeController.index)
    } else {
      Redirect(routes.HomeController.logout).flashing("error" -> "Invalid username or password")
    }
  }

}