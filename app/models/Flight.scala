package models

import java.time.LocalDateTime

case class Flight(
                   id: Long,
                   departureAirport: Airport,
                   arrivalAirport: Airport,
                   departureTime: LocalDateTime,
                   arrivalTime: LocalDateTime,
                   plane: Plane,
                   status: String
                 )
