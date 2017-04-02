package org.repwatch.repositories

import org.repwatch.models.{Representative, Senator, ZipCode}

import scala.concurrent.Future

trait LegislatorRepository {
  def locateRepresentative(zipCode: ZipCode) : Future[Option[Representative]]

  def locateSenators(zipCode: ZipCode) : Future[Array[Senator]]
}
