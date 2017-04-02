package org.repwatch.repositories

import org.repwatch.models._
import org.repwatch.providers.google.GoogleCivicApi

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

class GoogleLegislatorRepository(client: GoogleCivicApi) extends LegislatorRepository {
  override def locateSenators(zipCode: ZipCode): Future[Array[Senator]] = {
    for (response <- client.findSenators(zipCode))
      yield response
        .officials
        .map(official => {
          new Senator(
            contactInformation =
              new ContactInformation(
                emailAddress = new EmailAddress("test@test.com"),
                phoneNumber = new PhoneNumber()),
            name = new Name(first = official.name.split(' ')(0), last = official.name.split(' ')(1))
          )
        }).toArray
  }
}
