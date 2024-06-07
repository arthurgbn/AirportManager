// services/UserService.scala
package services

import anorm.SqlParser.{get, scalar}
import anorm.{SQL, ~}
import com.google.inject.Inject
import models.User
import play.api.db.Database

class UserService @Inject()(db: Database) {

  val simple = {
    get[Long]("id") ~ get[String]("username") ~ get[String]("password") map {
      case id ~ username ~ password => User(id, username, password)
    }
  }

  def findByUsername(username: String): Option[User] = {
    db.withConnection { implicit connection =>
      SQL("SELECT * FROM users WHERE username = {username}")
        .on("username" -> username)
        .as(simple.singleOpt)
    }
  }

  def authenticate(username: String, password: String): Option[User] = {
    findByUsername(username).filter(user => user.password == password)
  }

  def createUser(user: User): Long = {
    db.withConnection { implicit connection =>
      SQL(
        """
          INSERT INTO users (username, password)
          VALUES ({username}, {password})
        """
      ).on("username" -> user.username, "password" -> user.password).executeInsert(scalar[Long].single)
    }
  }
}