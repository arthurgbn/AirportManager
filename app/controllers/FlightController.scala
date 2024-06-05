package controllers

import models.Flight
import play.api.mvc._
import services.{AirportService, FlightService, PlaneService}

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton class FlightController @Inject()(val controllerComponents: ControllerComponents, flightService: FlightService, airportService: AirportService, planeService: PlaneService) extends BaseController {


  def delete(id: Long): Action[AnyContent] = Action {
    flightService.deleteFlight(id)
    Redirect(routes.HomeController.index())
  }


  def create: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    implicit val ec: ExecutionContext = ExecutionContext.global

    val departureAirportId = request.body.asFormUrlEncoded.get("departureAirport").head.toLong
    val arrivalAirportId = request.body.asFormUrlEncoded.get("arrivalAirport").head.toLong
    val departureTime = request.body.asFormUrlEncoded.get("departureTime").head
    val arrivalTime = request.body.asFormUrlEncoded.get("arrivalTime").head
    val planeId = request.body.asFormUrlEncoded.get("plane").head.toLong

    val newFlight = Flight(id = 0L, departureAirportId = departureAirportId, arrivalAirportId = arrivalAirportId, departureTime = LocalDateTime.parse(departureTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")), arrivalTime = LocalDateTime.parse(arrivalTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")), planeId = planeId, status = "Scheduled")

    flightService.addFlight(newFlight).map { _ =>
      Redirect(routes.HomeController.index())
    }
  }

  def filterFlights(planeId: Option[Long], departureAirportId: Option[Long], arrivalAirportId: Option[Long], status: Option[String]): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(flightService.getFilteredFlights(planeId, departureAirportId, arrivalAirportId, status), airportService.getAirports, planeService.getPlanes)).flashing(request.flash)
  }
}