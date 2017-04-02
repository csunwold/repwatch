package org.repwatch.repositories

import org.repwatch.models.{Senator, ZipCode}

import scala.concurrent.Future

trait LegislatorRepository {
  def locateSenators(zipCode: ZipCode) : Future[Array[Senator]]
}
