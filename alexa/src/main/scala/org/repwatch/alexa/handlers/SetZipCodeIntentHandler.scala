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
    val maybeUser = Option(intent.intentRequest.getIntent.getSlot("ZipCode"))
      .map(_.getValue)
      .flatMap(ZipCode(_))
      .map(zipCode => {
        println(s"Saving zipcode: ${zipCode.value}")
        // TODO - Refactor this to isolate side effects
        session.setAttribute("ZipCode", zipCode.value)

        val user = new User(id = UserId(session.getUser.getUserId), zipCode = zipCode)
        val futureUser = userRepository.save(user)

        Await.result(futureUser, 3 seconds)
      })

    maybeUser match {
      case Some(user) => {
        val outputSpeech = new PlainTextOutputSpeech
        outputSpeech.setText("Thank you")

        val response = new SpeechletResponse()
        response.setShouldEndSession(false)
        response.setOutputSpeech(outputSpeech)

        response
      }
      case None => {
        val output = new PlainTextOutputSpeech
        output.setText("That didn't work.")

        val repromptSpeech = new PlainTextOutputSpeech
        repromptSpeech.setText("Can you please tell me your zip code again?")
        // TODO - Give a zip code response
        val reprompt = new Reprompt
        reprompt.setOutputSpeech(repromptSpeech)

        SpeechletResponse.newAskResponse(output, reprompt)
      }
    }


  }
}
