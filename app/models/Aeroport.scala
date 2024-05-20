package models

import play.api.libs.json.Json

case class Aeroport(
                     id: Option[Long],
                     nom: String,
                     codeIata: String,
                     ville: String,
                     pays: String
                   )

object Aeroport {
  implicit val aeroportFormat = Json.format[Aeroport]
}
