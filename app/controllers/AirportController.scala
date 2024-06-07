package controllers

import play.api.mvc._
import services.AirportService

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class AirportController @Inject()(val controllerComponents: ControllerComponents, airportService: AirportService) extends BaseController {

  def delete(id: Long): Action[AnyContent] = Action {
    val success = airportService.deleteAirport(id)
    if (success) {
      Redirect(routes.HomeController.index)
    } else {
      Redirect(routes.HomeController.index).flashing("error" -> "Cannot delete airport because it is used in a flight.")
    }
  }

  def create: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    implicit val ec: ExecutionContext = ExecutionContext.global

    val name = request.body.asFormUrlEncoded.get("name").head
    val city = request.body.asFormUrlEncoded.get("city").head
    val country = request.body.asFormUrlEncoded.get("country").head
    val code = request.body.asFormUrlEncoded.get("code").head

    airportService.addAirport(name, code, city, country).map { _ =>
      Redirect(routes.HomeController.index)

    }
  }
}
