package org.repwatch.api

import org.repwatch.models.{Representative, Senator, User, UserId}
import org.repwatch.repositories.{LegislatorRepository, UserRepository}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success, Try}

sealed trait State

case class UserIsNotRecognized(intentName: String) extends State
case class UserCouldNotBeSaved() extends State
case class InvalidZipCode() extends State
case class SenatorsFound(senators: Array[Senator]) extends State
case class SenatorsNotFound() extends State
case class RepresentativeFound(representative: Representative) extends State
case class RepresentativeNotFound() extends State
case class EndSession() extends State
case class ErrorOccurred(error: Throwable) extends State

sealed trait Event {
  def userId: UserId
}

case class IntentReceived(userId: UserId, name: String) extends Event

sealed trait Intent extends Event
case class FindSenators(userId: UserId, user: User) extends Intent
case class FindRepresentative(userId: UserId, user: User) extends Intent
case class SaveUser(userId: UserId, user: User, originalIntent: Option[Intent]) extends Intent
case class UnrecognizedIntent(userId: UserId) extends Intent

class Repwatch(legislatorRepository: LegislatorRepository, userRepository: UserRepository) {

  def onEvent(event: Event): State = {
    event match {
      case IntentReceived(userId, name) => {
        findUser(userId)
          .map(user => getIntent(name, user))
          .map(intent => onEvent(intent))
          .getOrElse(UserIsNotRecognized(name))
      }
      case FindSenators(_, user) => findSenators(user)
      case FindRepresentative(_, user) => findRepresentative(user)
      case SaveUser(_, user, originalIntent) => {
        saveUser(user) match {
          case Success(_) => originalIntent.map(onEvent).getOrElse(EndSession())
          case Failure(throwable) => ErrorOccurred(throwable)
        }
      }
      case UnrecognizedIntent(_) => ErrorOccurred(new Exception("Unrecognized Intent"))
    }
  }

  def findUser(userId: UserId): Option[User] = {
    val userFuture = userRepository.findUser(userId.value)

    Await.result(userFuture, 2.seconds)
  }

  def findSenators(user: User) = {
    val senatorsFuture = legislatorRepository.locateSenators(user.zipCode)
    val senators = Await.result(senatorsFuture, 5.seconds)

    if (senators.length == 0) {
      SenatorsNotFound()
    } else {
      SenatorsFound(senators)
    }
  }

  def findRepresentative(user: User) = {
    val representativeFuture = legislatorRepository.locateRepresentative(user.zipCode)
    val maybeRepresentative = Await.result(representativeFuture, 5.seconds)

    maybeRepresentative match {
      case Some(representative) => RepresentativeFound(representative)
      case None => RepresentativeNotFound()
    }
  }

  def saveUser(user: User) = Try {

  }

  def getIntent(name: String, user: User): Intent = {
    // is user recognized?
    name match {
      case "FindSenators" => FindSenators(user.id, user)
      case "FindRepresentative" => FindRepresentative(user.id, user)
      case "SetZipCode" => SaveUser(user.id, user, None)
      case _ => UnrecognizedIntent(user.id)
    }
  }
}