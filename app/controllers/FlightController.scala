package controllers

import models.Flight
import play.api.mvc._
import services.{AirportService, FlightService, PlaneService}

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import forms.NewFlightForm
import play.silhouette.api.Silhouette
import utils.DefaultEnv

@Singleton class FlightController @Inject()(val controllerComponents: ControllerComponents,  silhouette: Silhouette[DefaultEnv], flightService: FlightService, airportService: AirportService, planeService: PlaneService) extends BaseController {


  def delete(id: Long): Action[AnyContent] = silhouette.SecuredAction { implicit request =>
    flightService.deleteFlight(id)
    Redirect(routes.HomeController.index)
  }

  def create: Action[AnyContent] = silhouette.SecuredAction.async { implicit request: Request[AnyContent] =>
    implicit val ec: ExecutionContext = ExecutionContext.global

    NewFlightForm.form.bindFromRequest.fold(
      error => Future.successful(BadRequest(views.html.addFlight(flightService.getFlights, airportService.getAirports, planeService.getPlanes))),
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
          Redirect(routes.HomeController.index)
        }
      }
    )
  }

  def filterFlights(planeId: Option[Long], departureAirportId: Option[Long], arrivalAirportId: Option[Long], status: Option[String]): Action[AnyContent] = silhouette.SecuredAction { implicit request =>
    Ok(views.html.index(flightService.getFilteredFlights(planeId, departureAirportId, arrivalAirportId, status), airportService.getAirports, planeService.getPlanes)).flashing(request.flash)
  }
}