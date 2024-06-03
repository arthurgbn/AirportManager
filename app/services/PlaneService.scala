package services

import anorm.{RowParser, SQL, ~}
import anorm.SqlParser.{get, scalar}
import com.google.inject.Inject
import models.Plane
import play.api.db.Database
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps


class PlaneService @Inject()(db: Database) {

  val simplePlane: RowParser[Plane] = {
    get[Long]("id") ~
      get[String]("model") ~
      get[Int]("capacity") map {
      case id ~ model ~ capacity => Plane(id, model, capacity)
    }
  }

  def getPlanes: List[Plane] = {
    db.withConnection { implicit connection =>
      SQL("SELECT * FROM planes").as(simplePlane *)
    }
  }

  def getPlaneById(id: Long): Option[Plane] = {
    db.withConnection { implicit connection =>
      SQL("SELECT * FROM planes WHERE id = $id").as(simplePlane.singleOpt)
    }
  }

  // delete a plane by id from the database if it exists and it is not used in any flight
  def deletePlane(id: Long): Unit = {
    db.withConnection { implicit connection =>
      SQL("DELETE FROM planes WHERE id = {id} AND id NOT IN (SELECT plane_id FROM flights)")
        .on("id" -> id)
        .executeUpdate()
    }
  }

  // add a plane to the database
  def addPlane(model: String, capacity: Int): Future[Long] = {
    Future {
      db.withConnection { implicit connection =>
        SQL(
          """
          INSERT INTO planes (model, capacity)
          VALUES ({model}, {capacity})
          """
        ).on(
          "model" -> model,
          "capacity" -> capacity
        ).executeInsert(scalar[Long].single)
      }
    }
  }

}
