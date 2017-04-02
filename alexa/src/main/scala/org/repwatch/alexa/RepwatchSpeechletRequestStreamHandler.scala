package org.repwatch.alexa

import java.util

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler
import org.repwatch.config.ApplicationConfig
import org.repwatch.providers.google.{ApiKey, GoogleCivicApi}
import org.repwatch.repositories.{GoogleLegislatorRepository, InMemoryUserRepository}

class RepwatchSpeechletRequestStreamHandler extends {

  val userRepository = new InMemoryUserRepository
  val config = new ApplicationConfig
  val googleApiKey = new ApiKey(config.googleApiKey)
  val googleClient = new GoogleCivicApi(googleApiKey)

  val speechlet = new RepwatchSpeechlet(new GoogleLegislatorRepository(googleClient), userRepository)
  val applicationIds = new util.HashSet[String]

} with SpeechletRequestStreamHandler(speechlet, applicationIds)
