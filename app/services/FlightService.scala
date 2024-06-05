package services

import anorm.SqlParser.{get, scalar}
import anorm.{RowParser, SQL, ~}
import com.google.inject.Inject
import models.Flight
import play.api.db.Database

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps


class FlightService @Inject()(db: Database) {

  val simple: RowParser[Flight] = {
    get[Long]("id") ~ get[Long]("departure_airport_id") ~ get[Long]("arrival_airport_id") ~ get[LocalDateTime]("departure_time") ~ get[LocalDateTime]("arrival_time") ~ get[Long]("plane_id") ~ get[String]("status") map { case id ~ departureAirportId ~ arrivalAirportId ~ departureTime ~ arrivalTime ~ planeId ~ status => Flight(id, departureAirportId, arrivalAirportId, departureTime, arrivalTime, planeId, status)
    }
  }

  def getFlightById(id: Long): Option[Flight] = {
    db.withConnection { implicit connection =>
      SQL("SELECT * FROM flights WHERE id = $id").as(simple.singleOpt)
    }
  }

  def deleteFlight(id: Long): Unit = {
    db.withConnection { implicit connection =>
      SQL("DELETE FROM flights WHERE id = {id}").on("id" -> id).executeUpdate()
    }
  }

  def addFlight(flight: Flight): Future[Long] = {
    Future {
      db.withConnection { implicit connection =>
        SQL(
          """
          INSERT INTO flights (departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status)
          VALUES ({departureAirportId}, {arrivalAirportId}, {departureTime}, {arrivalTime}, {planeId}, {status})
        """).on("departureAirportId" -> flight.departureAirportId, "arrivalAirportId" -> flight.arrivalAirportId, "departureTime" -> flight.departureTime, "arrivalTime" -> flight.arrivalTime, "planeId" -> flight.planeId, "status" -> flight.status).executeInsert(scalar[Long].single)
      }
    }
  }

  def getSortedFlights(sortBy: String, column: String): List[Flight] = {
    val flights = getFlights
    val airports = new AirportService(db).getAirports
    val planes = new PlaneService(db).getPlanes
    (sortBy, column) match {
      case ("asc", "id") => flights.sortBy(_.id)
      case ("desc", "id") => flights.sortBy(_.id).reverse // more difficult to sort by airport name, because we need to join with the airport table to get the name
      case ("asc", "departureAirport") => flights.sortBy(flight => airports.find(_.id == flight.departureAirportId).get.name)
      case ("desc", "departureAirport") => flights.sortBy(flight => airports.find(_.id == flight.departureAirportId).get.name).reverse
      case ("asc", "arrivalAirport") => flights.sortBy(flight => airports.find(_.id == flight.arrivalAirportId).get.name)
      case ("desc", "arrivalAirport") => flights.sortBy(flight => airports.find(_.id == flight.arrivalAirportId).get.name).reverse
      case ("asc", "departureTime") => flights.sortBy(_.departureTime)
      case ("desc", "departureTime") => flights.sortBy(_.departureTime).reverse
      case ("asc", "arrivalTime") => flights.sortBy(_.arrivalTime)
      case ("desc", "arrivalTime") => flights.sortBy(_.arrivalTime).reverse
      case ("asc", "plane") => flights.sortBy(flight => planes.find(_.id == flight.planeId).get.model)
      case ("desc", "plane") => flights.sortBy(flight => planes.find(_.id == flight.planeId).get.model).reverse
      case ("asc", "status") => flights.sortBy(_.status)
      case ("desc", "status") => flights.sortBy(_.status).reverse
      case _ => flights
    }
  }

  def getFilteredFlights(planeId: Option[Long], departureAirportId: Option[Long], arrivalAirportId: Option[Long], status: Option[String]): List[Flight] = {
    getFlights.filter(flight => planeId.forall(_ == flight.planeId) && departureAirportId.forall(_ == flight.departureAirportId) && arrivalAirportId.forall(_ == flight.arrivalAirportId) && status.forall(_ == flight.status))
  }

  def getFlights: List[Flight] = {
    db.withConnection { implicit connection =>
      SQL("SELECT * FROM flights").as(simple *)
    }
  }

}