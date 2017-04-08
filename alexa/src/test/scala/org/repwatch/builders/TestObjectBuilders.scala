package org.repwatch.builders

import java.util.{Date, Locale}

import com.amazon.speech.slu.{Intent, Slot}
import com.amazon.speech.speechlet.{IntentRequest, Session, User}

import scala.collection.JavaConverters._

object TestObjectBuilders {
  def buildIntent(name: String,
                  slots: Map[String, Slot] = Map.empty): Intent = {
    Intent.builder()
      .withName(name)
      .withSlots(slots.asJava)
      .build()
  }

  def buildIntentRequest(intent: Intent = buildIntent("TEST"),
                         locale: Locale = Locale.US,
                         requestId: String = "37786d3f-683b-48ce-aba7-794ac2995cbf",
                         timestamp: Date = new Date()
                         ): IntentRequest = {
    IntentRequest.builder()
      .withIntent(intent)
      .withLocale(locale)
      .withRequestId(requestId)
      .withTimestamp(timestamp)
      .build()
  }

  def buildSession(userId: String = "385452ca-afee-4c18-af2c-6589d54b29b8"): Session = {
    Session.builder()
      .withSessionId("59a3b1c4-f8b3-4469-ac1c-6dbf3d059bce")
      .withUser(buildUser(userId))
      .build()
  }

  def buildSlot(name: String, value: String): Slot = {
    Slot
      .builder()
      .withName(name)
      .withValue(value)
      .build()
  }

  def buildUser(id: String = "385452ca-afee-4c18-af2c-6589d54b29b8"): User = {
    User
      .builder()
      .withUserId(id)
      .build()
  }
}
