package org.repwatch.alexa.handlers

import com.amazon.speech.speechlet.SpeechletResponse
import com.amazon.speech.ui.PlainTextOutputSpeech
import org.repwatch.alexa.FindSenatorsIntent

object FindSenatorsIntentHandler {
  def handle(intent: FindSenatorsIntent) : SpeechletResponse = {
    val response = new SpeechletResponse

    val output = new PlainTextOutputSpeech
    output.setText("Your senators are Maria Cantwell and Patty Murray")

    response.setOutputSpeech(output)

    response
  }
}
