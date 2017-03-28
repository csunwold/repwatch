package org.repwatch.alexa

import com.amazon.speech.ui.PlainTextOutputSpeech
import org.repwatch.builders.TestObjectBuilders._
import org.repwatch.models.{User, UserId, ZipCode}
import org.repwatch.repositories.InMemoryUserRepository
import org.scalatest.{FreeSpec, Matchers}

class RepwatchSpeechletSpec extends FreeSpec with Matchers {
  "RepwatchSpeechlet" - {
    "Given a FindSenatorIntent without a recognized user" - {
      "Should ask for a zip code" in {
        val speechlet = new RepwatchSpeechlet(new InMemoryUserRepository)

        val intentRequest = buildIntentRequest(intent = buildIntent(RepwatchSpeechlet.FindSenators))
        val session = buildSession(userId = "385452ca-afee-4c18-af2c-6589d54b29b8")
        val response = speechlet.onIntent(intentRequest, session)

        response.getOutputSpeech.getClass should be (classOf[PlainTextOutputSpeech])
        val outputSpeech = response.getOutputSpeech.asInstanceOf[PlainTextOutputSpeech]
        val text = outputSpeech.getText

        text should be ("First, I need to know your zip code.")
      }
    }

    "Given a FindSenatorIntent in Washington State" - {
      "Should return Senators" in {
        val userRepository = new InMemoryUserRepository
        val user = User(id = UserId("79a75932-c1d1-4a81-9b90-ebd019561d48"), zipCode = ZipCode("98101"))
        userRepository.save(user)

        val speechlet = new RepwatchSpeechlet(userRepository)

        val intentRequest = buildIntentRequest(intent = buildIntent(RepwatchSpeechlet.FindSenators))
        val session = buildSession(userId = user.id.value)
        val response = speechlet.onIntent(intentRequest, session)

        response.getOutputSpeech.getClass should be (classOf[PlainTextOutputSpeech])
        val outputSpeech = response.getOutputSpeech.asInstanceOf[PlainTextOutputSpeech]
        val text = outputSpeech.getText

        text should be ("Your senators are Maria Cantwell and Patty Murray")
      }
    }
  }
}
