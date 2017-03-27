package org.repwatch.alexa

import java.util

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler

class RepwatchSpeechletRequestStreamHandler extends {
  val speechlet = new RepwatchSpeechlet
  val applicationIds = new util.HashSet[String]

} with SpeechletRequestStreamHandler(speechlet, applicationIds)
