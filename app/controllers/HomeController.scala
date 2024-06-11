  package controllers

  import play.api.mvc._
  import play.silhouette
  import play.silhouette.api.Silhouette
  import services.{AirportService, FlightService, PlaneService}
  import utils.DefaultEnv

  import javax.inject._

  @Singleton class HomeController @Inject()(val controllerComponents: ControllerComponents,silhouette: Silhouette[DefaultEnv], flightService: FlightService, airportService: AirportService, planeService: PlaneService) extends BaseController {

def index: Action[AnyContent] = silhouette.SecuredAction { implicit request =>
      Ok(views.html.index(flightService.getFlights, airportService.getAirports, planeService.getPlanes))
}

      def goToAddFlight(): Action[AnyContent] = silhouette.SecuredAction { implicit request =>
        Ok(views.html.addFlight(flightService.getFlights, airportService.getAirports, planeService.getPlanes))
      }

      def goToAddPlane(): Action[AnyContent] = silhouette.SecuredAction { implicit request =>
        Ok(views.html.addPlane(planeService.getPlanes))
      }

      def goToAddAirport(): Action[AnyContent] = silhouette.SecuredAction { implicit request =>
        Ok(views.html.addAirport(airportService.getAirports))
      }

      def flights: Action[AnyContent] = silhouette.SecuredAction { implicit request =>
        Ok(views.html.flights(flightService.getFlights, airportService.getAirports, planeService.getPlanes))
      }

      def airports: Action[AnyContent] = silhouette.SecuredAction { implicit request =>
        Ok(views.html.airports(airportService.getAirports))
      }

      def planes: Action[AnyContent] = silhouette.SecuredAction { implicit request =>
        Ok(views.html.planes(planeService.getPlanes))
      }

      def logout: Action[AnyContent] = silhouette.SecuredAction {
        Redirect(routes.AuthController.showLoginForm).withNewSession.flashing("success" -> "You are now logged out.")
      }

  }
