package forms

import play.api.data.Form
import play.api.data.Forms._

object NewFlightForm {
  val form: Form[Data] = Form(
    mapping(
      "departureAirport" -> nonEmptyText,
      "arrivalAirport" -> nonEmptyText,
      "departureTime" -> nonEmptyText,
      "arrivalTime" -> nonEmptyText,
      "plane" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )

  case class Data(departureAirport: String, arrivalAirport: String, departureTime: String, arrivalTime: String, plane: String)
}
