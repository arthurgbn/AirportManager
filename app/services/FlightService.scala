package services

import anorm.SqlParser.{get, scalar}
import anorm.{RowParser, SQL, ~}
import com.google.inject.Inject
import models.Flight
import models.Airport
import models.Plane

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.db.Database
import scala.language.postfixOps



class FlightService @Inject()(db: Database) {

  private val planes = List(
    Plane(1, "Boeing 747", 400),
    Plane(2, "Airbus A380", 600),
    Plane(3, "Boeing 737", 200)
  )

  private val airports = List(
    Airport(1, "Los Angeles International Airport", "LAX", "Los Angeles", "USA"),
    Airport(2, "JFK International Airport", "JFK", "New York", "USA"),
    Airport(3, "London Heathrow", "LHR", "London", "UK"),
    Airport(4, "Schiphol", "AMS", "Amsterdam", "Netherlands"),
    Airport(5, "Charles de Gaulle", "CDG", "Paris", "France"),
    Airport(6, "Frankfurt Airport", "FRA", "Frankfurt", "Germany")
  )

  val simpleAirport: RowParser[Airport] = {
    get[Long]("id") ~
      get[String]("name") ~
      get[String]("iata") ~
      get[String]("city") ~
      get[String]("country") map {
      case id ~ name ~ iata ~ city ~ country => Airport(id, name, iata, city, country)
    }
  }

  val simplePlane: RowParser[Plane] = {
    get[Long]("id") ~
      get[String]("name") ~
      get[Int]("capacity") map {
      case id ~ name ~ capacity => Plane(id, name, capacity)
    }
  }

  val simple: RowParser[Flight] = {
    get[Long]("id") ~
      get[Long]("departure_airport_id") ~
      get[Long]("arrival_airport_id") ~
      get[LocalDateTime]("departure_time") ~
      get[LocalDateTime]("arrival_time") ~
      get[Long]("plane_id") ~
      get[String]("status") map {
      case id ~ departureAirportId ~ arrivalAirportId ~ departureTime ~ arrivalTime ~ planeId ~ status =>
        Flight(id, airports.find(_.id == departureAirportId).get, airports.find(_.id == arrivalAirportId).get, departureTime, arrivalTime, planes.find(_.id == planeId).get, status)
    }
  }



  def getFlights: List[Flight] = {
    db.withConnection { implicit connection =>
      SQL("SELECT * FROM flights").as(simple *)
    }
  }

  def getAirports: List[Airport] = {
    db.withConnection { implicit connection =>
      SQL("SELECT * FROM airports").as(simpleAirport *)
    }
  }

  def getPlanes: List[Plane] = {
    db.withConnection { implicit connection =>
      SQL("SELECT * FROM planes").as(simplePlane *)
    }
  }

  def getFlightById(id: Long): Option[Flight] = {
    db.withConnection { implicit connection =>
      SQL("SELECT * FROM flights WHERE id = $id").as(simple.singleOpt)
    }
  }

  def getAirportById(id: Long): Option[Airport] = {
    db.withConnection { implicit connection =>
      SQL("SELECT * FROM airports WHERE id = $id").as(simpleAirport.singleOpt)
    }
  }

  def getPlaneById(id: Long): Option[Plane] = {
    db.withConnection { implicit connection =>
      SQL("SELECT * FROM planes WHERE id = $id").as(simplePlane.singleOpt)
    }
  }

  def deleteFlight (id: Long): Unit = {
    db.withConnection { implicit connection =>
      SQL("DELETE FROM flights WHERE id = $id").executeUpdate()
    }
  }

  def addFlight(flight: Flight): Unit = {
    Future {
      db.withConnection { implicit connection =>
        SQL(
          """
            INSERT INTO flights (departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status)
            VALUES ({departureAirportId}, {arrivalAirportId}, {departureTime}, {arrivalTime}, {planeId}, {status})
          """
        ).on(
          "departureAirportId" -> flight.departureAirport.id,
          "arrivalAirportId" -> flight.arrivalAirport.id,
          "departureTime" -> flight.departureTime,
          "arrivalTime" -> flight.arrivalTime,
          "planeId" -> flight.plane.id,
          "status" -> flight.status
        ).executeInsert(scalar[Long].single)
      }
    }
  }

}