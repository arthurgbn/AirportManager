package controllers

import models.{Airport, Flight, Plane}

import javax.inject._
import play.api.mvc._
import play.api.mvc
import services.FlightService

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FlightController @Inject()(val controllerComponents: ControllerComponents, flightService: FlightService) extends BaseController {

  def index: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val flights = flightService.getFlights
    Ok(views.html.flights(flights))
  }

  def delete(id: Long): Action[AnyContent] = Action {
    flightService.deleteFlight(id)
    Redirect(routes.FlightController.index)
  }

  def addFlight: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.addFlight(flightService.getFlights, flightService.getAirports, flightService.getPlanes))
  }

  def create: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    implicit val ec: ExecutionContext = ExecutionContext.global

    println("test " + request.body.asFormUrlEncoded.get("departureTime"))
    val departureAirportId = request.body.asFormUrlEncoded.get("departureAirport").head.toLong
    // to display info in the logs :
    println("departureAirportId: " + departureAirportId)
    val arrivalAirportId = request.body.asFormUrlEncoded.get("arrivalAirport").head.toLong
    val departureTime = request.body.asFormUrlEncoded.get("departureTime").head
    val arrivalTime = request.body.asFormUrlEncoded.get("arrivalTime").head
    val planeId = request.body.asFormUrlEncoded.get("plane").head.toLong

      val airports = flightService.getAirports
      val planes = flightService.getPlanes
      val flights = flightService.getFlights
      val newFlight = Flight(
        id = flights.size + 1,
        departureAirport = airports.find(_.id == departureAirportId).getOrElse(Airport(0, "", "", "", "")),
        arrivalAirport = airports.find(_.id == arrivalAirportId).getOrElse(Airport(0, "", "", "", "")),
        departureTime = LocalDateTime.parse(departureTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
        arrivalTime = LocalDateTime.parse(arrivalTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
        plane = planes.find(_.id == planeId).getOrElse(Plane(0, "", 0)),
        status = "Scheduled"
      )

      flightService.addFlight(newFlight).map { _ =>
        Redirect(routes.FlightController.index)
      }
    }




}