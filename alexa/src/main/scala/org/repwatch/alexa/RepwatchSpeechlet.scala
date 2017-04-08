package org.repwatch.alexa

import com.amazon.speech.speechlet._
import com.amazon.speech.ui.{PlainTextOutputSpeech, Reprompt}
import org.repwatch.alexa.handlers.{FindRepresentativeIntentHandler, FindSenatorsIntentHandler, SetZipCodeIntentHandler, UnknownIntentHandler}
import org.repwatch.models
import org.repwatch.models.{User, UserId, ZipCode}
import org.repwatch.repositories.{LegislatorRepository, UserRepository}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class RepwatchSpeechlet(legislatorRepository: LegislatorRepository, userRepository: UserRepository) extends Speechlet {
  override def onSessionEnded(sessionEndedRequest: SessionEndedRequest, session: Session): Unit = {

  }

  override def onSessionStarted(sessionStartedRequest: SessionStartedRequest, session: Session): Unit = {

  }

  override def onIntent(intentRequest: IntentRequest, session: Session): SpeechletResponse = {
    findUser(session) match {
      case Some(user) => handleAuthedIntent(intentRequest, session, user)
      case None => handleUnAuthedIntent(intentRequest, session)
    }
  }

  override def onLaunch(launchRequest: LaunchRequest, session: Session): SpeechletResponse = {
    val response = new SpeechletResponse
    val output = new PlainTextOutputSpeech

    output.setText("Hello. You can ask repwatch who your senators are or who your representative is.")
    response.setOutputSpeech(output)

    response
  }

  private def askForZipCode = {
    val output = new PlainTextOutputSpeech
    output.setText("First, I need to know your zip code.")

    val repromptSpeech = new PlainTextOutputSpeech
    repromptSpeech.setText("You can say, my zip code is <say-as interpret-as=\"digits\">20500</say-as>")
    val reprompt = new Reprompt
    reprompt.setOutputSpeech(repromptSpeech)

    SpeechletResponse.newAskResponse(output, reprompt)
  }

  private def findUser(session: Session): Option[org.repwatch.models.User] = {
    val id = session.getUser.getUserId
    Option(session.getAttribute("ZipCode"))
      .map(z => z.asInstanceOf[String])
      .filter(_.trim.isEmpty)
      .flatMap(ZipCode(_))
      .map(z => new User(id = UserId(id), zipCode = z))
      .orElse({
        val futureUser = userRepository.findUser(id)
        Await.result(futureUser, 2.seconds)
      })
  }

  private def handleAuthedIntent(intentRequest: IntentRequest, session: Session, user: models.User) = {
    intentRequest.getIntent.getName match {
      case RepwatchIntent.Intents.FindSenators =>
        FindSenatorsIntentHandler.handle(new FindSenatorsIntent(intentRequest), legislatorRepository, user)
      case RepwatchIntent.Intents.FindRepresentative =>
        FindRepresentativeIntentHandler.handle(new FindRepresentativeIntent(intentRequest), legislatorRepository, user)
      case RepwatchIntent.Intents.SetZipCodeIntent =>
        SetZipCodeIntentHandler.handle(new SetZipCodeIntent(intentRequest), session, userRepository)
      case _ => UnknownIntentHandler.handle(new UnknownIntent(intentRequest))
    }
  }

  private def handleUnAuthedIntent(intentRequest: IntentRequest, session: Session) = {
    intentRequest.getIntent.getName match {
      case RepwatchIntent.Intents.SetZipCodeIntent =>
        SetZipCodeIntentHandler.handle(new SetZipCodeIntent(intentRequest), session, userRepository)
      case _ => {
        askForZipCode
      }
    }
  }
}
