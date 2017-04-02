package org.repwatch.alexa.handlers

import com.amazon.speech.speechlet.SpeechletResponse
import com.amazon.speech.ui.PlainTextOutputSpeech
import org.repwatch.alexa.FindSenatorsIntent
import org.repwatch.models._
import org.repwatch.repositories.LegislatorRepository

import scala.concurrent.Await
import scala.concurrent.duration._

object FindSenatorsIntentHandler {
  def handle(intent: FindSenatorsIntent, legislatorRepository: LegislatorRepository, user: User) : SpeechletResponse = {

    val senatorsFuture = legislatorRepository.locateSenators(user.zipCode)
    val senators = Await.result(senatorsFuture, 5 seconds)
    val outputText = s"Your senators are ${senators(0).toString} and ${senators(1).toString}"

    val response = new SpeechletResponse
    val output = new PlainTextOutputSpeech

    output.setText(outputText)
    response.setOutputSpeech(output)

    response
  }
}
