package org.repwatch.repositories

import org.repwatch.models.{GeoCoordinate, Legislator, ZipCode}
import org.scalatest.AsyncFlatSpec
import org.scalatest.Matchers._

// TODO - Delete these or hook them up in repwatch-providers
trait LegislatorRepositoryBehaviors { this: AsyncFlatSpec =>
  def repositoryWithoutLegislators(newRepo: => LegislatorRepository) = {
    it should "not return a legislator" in {
      val futureLegislator = newRepo.locateSenators(new ZipCode("98001"))

      futureLegislator.map { legislators => legislators.length should be (0) }
    }
  }

  def repositoryWithLegislatorInLocation(newRepo: => LegislatorRepository,
                                         geoCoordinate: GeoCoordinate,
                                         knownLegislator: Legislator) = {
    val futureLegislator = newRepo.locateSenators(new ZipCode("98001"))

    futureLegislator.map { legislators => legislators.head should be (knownLegislator)}
  }
}
