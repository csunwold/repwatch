package org.repwatch.alexa

import com.amazon.speech.speechlet._
import com.amazon.speech.ui.{PlainTextOutputSpeech, Reprompt}
import org.repwatch.alexa.handlers.{FindSenatorsIntentHandler, UnknownIntentHandler}
import org.repwatch.repositories.UserRepository

import scala.concurrent.Await
import scala.concurrent.duration._

object RepwatchSpeechlet {
  val FindSenators = "FindSenators"
}

class RepwatchSpeechlet(userRepository: UserRepository) extends Speechlet {
  override def onSessionEnded(sessionEndedRequest: SessionEndedRequest, session: Session): Unit = ???

  override def onSessionStarted(sessionStartedRequest: SessionStartedRequest, session: Session): Unit = ???

  override def onIntent(intentRequest: IntentRequest, session: Session): SpeechletResponse = {

    val user = session.getUser

    val response = new SpeechletResponse
    if (userIsKnown(user)) {
      intentRequest.getIntent.getName match {
        case RepwatchSpeechlet.FindSenators => FindSenatorsIntentHandler.handle(new FindSenatorsIntent(intentRequest))
        case _ => UnknownIntentHandler.handle(new UnknownIntent(intentRequest))
      }
    } else {
      val output = new PlainTextOutputSpeech
      output.setText("First, I need to know your zip code.")

      val repromptSpeech = new PlainTextOutputSpeech
      repromptSpeech.setText("You can say, my zip code is ") // TODO - Give a zip code response
      val reprompt = new Reprompt
      reprompt.setOutputSpeech(repromptSpeech)

      SpeechletResponse.newAskResponse(output, reprompt)
    }
  }

  override def onLaunch(launchRequest: LaunchRequest, session: Session): SpeechletResponse = ???

  private def userIsKnown(user: User): Boolean = {
    val futureUser = userRepository.findUser(user.getUserId)
    val maybeUser = Await.result(futureUser, 2 seconds)

    maybeUser.isDefined
  }
}
