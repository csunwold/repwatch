package org.repwatch.repositories

import org.repwatch.models.{GeoCoordinate, Legislator}

import scala.concurrent.Future

trait LegislatorRepository {
  def locate(location: GeoCoordinate) : Future[Legislator]
}
