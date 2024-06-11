package controllers

import play.api.mvc._
import services.AirportService

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import forms.NewAirportForm
import play.silhouette.api.Silhouette
import utils.DefaultEnv

class AirportController @Inject()(val controllerComponents: ControllerComponents,  silhouette: Silhouette[DefaultEnv], airportService: AirportService) extends BaseController {

  def delete(id: Long): Action[AnyContent] = silhouette.SecuredAction { implicit request =>
    val success = airportService.deleteAirport(id)
    if (success) {
      Redirect(routes.HomeController.index)
    } else {
      Redirect(routes.HomeController.index).flashing("error" -> "Cannot delete airport because it is used in a flight.")
    }
  }

  def create: Action[AnyContent] = silhouette.SecuredAction.async { implicit request: Request[AnyContent] =>
    implicit val ec: ExecutionContext = ExecutionContext.global

    NewAirportForm.form.bindFromRequest.fold(
      error => Future.successful(BadRequest(views.html.addAirport(airportService.getAirports)))
      ,
      airportData => {
        val name = airportData.name
        val city = airportData.city
        val country = airportData.country
        val code = airportData.code

        airportService.addAirport(name, code, city, country).map { _ =>
          Redirect(routes.HomeController.index)
        }
      }
    )
  }
}
