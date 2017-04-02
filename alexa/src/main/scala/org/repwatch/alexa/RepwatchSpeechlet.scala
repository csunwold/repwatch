package org.repwatch.alexa

import com.amazon.speech.speechlet._
import com.amazon.speech.ui.{PlainTextOutputSpeech, Reprompt}
import org.repwatch.alexa.handlers.{FindRepresentativeIntentHandler, FindSenatorsIntentHandler, UnknownIntentHandler}
import org.repwatch.models
import org.repwatch.repositories.{LegislatorRepository, UserRepository}

import scala.concurrent.Await
import scala.concurrent.duration._

// TODO - Unit Tests
class RepwatchSpeechlet(legislatorRepository: LegislatorRepository, userRepository: UserRepository) extends Speechlet {
  override def onSessionEnded(sessionEndedRequest: SessionEndedRequest, session: Session): Unit = ???

  override def onSessionStarted(sessionStartedRequest: SessionStartedRequest, session: Session): Unit = ???

  override def onIntent(intentRequest: IntentRequest, session: Session): SpeechletResponse = {
    findUser(session.getUser) match {
      case Some(user) => handleAuthedIntent(intentRequest, user)
      case None => handleUnAuthedIntent
    }
  }

  private def handleUnAuthedIntent = {
    val output = new PlainTextOutputSpeech
    output.setText("First, I need to know your zip code.")

    val repromptSpeech = new PlainTextOutputSpeech
    repromptSpeech.setText("You can say, my zip code is ")
    // TODO - Give a zip code response
    val reprompt = new Reprompt
    reprompt.setOutputSpeech(repromptSpeech)

    SpeechletResponse.newAskResponse(output, reprompt)
  }

  private def handleAuthedIntent(intentRequest: IntentRequest, user: models.User) = {
    intentRequest.getIntent.getName match {
      case RepwatchIntent.Intents.FindSenators =>
        FindSenatorsIntentHandler.handle(new FindSenatorsIntent(intentRequest), legislatorRepository, user)
      case RepwatchIntent.Intents.FindRepresentative =>
        FindRepresentativeIntentHandler.handle(new FindRepresentativeIntent(intentRequest), legislatorRepository, user)
      case _ => UnknownIntentHandler.handle(new UnknownIntent(intentRequest))
    }
  }

  override def onLaunch(launchRequest: LaunchRequest, session: Session): SpeechletResponse = ???

  private def findUser(user: User): Option[org.repwatch.models.User] = {
    val futureUser = userRepository.findUser(user.getUserId)
    val maybeUser = Await.result(futureUser, 2 seconds)

    maybeUser
  }
}
