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


  val simpleAirport: RowParser[Airport] = {
    get[Long]("id") ~
      get[String]("name") ~
      get[String]("code") ~
      get[String]("city") ~
      get[String]("country") map {
      case id ~ name ~ code ~ city ~ country => Airport(id, name, code, city, country)
    }
  }

  val simplePlane: RowParser[Plane] = {
    get[Long]("id") ~
      get[String]("model") ~
      get[Int]("capacity") map {
      case id ~ model ~ capacity => Plane(id, model, capacity)
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
        Flight(id, departureAirportId, arrivalAirportId, departureTime, arrivalTime, planeId, status)
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
      SQL("DELETE FROM flights WHERE id = {id}")
        .on("id" -> id)
        .executeUpdate()
    }
  }

  def addFlight(flight: Flight): Future[Long] = {
    Future {
      db.withConnection { implicit connection =>
        SQL(
          """
          INSERT INTO flights (departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status)
          VALUES ({departureAirportId}, {arrivalAirportId}, {departureTime}, {arrivalTime}, {planeId}, {status})
        """
        ).on(
          "departureAirportId" -> flight.departureAirportId,
          "arrivalAirportId" -> flight.arrivalAirportId,
          "departureTime" -> flight.departureTime,
          "arrivalTime" -> flight.arrivalTime,
          "planeId" -> flight.planeId,
          "status" -> flight.status
        ).executeInsert(scalar[Long].single)
      }
    }
  }

}