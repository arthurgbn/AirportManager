package models

import play.silhouette.api.{Identity}
case class User(id: Long, name: String, email: String) extends Identity