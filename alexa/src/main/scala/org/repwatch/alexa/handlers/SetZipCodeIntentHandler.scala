package org.repwatch.alexa.handlers

import com.amazon.speech.speechlet.{Session, SpeechletResponse}
import com.amazon.speech.ui.{PlainTextOutputSpeech, Reprompt}
import org.repwatch.alexa.SetZipCodeIntent
import org.repwatch.models.{User, UserId, ZipCode}
import org.repwatch.repositories.UserRepository

import scala.concurrent.Await
import scala.concurrent.duration._

object SetZipCodeIntentHandler {
  def handle(intent: SetZipCodeIntent, session: Session, userRepository: UserRepository) : SpeechletResponse = {
    val zipCode = Option(intent.intentRequest.getIntent.getSlot("ZipCode"))
      .map(_.getValue)
      .flatMap(ZipCode(_))

    zipCode match {
      case Some(value) => {
        session.setAttribute("ZipCode", value)

        val user = new User(id = UserId(session.getUser.getUserId), zipCode = value)
        val futureUser = userRepository.save(user)

        Await.result(futureUser, 3 seconds)

        // TODO - Was the user trying to do something previously in the session? If so, we should do that now that we
        // have a valid zip code.
        val outputSpeech = new PlainTextOutputSpeech
        outputSpeech.setText("Thank you")

        val response = new SpeechletResponse()
        response.setShouldEndSession(false)
        response.setOutputSpeech(outputSpeech)

        response
      }
      case None => {
        val output = new PlainTextOutputSpeech
        val repromptSpeech = new PlainTextOutputSpeech
        repromptSpeech.setText("That zip code is not valid. Please say your zip code again. For example, <say-as interpret-as=\"digits\">20500</say-as>")
        // TODO - Give a zip code response
        val reprompt = new Reprompt
        reprompt.setOutputSpeech(repromptSpeech)

        SpeechletResponse.newAskResponse(output, reprompt)
      }
    }
  }
}
