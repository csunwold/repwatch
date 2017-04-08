package org.repwatch.providers.google

import dispatch.as
import dispatch.Future
import dispatch.Http
import dispatch.url
import net.liftweb.json.DefaultFormats
import net.liftweb.json.JsonParser
import org.repwatch.models.ZipCode
import org.repwatch.providers.google.models.RepresentativeInfoQueryResponse

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * An API client that wraps Google's Civic API.
  *
  * https://developers.google.com/civic-information/docs/v2/
  */
class GoogleCivicApi(apiKey: ApiKey) {
  def findRepresentative(zipCode: ZipCode) : Future[RepresentativeInfoQueryResponse] = {
    getRepresentativeInfoQueryResponse(role = "legislatorLowerBody", zipCode = zipCode)
  }

  def findSenators(zipCode: ZipCode) : Future[RepresentativeInfoQueryResponse] = {
    getRepresentativeInfoQueryResponse(role = "legislatorUpperBody", zipCode = zipCode)
  }

  // TODO - convert role to case class type
  private def getRepresentativeInfoQueryResponse(role: String, zipCode: ZipCode) = {
    val svc = url("https://www.googleapis.com/civicinfo/v2/representatives")
      .addQueryParameter("address", zipCode.value)
      .addQueryParameter("key", apiKey.key)
      .addQueryParameter("includeOffices", "true")
      .addQueryParameter("roles", role)

    val responseBody = Http(svc OK as.String)

    responseBody
      .map(body => {
        val json = JsonParser.parse(body)
        implicit val formats = DefaultFormats

        json.extract[RepresentativeInfoQueryResponse]
      })
  }
}
