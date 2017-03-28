package org.repwatch.alexa.handlers

import com.amazon.speech.speechlet.SpeechletResponse
import org.repwatch.alexa.UnknownIntent

object UnknownIntentHandler {
  def handle(unknownIntent: UnknownIntent): SpeechletResponse = {
    new SpeechletResponse
  }
}
