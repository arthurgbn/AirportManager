package modules

import com.google.inject.AbstractModule
import play.api.libs.concurrent.PekkoGuiceSupport
import play.api.{Configuration, Environment}
import services.FlightService
import play.api.db.Database



class MyModule(environment: Environment, configuration: Configuration) extends AbstractModule with PekkoGuiceSupport {
  override def configure(): Unit = {
    bind(classOf[FlightService]).toInstance(new FlightService(play.api.db.Databases.inMemory()))

  }
}