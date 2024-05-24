package models

import java.time.LocalDateTime

case class Flight(
                   id: Long,
                   departureAirportId: Long,
                   arrivalAirportId: Long,
                   departureTime: LocalDateTime,
                   arrivalTime: LocalDateTime,
                   planeId: Long,
                   status: String
                 )
