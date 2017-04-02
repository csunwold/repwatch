package org.repwatch.alexa

import com.amazon.speech.ui.PlainTextOutputSpeech
import org.repwatch.builders.TestObjectBuilders.{buildIntent, buildIntentRequest, buildSession}
import org.repwatch.config.ApplicationConfig
import org.repwatch.models.{User, UserId, ZipCode}
import org.repwatch.providers.google.{ApiKey, GoogleCivicApi}
import org.repwatch.repositories.{GoogleLegislatorRepository, InMemoryUserRepository}
import org.scalatest.{AsyncFreeSpec, Matchers}

/**
  * These are complete integration tests that exercise the entire code path. You must have valid API keys created
  * and setup in the repwatch.conf file for these to run successfully.
  *
  * Because they are complete end to end tests the data will change from time to time as elections and other vacancies
  * happen. These tests should only be ran as needed.
  */
class RepwatchSpeechletSpec extends AsyncFreeSpec with Matchers {
  var config : ApplicationConfig = new ApplicationConfig
  val googleApiKey = new ApiKey(config.googleApiKey)
  val googleClient = new GoogleCivicApi(googleApiKey)
  val legislatorRepository = new GoogleLegislatorRepository(googleClient)

  "RepwatchSpeechlet" - {
    "Given a FindSenatorIntent without a recognized user" - {
      "Should ask for a zip code" in {
        val speechlet = new RepwatchSpeechlet(legislatorRepository, new InMemoryUserRepository)

        val intentRequest = buildIntentRequest(intent = buildIntent(RepwatchIntent.Intents.FindSenators))
        val session = buildSession(userId = "385452ca-afee-4c18-af2c-6589d54b29b8")
        val response = speechlet.onIntent(intentRequest, session)

        response.getOutputSpeech.getClass should be (classOf[PlainTextOutputSpeech])
        val outputSpeech = response.getOutputSpeech.asInstanceOf[PlainTextOutputSpeech]
        val text = outputSpeech.getText

        text should be ("First, I need to know your zip code.")
      }
    }

    "Given a FindSenatorIntent in Washington State" - {
      "Should return Senators Maria Cantwell and Patty Murray" in {
        val userRepository = new InMemoryUserRepository
        val user = User(id = UserId("79a75932-c1d1-4a81-9b90-ebd019561d48"), zipCode = ZipCode("98101"))
        userRepository
          .save(user)
          .map(_ => {
            val speechlet = new RepwatchSpeechlet(legislatorRepository, userRepository)

            val intentRequest = buildIntentRequest(intent = buildIntent(RepwatchIntent.Intents.FindSenators))
            val session = buildSession(userId = user.id.value)
            val response = speechlet.onIntent(intentRequest, session)

            response.getOutputSpeech.getClass should be (classOf[PlainTextOutputSpeech])
            val outputSpeech = response.getOutputSpeech.asInstanceOf[PlainTextOutputSpeech]
            val text = outputSpeech.getText

            text should be ("Your senators are Maria Cantwell and Patty Murray")
          })
      }
    }

    "Given a FindSenatorIntent in Oregon State" - {
      "Should return Senators Ron Wyden and Jeff Merkley" in {
        val userRepository = new InMemoryUserRepository
        val user = User(id = UserId("79a75932-c1d1-4a81-9b90-ebd019561d48"), zipCode = ZipCode("97080"))
        userRepository
          .save(user)
          .map(_ => {
            val speechlet = new RepwatchSpeechlet(legislatorRepository, userRepository)

            val intentRequest = buildIntentRequest(intent = buildIntent(RepwatchIntent.Intents.FindSenators))
            val session = buildSession(userId = user.id.value)
            val response = speechlet.onIntent(intentRequest, session)

            response.getOutputSpeech.getClass should be (classOf[PlainTextOutputSpeech])
            val outputSpeech = response.getOutputSpeech.asInstanceOf[PlainTextOutputSpeech]
            val text = outputSpeech.getText

            text should be ("Your senators are Jeff Merkley and Ron Wyden")
          })
      }
    }

    "Given a FindRepresentativeIntent in 99203" - {
      "Should return congresswoman Cathy McMorris Rodgers" in {
        val userRepository = new InMemoryUserRepository
        val user = User(id = UserId("79a75932-c1d1-4a81-9b90-ebd019561d48"), zipCode = ZipCode("99203"))
        userRepository
          .save(user)
          .map(_ => {
            val speechlet = new RepwatchSpeechlet(legislatorRepository, userRepository)

            val intentRequest = buildIntentRequest(intent = buildIntent(RepwatchIntent.Intents.FindRepresentative))
            val session = buildSession(userId = user.id.value)
            val response = speechlet.onIntent(intentRequest, session)

            response.getOutputSpeech.getClass should be (classOf[PlainTextOutputSpeech])
            val outputSpeech = response.getOutputSpeech.asInstanceOf[PlainTextOutputSpeech]
            val text = outputSpeech.getText

            text should be ("Your representative in congress is Cathy McMorris Rodgers")
          })
      }
    }
  }
}
