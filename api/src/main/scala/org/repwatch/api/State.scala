package org.repwatch.api

import org.repwatch.models.{Senator, User, UserId}

import scala.util.{Failure, Success, Try}

sealed trait State

case class UserIsNotRecognized(intentName: String) extends State
case class UserIsRecognized() extends State
case class UserCouldNotBeSaved() extends State
case class InvalidZipCode() extends State
case class SenatorsFound(senators: Array[Senator]) extends State
case class SenatorsNotFound() extends State
case class RepresentativeFound() extends State
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

object Repwatch {
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
    // TODO
    None
  }

  def findSenators(user: User) = {
    // TODO - Handle this intent
    new SenatorsNotFound
  }

  def findRepresentative(user: User) = {
    // TODO
    new RepresentativeNotFound
  }

  def saveUser(user: User) = Try {
    
  }

  def getIntent(name: String, user: User): Intent = {
    // is user recognized?
    name match {
      case "FindSenators" => new FindSenators(user.id, user)
      case "FindRepresentative" => new FindRepresentative(user.id, user)
      case "SetZipCode" => new SaveUser(user.id, user, None)
      case _ => new UnrecognizedIntent(user.id)
    }
  }
}