package org.repwatch.repositories

import org.repwatch.models.{GeoCoordinate, Legislator}
import org.scalatest.AsyncFlatSpec
import org.scalatest.Matchers._
import org.scalatest.OptionValues._

trait LegislatorRepositoryBehaviors { this: AsyncFlatSpec =>
  def repositoryWithoutLegislators(newRepo: => LegislatorRepository) = {
    it should "not return a legislator" in {
      val futureLegislator = newRepo.locate(GeoCoordinate(0, 0))

      futureLegislator.map { legislator => legislator should be (None) }
    }
  }

  def repositoryWithLegislatorInLocation(newRepo: => LegislatorRepository,
                                         geoCoordinate: GeoCoordinate,
                                         knownLegislator: Legislator) = {
    val futureLegislator = newRepo.locate(geoCoordinate)

    futureLegislator.map { legislator => legislator.value should be (knownLegislator)}
  }
}
