package org.repwatch.alexa.handlers

import com.amazon.speech.speechlet.SpeechletResponse
import com.amazon.speech.ui.PlainTextOutputSpeech
import org.repwatch.alexa.FindRepresentativeIntent
import org.repwatch.models.User
import org.repwatch.repositories.LegislatorRepository

import scala.concurrent.Await
import scala.concurrent.duration._

object FindRepresentativeIntentHandler {
  def handle(intent: FindRepresentativeIntent,
             legislatorRepository: LegislatorRepository,
             user: User) : SpeechletResponse = {
    val representativeFuture = legislatorRepository.locateRepresentative(user.zipCode)
    val maybeRepresentative = Await.result(representativeFuture, 5 seconds)
    val outputText = maybeRepresentative match {
      case Some(representative) => s"Your representative in congress is ${representative.toString}"
      case None => "I wasn't able to find a representative for that zip code."
    }

    val response = new SpeechletResponse
    val output = new PlainTextOutputSpeech

    output.setText(outputText)
    response.setOutputSpeech(output)

    response
  }
}
