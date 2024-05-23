package services

import models.Plane

class PlaneService {

  private val planes = List(
    Plane(1, "Boeing 747", 400),
    Plane(2, "Airbus A380", 600),
    Plane(3, "Boeing 737", 200)
  )

  def getPlanes(): List[Plane] = {
    planes
  }

  def getPlaneById(id: Long): Option[Plane] = {
    planes.find(_.id == id)
  }

}

