package controllers

import models.Flight
import play.api.mvc._
import services.{AirportService, FlightService, PlaneService}

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import forms.NewFlightForm

@Singleton class FlightController @Inject()(val controllerComponents: ControllerComponents, flightService: FlightService, airportService: AirportService, planeService: PlaneService) extends BaseController {


  def delete(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    request.session.get("username") match {
      case Some(_) =>
        flightService.deleteFlight(id)
        Redirect(routes.HomeController.planes)
      case None =>
        Redirect(routes.AuthController.showLoginForm).flashing("error" -> "You must be authenticated to delete a flight.")
    }
  }





  def create: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    implicit val ec: ExecutionContext = ExecutionContext.global

    NewFlightForm.form.bindFromRequest.fold(
      error => Future.successful(BadRequest(views.html.flights(flightService.getFlights, airportService.getAirports, planeService.getPlanes))),
      flightData => {
        val departureAirportId = flightData.departureAirport.toLong
        val arrivalAirportId = flightData.arrivalAirport.toLong
        val departureTime = LocalDateTime.parse(flightData.departureTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
        val arrivalTime = LocalDateTime.parse(flightData.arrivalTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
        val planeId = flightData.plane.toLong

        val newFlight = Flight(
          id = 0L,
          departureAirportId = departureAirportId,
          arrivalAirportId = arrivalAirportId,
          departureTime = departureTime,
          arrivalTime = arrivalTime,
          planeId = planeId,
          status = "Scheduled"
        )

        flightService.addFlight(newFlight).map { _ =>
          Redirect(routes.HomeController.planes)
        }
      }
    )
  }


  def filterFlights(planeId: Option[Long], departureAirportId: Option[Long], arrivalAirportId: Option[Long], status: Option[String]): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.flights(flightService.getFilteredFlights(planeId, departureAirportId, arrivalAirportId, status), airportService.getAirports, planeService.getPlanes)).flashing(request.flash)
  }
}