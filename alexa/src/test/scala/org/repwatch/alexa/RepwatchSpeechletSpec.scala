package org.repwatch.alexa

import com.amazon.speech.ui.PlainTextOutputSpeech
import org.repwatch.StubLegislatorRepository
import org.repwatch.builders.TestObjectBuilders.{buildIntent, buildIntentRequest, buildSession}
import org.repwatch.repositories.{InMemoryUserRepository}
import org.scalatest.{AsyncFreeSpec, Matchers}

class RepwatchSpeechletSpec extends AsyncFreeSpec with Matchers {
  "RepwatchSpeechlet" - {
    "Given a FindSenatorIntent without a recognized user" - {
      "Should ask for a zip code" in {
        val speechlet = new RepwatchSpeechlet(new StubLegislatorRepository(), new InMemoryUserRepository)

        val intentRequest = buildIntentRequest(intent = buildIntent(RepwatchIntent.Intents.FindSenators))
        val session = buildSession(userId = "385452ca-afee-4c18-af2c-6589d54b29b8")
        val response = speechlet.onIntent(intentRequest, session)

        response.getOutputSpeech.getClass should be(classOf[PlainTextOutputSpeech])
        val outputSpeech = response.getOutputSpeech.asInstanceOf[PlainTextOutputSpeech]
        val text = outputSpeech.getText

        text should be("First, I need to know your zip code.")
      }
    }
  }
}
