package org.repwatch.config

import com.typesafe.config.ConfigFactory

class ApplicationConfig {
  private val config = ConfigFactory.load("repwatch.conf")

  val googleApiKey: String = config.getString("google.api-key")

  val propublicaApiKey: String = config.getString("propublica.api-key")
}
