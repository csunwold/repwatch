package org.repwatch.repositories

import org.repwatch.models._
import org.repwatch.providers.google.GoogleCivicApi

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

class GoogleLegislatorRepository(client: GoogleCivicApi) extends LegislatorRepository {
  override def locateRepresentative(zipCode: ZipCode): Future[Option[Representative]] = {
    for (response <- client.findRepresentative(zipCode))
      yield response
        .officials
        .flatMap(official => {
          for (name <- Name(official.name))
            yield new Representative(
              contactInformation =
                new ContactInformation(
                  emailAddress = new EmailAddress("test@test.com"),
                  phoneNumber = new PhoneNumber()),
              name = name
            )
        }).headOption
  }

  override def locateSenators(zipCode: ZipCode): Future[Array[Senator]] = {
    for (response <- client.findSenators(zipCode))
      yield response
        .officials
        .flatMap(official => {
          for (name <- Name(official.name))
            yield new Senator(
              contactInformation =
                new ContactInformation(
                  emailAddress = new EmailAddress("test@test.com"),
                  phoneNumber = new PhoneNumber()),
              name = name
            )
        }).toArray
  }
}
