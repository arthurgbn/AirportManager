package services

import javax.inject.Inject
import play.api.db.Database
import anorm.SqlParser.get
import anorm.{SQL, ~}
import play.silhouette.api.util.PasswordInfo
import play.silhouette.persistence.daos.DelegableAuthInfoDAO
import play.silhouette.api.LoginInfo
import scala.language.postfixOps
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

class PasswordService  @Inject()(db: Database)(implicit ec: ExecutionContext) extends DelegableAuthInfoDAO[PasswordInfo] {
  private val simple = {
    get[String]("passwords.hash") ~
      get[String]("passwords.password") ~
      get[String]("passwords.email") map {
      case hash ~ password ~ email => PasswordInfo(hash, password)
    }
  }

  override val classTag: ClassTag[PasswordInfo] = scala.reflect.classTag[PasswordInfo]

  override def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
    println("find")
    Future {
      db.withConnection { implicit connection =>
        SQL("SELECT * FROM passwords WHERE email = {email}").on("email" -> loginInfo.providerKey).as(simple.singleOpt)
      }
    }
  }

  override def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] =
    Future {
      db.withConnection { implicit connection =>
        SQL(
          """
            INSERT INTO passwords(hash, password, email)
            VALUES ({hash}, {password}, {email})
          """
        ).on(
          "hash" -> authInfo.hasher,
          "password" -> authInfo.password,
          "email" -> loginInfo.providerKey
        ).executeInsert()
        authInfo
      }
    }

  override def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] =
    Future {
      db.withConnection { implicit connection =>
        SQL(
          """
            UPDATE passwords
            SET hash = {hash}, password = {password}
            WHERE email = {email}
          """
        ).on(
          "hash" -> authInfo.hasher,
          "password" -> authInfo.password,
          "email" -> loginInfo.providerKey
        ).executeUpdate()
        authInfo
      }
    }

  override def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] =
    Future {
      db.withConnection { implicit connection =>
        SQL(
          """
            INSERT INTO passwords(hash, password, email)
            VALUES ({hash}, {password}, {email})
          """
        ).on(
          "hash" -> authInfo.hasher,
          "password" -> authInfo.password,
          "email" -> loginInfo.providerKey
        ).executeInsert()
        authInfo
      }
    }

  override def remove(loginInfo: LoginInfo): Future[Unit] =
    Future {
      db.withConnection { implicit connection =>
        SQL("DELETE FROM passwords WHERE email = {email}").on("email" -> loginInfo.providerKey).executeUpdate()
      }
    }
}
