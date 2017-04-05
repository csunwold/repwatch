package org.repwatch.alexa.handlers

import com.amazon.speech.speechlet.SpeechletResponse
import com.amazon.speech.ui.{PlainTextOutputSpeech}
import org.repwatch.alexa.SetZipCodeIntent
import org.repwatch.models.{User, ZipCode}
import org.repwatch.repositories.UserRepository

object SetZipCodeIntentHandler {
  def handle(intent: SetZipCodeIntent, user: User, userRepository: UserRepository) : SpeechletResponse = {
    Option(intent.intentRequest.getIntent.getSlot("ZipCode"))
      .map(_.getValue)
      .foreach(zipCode => {
        val updatedUser = user.copy(zipCode = ZipCode(zipCode))
        userRepository.save(updatedUser)
      })
    val outputSpeech = new PlainTextOutputSpeech
    outputSpeech.setText("Thank you")

    SpeechletResponse.newTellResponse(outputSpeech)
  }
}
