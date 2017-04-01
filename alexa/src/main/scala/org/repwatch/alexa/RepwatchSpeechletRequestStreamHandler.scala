package org.repwatch.alexa

import java.util

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler
import org.repwatch.config.ApplicationConfig
import org.repwatch.repositories.InMemoryUserRepository

class RepwatchSpeechletRequestStreamHandler extends {

  val userRepository = new InMemoryUserRepository

  val speechlet = new RepwatchSpeechlet(new ApplicationConfig, userRepository)
  val applicationIds = new util.HashSet[String]

} with SpeechletRequestStreamHandler(speechlet, applicationIds)
