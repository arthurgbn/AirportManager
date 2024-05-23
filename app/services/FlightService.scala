package services

import models.Flight
import models.Airport
import models.Plane

import java.time.LocalDateTime
import play.api.db.Database
import anorm._
import anorm.SqlParser.get


class FlightService(db: Database) {

  private val simple = {
    get[Long]("flight.id") ~
      get[Long]("flight.departure_airport_id") ~
      get[Long]("flight.arrival_airport_id") ~
      get[LocalDateTime]("flight.departure_time") ~
      get[LocalDateTime]("flight.arrival_time") ~
      get[Long]("flight.plane_id") ~
      get[String]("flight.status") map {
      case id ~ departureAirportId ~ arrivalAirportId ~ departureTime ~ arrivalTime ~ planeId ~ status => Flight(id, Airport(departureAirportId), Airport(arrivalAirportId), departureTime, arrivalTime, PlaneService.getPlaneById(planeId).get, status)
    }
  }

  def options: Seq[(String, String)] = db.withConnection { implicit connection =>
    SQL("select * from flight order by departure_time desc").as(simple *).map(c => c.id.toString -> c.departureTime.toString)
  }

  def findById(id: Long): Option[Flight] = {
    db.withConnection { implicit connection =>
      SQL(
        """
          select * from flight
          where id = {id}
        """
      ).on(
        "id" -> id
      ).as(simple.singleOpt)
    }
  }

  def findByDepartureAirport(departureAirport: Long): Seq[Flight] = {
    db.withConnection { implicit connection =>
      SQL(
        """
          select * from flight
          where departure_airport_id = {departureAirport}
          order by departure_time
        """
      ).on(
        "departureAirport" -> departureAirport
      ).as(simple *)
    }
  }

  def findByArrivalAirport(arrivalAirport: Long): Seq[Flight] = {
    db.withConnection { implicit connection =>
      SQL(
        """
          select * from flight
          where arrival_airport_id = {arrivalAirport}
          order by arrival_time
        """
      ).on(
        "arrivalAirport" -> arrivalAirport
      ).as(simple *)
    }
  }

  def insert(flight: Flight): Unit = {
    db.withConnection { implicit connection =>
      val sql = SQL("insert into flight(departure_airport_id, arrival_airport_id, departure_time, arrival_time, plane_id, status) values({departureAirportId}, {arrivalAirportId}, {departureTime

