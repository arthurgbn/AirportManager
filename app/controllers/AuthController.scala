package controllers

import forms.LoginForm

import javax.inject._
import play.api.mvc._
import services.UserService


@Singleton
class AuthController @Inject()(cc: ControllerComponents, userService: UserService) extends AbstractController(cc) {



  def showLoginForm = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login())
  }

  def login = Action { implicit request: Request[AnyContent] =>
    LoginForm.form.bindFromRequest.fold(
      errors => BadRequest(views.html.login()),
      userData => {
        val username = userData.username
        val password = userData.password
        userService.authenticate(username, password) match {
          case Some(user) =>
            val result = Redirect(routes.HomeController.index).withSession("userId" -> user.id.toString, "username" -> user.username)
            result
          case None =>
            Redirect(routes.AuthController.showLoginForm).flashing("error" -> "Invalid username or password")
        }
      }
    )
  }


  def logout = Action {
    Redirect(routes.AuthController.showLoginForm).withNewSession.flashing("success" -> "You have been logged out")
  }
}
