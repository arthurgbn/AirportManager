package forms

import play.api.data.Form
import play.api.data.Forms._

object NewAirportForm {
  val form = Form(
    mapping(
      "name" -> nonEmptyText,
      "city" -> nonEmptyText,
      "country" -> nonEmptyText,
      "code" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )

  case class Data(name: String, city: String, country: String, code: String)





}
