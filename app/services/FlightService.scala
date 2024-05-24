package services

import models.Flight
import models.Airport
import models.Plane

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class FlightService {

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

  private var flights = List(
      Flight(1, airports(0).id, airports(1).id, LocalDateTime.of(2021, 8, 1, 9, 0), LocalDateTime.of(2021, 8, 1, 12, 0), planes(0).id, "Scheduled"),
      Flight(2, airports(1).id, airports(0).id, LocalDateTime.of(2021, 8, 2, 9, 0), LocalDateTime.of(2021, 8, 2, 12, 0), planes(1).id, "Scheduled"),
      Flight(3, airports(2).id, airports(3).id, LocalDateTime.of(2021, 8, 3, 9, 0), LocalDateTime.of(2021, 8, 3, 12, 0), planes(2).id, "Scheduled"),
      Flight(4, airports(3).id, airports(2).id, LocalDateTime.of(2021, 8, 4, 9, 0), LocalDateTime.of(2021, 8, 4, 12, 0), planes(0).id, "Scheduled"),
      Flight(5, airports(4).id, airports(5).id, LocalDateTime.of(2021, 8, 5, 9, 0), LocalDateTime.of(2021, 8, 5, 12, 0), planes(1).id, "Scheduled"),
      Flight(6, airports(5).id, airports(4).id, LocalDateTime.of(2021, 8, 6, 9, 0), LocalDateTime.of(2021, 8, 6, 12, 0), planes(2).id, "Scheduled")
    )



    def getFlights: List[Flight] = {
      flights
    }

    def getAirports: List[Airport] = {
      airports
    }

    def getPlanes: List[Plane] = {
      planes
    }

    def getFlightById(id: Long): Option[Flight] = {
      flights.find(_.id == id)
    }

  def getAirportById(id: Long): Option[Airport] = {
    airports.find(_.id == id)
  }

  def getPlaneById(id: Long): Option[Plane] = {
    planes.find(_.id == id)
  }

  def deleteFlight(id: Long): Unit = {
    flights = flights.filterNot(_.id == id)
  }


  def addFlight(flight: Flight): Future[Unit] = Future {
    flights = flights :+ flight
  }



}