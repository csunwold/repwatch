package org.repwatch

import org.repwatch.models.{Representative, Senator, ZipCode}
import org.repwatch.repositories.LegislatorRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class StubLegislatorRepository(representatives: Map[ZipCode, Representative] = Map.empty,
                               senators: Map[ZipCode, Array[Senator]] = Map.empty) extends LegislatorRepository {

  override def locateRepresentative(zipCode: ZipCode): Future[Option[Representative]] = Future {
    representatives.get(zipCode)
  }

  override def locateSenators(zipCode: ZipCode): Future[Array[Senator]] = Future {
    senators
      .get(zipCode)
      .getOrElse(Array.empty)
  }
}
