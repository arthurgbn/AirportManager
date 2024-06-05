package services

import anorm.SqlParser.get
import anorm.{RowParser, SQL, ~}
import com.google.inject.Inject
import models.Airport
import play.api.db.Database

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps


class AirportService @Inject()(db: Database) {

  val simpleAirport: RowParser[Airport] = {
    get[Long]("id") ~ get[String]("name") ~ get[String]("code") ~ get[String]("city") ~ get[String]("country") map { case id ~ name ~ code ~ city ~ country => Airport(id, name, code, city, country)
    }
  }

  def getAirports: List[Airport] = {
    db.withConnection { implicit connection =>
      SQL("SELECT * FROM airports").as(simpleAirport *)
    }
  }

  def getAirportById(id: Long): Option[Airport] = {
    db.withConnection { implicit connection =>
      SQL("SELECT * FROM airports WHERE id = $id").as(simpleAirport.singleOpt)
    }
  }

  // delete an airport by id from the database if it exists and it is not used in any flight
  def deleteAirport(id: Long): Boolean = {
    db.withConnection { implicit connection =>
      val updateCount = SQL("DELETE FROM airports WHERE id = {id} AND id NOT IN (SELECT departure_airport_id FROM flights) AND id NOT IN (SELECT arrival_airport_id FROM flights)").on("id" -> id).executeUpdate()
      updateCount > 0
    }
  }

  // add an airport to the database
  def addAirport(name: String, code: String, city: String, country: String): Future[Long] = {
    Future {
      db.withConnection { implicit connection =>
        SQL(
          """
          INSERT INTO airports (name, code, city, country)
          VALUES ({name}, {code}, {city}, {country})
          """).on("name" -> name, "code" -> code, "city" -> city, "country" -> country).executeInsert(get[Long]("id").single)
      }
    }
  }


}
