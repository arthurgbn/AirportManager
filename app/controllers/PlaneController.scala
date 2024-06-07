package controllers

import play.api.mvc._
import services.PlaneService

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class PlaneController @Inject()(val controllerComponents: ControllerComponents, planeService: PlaneService) extends BaseController {

  def delete(id: Long): Action[AnyContent] = Action {
    val success = planeService.deletePlane(id)
    if (success) {
      Redirect(routes.HomeController.index)
    } else {
      Redirect(routes.HomeController.index).flashing("error" -> "Cannot delete plane because it is used in a flight.")
    }
  }


  def create: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    implicit val ec: ExecutionContext = ExecutionContext.global

    val name = request.body.asFormUrlEncoded.get("model").head
    val capacity = request.body.asFormUrlEncoded.get("capacity").head.toInt

    planeService.addPlane(name, capacity).map { _ =>
      Redirect(routes.HomeController.index)

    }
  }
}
