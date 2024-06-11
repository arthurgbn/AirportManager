package controllers

import play.api.mvc._
import services.PlaneService

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import forms.NewPlaneForm
import play.silhouette.api.Silhouette
import utils.DefaultEnv

class PlaneController @Inject()(val controllerComponents: ControllerComponents,  silhouette: Silhouette[DefaultEnv],planeService: PlaneService) extends BaseController {


  def delete(id: Long): Action[AnyContent] = silhouette.SecuredAction { implicit request =>
    val success = planeService.deletePlane(id)
    if (success) {
      Redirect(routes.HomeController.index)
    } else {
      Redirect(routes.HomeController.index).flashing("error" -> "Cannot delete plane because it is used in a flight.")
    }
  }

  def create: Action[AnyContent] = silhouette.SecuredAction.async { implicit request: Request[AnyContent] =>
    implicit val ec: ExecutionContext = ExecutionContext.global

    NewPlaneForm.form.bindFromRequest.fold(
      error => Future.successful(BadRequest(views.html.addPlane(planeService.getPlanes))),
      planeData => {
        val model = planeData.model
        val capacity = planeData.capacity

        planeService.addPlane(model, capacity).map { _ =>
          Redirect(routes.HomeController.index)
        }
      }
    )
  }
}
