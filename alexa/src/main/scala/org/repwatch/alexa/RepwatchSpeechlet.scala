package org.repwatch.alexa

import com.amazon.speech.speechlet._

class RepwatchSpeechlet extends Speechlet {
  override def onSessionEnded(sessionEndedRequest: SessionEndedRequest, session: Session): Unit = ???

  override def onSessionStarted(sessionStartedRequest: SessionStartedRequest, session: Session): Unit = ???

  override def onIntent(intentRequest: IntentRequest, session: Session): SpeechletResponse = ???

  override def onLaunch(launchRequest: LaunchRequest, session: Session): SpeechletResponse = ???
}
