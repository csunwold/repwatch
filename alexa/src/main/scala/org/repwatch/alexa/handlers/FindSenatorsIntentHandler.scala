package org.repwatch.alexa.handlers

import com.amazon.speech.speechlet.SpeechletResponse
import com.amazon.speech.ui.PlainTextOutputSpeech
import dispatch._
import net.liftweb.json._
import org.repwatch.alexa.FindSenatorsIntent
import org.repwatch.config.ApplicationConfig
import org.repwatch.models._

import scala.concurrent.ExecutionContext.Implicits.global

object FindSenatorsIntentHandler {
  def handle(intent: FindSenatorsIntent, config: ApplicationConfig, user: User) : SpeechletResponse = {
    val response = new SpeechletResponse

    val output = new PlainTextOutputSpeech

    val senators = findSenators(user.zipCode, config.googleApiKey)

    output.setText(s"Your senators are ${senators(0).toString} and ${senators(1).toString}")

    response.setOutputSpeech(output)

    response
  }

  private def findSenators(zipCode: ZipCode, apiKey: String) : Array[Senator] = {
    val svc = url("https://www.googleapis.com/civicinfo/v2/representatives")
      .addQueryParameter("address", zipCode.value)
      .addQueryParameter("key", apiKey)
      .addQueryParameter("includeOffices", "true")
      .addQueryParameter("roles", "legislatorUpperBody")

    val responseBody = Http(svc OK as.String)

    val json = JsonParser.parse(responseBody())
    implicit val formats = DefaultFormats

    val response = json.extract[Response]

    response
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

  case class Response(kind: String, officials: Seq[Official])

  case class Official(name: String)
}
