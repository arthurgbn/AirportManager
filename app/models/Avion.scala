package models

import play.api.libs.json.Json

case class Avion(
                  id: Option[Long],
                  modele: String,
                  capacite: Int
                )

object Avion {
  implicit val avionFormat = Json.format[Avion]
}
