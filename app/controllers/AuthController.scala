package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.User
import services.UserService

@Singleton
class AuthController @Inject()(cc: ControllerComponents, userService: UserService) extends AbstractController(cc) {

  val loginForm: Form[(String, String)] = Form(
    tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )
  )

  def showLoginForm = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login())
  }

  def login = Action { implicit request: Request[AnyContent] =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.login())
      },
      userData => {
        val (username, password) = userData
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
