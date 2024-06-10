package forms

import play.api.data.Form
import play.api.data.Forms._

object LoginForm {
  val form = Form(
    mapping(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText
    )(Data.apply)( Data.unapply)
  )
  case class Data(email: String, password: String)
}