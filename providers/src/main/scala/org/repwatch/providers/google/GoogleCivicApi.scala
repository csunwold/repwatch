package org.repwatch.providers.google

import dispatch._
import net.liftweb.json._
import org.repwatch.models.ZipCode
import org.repwatch.providers.google.models.RepresentativeInfoQueryResponse

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * An API client that wraps Google's Civic API.
  *
  * https://developers.google.com/civic-information/docs/v2/
  */
class GoogleCivicApi(apiKey: ApiKey) {
  def findSenators(zipCode: ZipCode) : Future[RepresentativeInfoQueryResponse] = {
    val svc = url("https://www.googleapis.com/civicinfo/v2/representatives")
      .addQueryParameter("address", zipCode.value)
      .addQueryParameter("key", apiKey.key)
      .addQueryParameter("includeOffices", "true")
      .addQueryParameter("roles", "legislatorUpperBody")

    val responseBody = Http(svc OK as.String)

    responseBody
      .map(body => {
        val json = JsonParser.parse(body)
        implicit val formats = DefaultFormats

        json.extract[RepresentativeInfoQueryResponse]
      })
  }
}
