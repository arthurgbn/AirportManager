package forms

import play.api.data.Form
import play.api.data.Forms._

object NewPlaneForm {
  val form = Form(
    mapping(
      "model" -> nonEmptyText,
      "capacity" -> number(min = 1)
    )(Data.apply)(Data.unapply)
  )

  case class Data(model: String, capacity: Int)

}
