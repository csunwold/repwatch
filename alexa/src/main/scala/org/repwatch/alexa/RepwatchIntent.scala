package org.repwatch.alexa

import com.amazon.speech.speechlet.IntentRequest

sealed trait RepwatchIntent {
  def intentRequest: IntentRequest
}

case class FindSenatorsIntent(intentRequest: IntentRequest) extends RepwatchIntent

case class UnknownIntent(intentRequest: IntentRequest) extends RepwatchIntent
