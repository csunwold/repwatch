package org.repwatch.api

import org.repwatch.models.{Senator, User, UserId}

sealed trait State

case class UserIsNotRecognized() extends State
case class UserIsRecognized() extends State
case class UserCouldNotBeSaved() extends State
case class InvalidZipCode() extends State
case class SenatorsFound(senators: Array[Senator]) extends State
case class SenatorsNotFound() extends State
case class RepresentativeFound() extends State
case class RepresentativeNotFound() extends State
case class EndSession() extends State
case class ErrorOccurred() extends State

sealed trait Event {
  def userId: UserId
}

case class IntentReceived(userId: UserId, name: String) extends Event
case class UserSaved(userId: UserId, intent: Option[Intent]) extends Event

sealed trait Intent extends Event
case class FindSenators(userId: UserId, user: User) extends Intent
case class FindRepresentative(userId: UserId, user: User) extends Intent
case class SaveUser(userId: UserId, user: User) extends Intent
case class UnrecognizedIntent(userId: UserId) extends Intent

object Repwatch {
  def onEvent(event: Event): State = {
    event match {
      case IntentReceived(userId, name) => {
        findUser(userId)
          .map(user => getIntent(name, user))
          .map(intent => onEvent(intent))
          .getOrElse(new UserIsNotRecognized)
      }
      case UserSaved(_, intent) => intent.map(onEvent).getOrElse(new EndSession)
      case FindSenators(_, user) => findSenators(user)
      case FindRepresentative(_, user) => findRepresentative(user)
      case SaveUser(_, user) =>
      case UnrecognizedIntent(_) => new ErrorOccurred
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

  def saveUser(user: User) = {

  }

  def getIntent(name: String, user: User): Intent = {
    // is user recognized?
    name match {
      case "FindSenators" => new FindSenators(user.id, user)
      case "FindRepresentative" => new FindRepresentative(user.id, user)
      case "SetZipCode" => new SaveUser(user.id, user)
      case _ => new UnrecognizedIntent(user.id)
    }
  }
}