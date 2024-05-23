package services

import models.Airport

class AirportService {

  private val airports = List(
    Airport(1, "John F. Kennedy International Airport", "JFK", "New York", "USA"),
    Airport(2, "Los Angeles International Airport", "LAX", "Los Angeles", "USA"),
    Airport(3, "Heathrow Airport", "LHR", "London", "UK")
  )

  def getAirports(): List[Airport] = {
    airports
  }

  def getAirportById(id: Long): Option[Airport] = {
    airports.find(_.id == id)
  }

}
