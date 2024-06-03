package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import services.{AirportService, FlightService, PlaneService}

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, flightService: FlightService, airportService: AirportService, planeService: PlaneService) extends BaseController {

  def index: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(flightService.getFlights, airportService.getAirports, planeService.getPlanes)).flashing(request.flash)
  }

  def goToAddFlight(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.addFlight(flightService.getFlights, airportService.getAirports, planeService.getPlanes))
  }

  def goToAddPlane(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.addPlane(planeService.getPlanes))
  }

  def goToAddAirport(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.addAirport(airportService.getAirports))
  }

  def flights: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.flights(flightService.getFlights, airportService.getAirports, planeService.getPlanes))
  }

  def airports: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.airports(airportService.getAirports))
  }

  def planes: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.planes(planeService.getPlanes))
  }

}
