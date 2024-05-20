package controllers

import models.{Aeroport, Avion, Vol}

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import play.api.mvc.Results.Ok


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class VolController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def list() = Action {
    val vols = List(
      Vol(1, "AF123", new java.sql.Timestamp(0), new java.sql.Timestamp(0), Avion(Some(1), "Boeing 747", 416), Aeroport(Some(1), "Charles de Gaulle", "CDG", "Paris", "France"), Aeroport(Some(2), "JFK", "JFK", "New York", "USA")),
      Vol(2, "AF456", new java.sql.Timestamp(0), new java.sql.Timestamp(0), Avion(Some(2), "Airbus A380", 525), Aeroport(Some(2), "JFK", "JFK", "New York", "USA"), Aeroport(Some(3), "Dubai International", "DXB", "Dubai", "United Arab Emirates")
      ))
        Ok(views.html.vols(vols))
  }


}