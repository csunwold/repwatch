package org.repwatch.alexa

import com.amazon.speech.speechlet._
import com.amazon.speech.ui.{PlainTextOutputSpeech, Reprompt}
import org.repwatch.api._
import org.repwatch.models.{Representative, Senator, UserId}
import org.repwatch.repositories.{LegislatorRepository, SessionAwareUserRepository, UserRepository}

class RepwatchSpeechlet(legislatorRepository: LegislatorRepository, userRepository: UserRepository) extends Speechlet {

  override def onSessionEnded(sessionEndedRequest: SessionEndedRequest, session: Session): Unit = {
  }

  override def onSessionStarted(sessionStartedRequest: SessionStartedRequest, session: Session): Unit = {
  }

  override def onIntent(intentRequest: IntentRequest, session: Session): SpeechletResponse = {
    val repwatch = new Repwatch(legislatorRepository, new SessionAwareUserRepository(userRepository, session))

    val intentReceived = new IntentReceived(
      userId = new UserId(session.getUser.getUserId),
      name = intentRequest.getIntent.getName
    )

    repwatch.onEvent(intentReceived) match {
      case UserIsNotRecognized(intentName) => askForZipCode(intentName, session)
      case UserCouldNotBeSaved() => failedSavingZipCodeResponse
      case InvalidZipCode() => invalidZipCodeResponse
      case SenatorsFound(senators) => saySenators(senators)
      case SenatorsNotFound() => senatorsNotFound
      case RepresentativeFound(representative) => sayRepresentative(representative)
      case RepresentativeNotFound() => representativeNotFoundResponse
      case EndSession() => endSession
      case ErrorOccurred(throwable) => errorOccurred(throwable)
    }
  }

  private def mapToIntent(intentRequest: IntentRequest, session: Session) = {

  }

  override def onLaunch(launchRequest: LaunchRequest, session: Session): SpeechletResponse = {
    val response = new SpeechletResponse
    val output = new PlainTextOutputSpeech

    output.setText("Hello. You can ask repwatch who your senators are or who your representative is.")
    response.setOutputSpeech(output)

    response
  }

  private def errorOccurred(throwable: Throwable) = {
    val output = new PlainTextOutputSpeech
    output.setText("Something did not work right. Please try again later.")

    SpeechletResponse.newTellResponse(output)
  }

  private def endSession = {
    val output = new PlainTextOutputSpeech
    output.setText("Thank you.")

    SpeechletResponse.newTellResponse(output)
  }

  private def askForZipCode(originalIntentName: String, session: Session) = {
    val output = new PlainTextOutputSpeech
    session.setAttribute("LastIntentName", originalIntentName)

    output.setText("First, I need to know your zip code.")

    val repromptSpeech = new PlainTextOutputSpeech
    repromptSpeech.setText("You can say, my zip code is <say-as interpret-as=\"digits\">20500</say-as>")
    val reprompt = new Reprompt
    reprompt.setOutputSpeech(repromptSpeech)

    SpeechletResponse.newAskResponse(output, reprompt)
  }


  def representativeNotFoundResponse = {
    val output = new PlainTextOutputSpeech
    output.setText("I wasn't able to find a representative for that zip code.")

    SpeechletResponse.newTellResponse(output)
  }

  def sayRepresentative(representative: Representative) = {
    val output = new PlainTextOutputSpeech
    val outputText = s"Your representative in congress is ${representative.toString}"
    output.setText(outputText)

    SpeechletResponse.newTellResponse(output)
  }

  def saySenators(senators: Array[Senator]) = {
    val output = new PlainTextOutputSpeech
    val outputText = s"Your senators are ${senators(0).toString} and ${senators(1).toString}"
    output.setText(outputText)

    SpeechletResponse.newTellResponse(output)
  }

  def senatorsNotFound = {
    val output = new PlainTextOutputSpeech
    output.setText("I wasn't able to find senators for that zip code.")

    SpeechletResponse.newTellResponse(output)
  }

  private def failedSavingZipCodeResponse = {
    val outputSpeech = new PlainTextOutputSpeech
    outputSpeech.setText("Something went wrong saving your zip code. Please try again later.")

    val response = new SpeechletResponse()
    response.setShouldEndSession(true)
    response.setOutputSpeech(outputSpeech)

    response
  }

  private def invalidZipCodeResponse = {
    val output = new PlainTextOutputSpeech
    val repromptSpeech = new PlainTextOutputSpeech
    repromptSpeech.setText("That zip code is not valid. Please say your zip code again. For example, <say-as interpret-as=\"digits\">20500</say-as>")
    val reprompt = new Reprompt
    reprompt.setOutputSpeech(repromptSpeech)

    SpeechletResponse.newAskResponse(output, reprompt)
  }
}
