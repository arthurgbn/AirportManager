package models

import java.sql.Timestamp
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Vol(
                id: Int,
                numero: String,
                heureDepart: Timestamp,
                heureArrivee: Timestamp,
                avion: Avion,
                aeroportDepart: Aeroport,
                aeroportArrivee: Aeroport
              )



object Vol {
  implicit val timestampFormat: Format[Timestamp] = (
    (JsPath \ "time").format[Long] and
      (JsPath \ "nanos").format[Int]
    )(
    (time, nanos) => new Timestamp(time),
    (timestamp: Timestamp) => (timestamp.getTime, timestamp.getNanos)
  )

  implicit val volFormat: Format[Vol] = (
    (JsPath \ "id").format[Int] and
      (JsPath \ "numero").format[String] and
      (JsPath \ "heureDepart").format[Timestamp](timestampFormat) and
      (JsPath \ "heureArrivee").format[Timestamp](timestampFormat) and
      (JsPath \ "avion").format[Avion] and
      (JsPath \ "aeroportDepart").format[Aeroport] and
      (JsPath \ "aeroportArrivee").format[Aeroport]
    )(Vol.apply, unlift(Vol.unapply))
}
