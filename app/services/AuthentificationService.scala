package services

import anorm.SqlParser.get
import anorm.{RowParser, SQL, ~}
import com.google.inject.Inject
import models.User
import play.api.db.Database

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps

class AuthentificationService @Inject()(db: Database) {

  val simpleUser: RowParser[User] = {
    get[Long]("id") ~ get[String]("username") ~ get[String]("password") map { case id ~ username ~ password => User(id, username, password)
    }
  }

  def authentification(username: String, password: String): Boolean = {
    db.withConnection { implicit connection =>
      SQL("SELECT * FROM Users WHERE username = {username} AND password = {password}").on("username" -> username, "password" -> password).as(simpleUser.singleOpt).isDefined
    }
  }

}
