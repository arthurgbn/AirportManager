package controllers

import models.Flight

import javax.inject._
import play.api.mvc._
import services.FlightService

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class FlightController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  val flightService = new FlightService()



  val flights: Seq[Flight] = flightService.getFlights()


    /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
    def index() = Action { implicit request: Request[AnyContent] =>
      Ok(views.html.flights(flights))
    }

}
