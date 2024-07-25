  package controllers

  import play.api.mvc._
  import services.{AirportService, FlightService, PlaneService}

  import javax.inject._

  @Singleton class HomeController @Inject()(val controllerComponents: ControllerComponents, flightService: FlightService, airportService: AirportService, planeService: PlaneService) extends BaseController {

    def index: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
      request.session.get("username") match {
        case Some(username) =>
          Ok(views.html.flights(flightService.getFlights, airportService.getAirports, planeService.getPlanes))
        case None =>
          Redirect(routes.AuthController.showLoginForm)
      }
    }

    def flights: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
      Ok(views.html.flights(flightService.getFlights, airportService.getAirports, planeService.getPlanes))
    }

    def airports: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
      Ok(views.html.airports(airportService.getAirports))
    }

    def planes: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
      Ok(views.html.planes(planeService.getPlanes))
    }

    def logout = Action {
      Redirect(routes.AuthController.showLoginForm).withNewSession.flashing("success" -> "You are now logged out.")
    }

  }
