package services

import anorm.SqlParser.{get, long, str}
import anorm.{SQL, ~}
import com.google.inject.Inject
import play.silhouette.api.LoginInfo
import play.silhouette.api.services.IdentityService
import models.User
import play.api.db.Database

import scala.concurrent.{ExecutionContext, Future}



class UserService @Inject()(db: Database)(implicit ec: ExecutionContext) extends IdentityService[User] {


  private val simple = {
    get[Long]("users.id") ~
      get[String]("users.name") ~
      get[String]("users.email") map {
      case id ~ name ~ email => User(id, name, email)
    }
  }

  def retrieve(loginInfo: LoginInfo): Future[Option[User]] = Future {
    db.withConnection { implicit connection =>
      SQL("SELECT * FROM users WHERE email = {email}").on("email" -> loginInfo.providerKey).as(simple.singleOpt)
    }
  }

  def save(user: User): Future[User] = Future {
    db.withConnection { implicit connection =>
      SQL(
        """
          INSERT INTO users(name, email)
          VALUES ({name}, {email})
        """
      ).on(
        "name" -> user.name,
        "email" -> user.email
      ).executeInsert()
      user
    }
  }

}
