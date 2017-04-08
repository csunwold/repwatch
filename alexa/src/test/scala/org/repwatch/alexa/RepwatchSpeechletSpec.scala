package org.repwatch.alexa

import com.amazon.speech.ui.PlainTextOutputSpeech
import org.repwatch.StubLegislatorRepository
import org.repwatch.builders.TestObjectBuilders._
import org.repwatch.models.User
import org.repwatch.repositories.{InMemoryUserRepository, UserRepository}
import org.scalatest.{AsyncFreeSpec, Matchers}

import scala.concurrent.Future

class RepwatchSpeechletSpec extends AsyncFreeSpec with Matchers {
  "FindSenatorIntent Handling" - {
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

  "SetZipCodeIntent Handling" - {
    "Given a SetZipCodeIntent with an invalid zip code" - {
      "Should reprompt for valid zip code" in {
        val speechlet = new RepwatchSpeechlet(new StubLegislatorRepository(), new InMemoryUserRepository)
        val intentRequest = buildIntentRequest(
          intent = buildIntent(
            name = RepwatchIntent.Intents.SetZipCodeIntent,
            slots = Map(("ZipCode", buildSlot("ZipCode", "")))))
        val session = buildSession(userId = "385452ca-afee-4c18-af2c-6589d54b29b8")
        val response = speechlet.onIntent(intentRequest, session)

        response.getOutputSpeech.getClass should be(classOf[PlainTextOutputSpeech])
        val outputSpeech = response.getReprompt.getOutputSpeech.asInstanceOf[PlainTextOutputSpeech]
        val text = outputSpeech.getText

        text should be("That zip code is not valid. Please say your zip code again. For example, <say-as interpret-as=\"digits\">20500</say-as>")
      }
    }

    "When an error happens saving a valid zip code" - {
      "Should say to try again later" in {
        val speechlet = new RepwatchSpeechlet(new StubLegislatorRepository(), new UserRepository {
          override def findUser(id: String): Future[Option[User]] = {
            Future.successful(None)
          }

          override def save(user: User): Future[User] = {
            Future.failed(new Exception("Error when saving"))
          }
        })
        val intentRequest = buildIntentRequest(
          intent = buildIntent(
            name = RepwatchIntent.Intents.SetZipCodeIntent,
            slots = Map(("ZipCode", buildSlot("ZipCode", "20500")))))
        val session = buildSession(userId = "385452ca-afee-4c18-af2c-6589d54b29b8")
        val response = speechlet.onIntent(intentRequest, session)

        response.getOutputSpeech.getClass should be(classOf[PlainTextOutputSpeech])
        val outputSpeech = response.getOutputSpeech.asInstanceOf[PlainTextOutputSpeech]
        val text = outputSpeech.getText

        text should be("Something went wrong saving your zip code. Please try again later.")
      }
    }
  }
}
