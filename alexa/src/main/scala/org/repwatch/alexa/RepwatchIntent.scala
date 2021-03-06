package org.repwatch.alexa

import com.amazon.speech.speechlet.IntentRequest

object RepwatchIntent {
  object Intents {
    val FindSenators = "FindSenators"
    val FindRepresentative = "FindRepresentative"
    val SetZipCodeIntent = "SetZipCode"
  }
}
sealed trait RepwatchIntent {
  def intentRequest: IntentRequest
}

case class FindRepresentativeIntent(intentRequest: IntentRequest) extends RepwatchIntent

case class FindSenatorsIntent(intentRequest: IntentRequest) extends RepwatchIntent

case class SetZipCodeIntent(intentRequest: IntentRequest) extends RepwatchIntent

case class UnknownIntent(intentRequest: IntentRequest) extends RepwatchIntent
