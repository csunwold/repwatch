package org.repwatch.repositories

import org.repwatch.models.User

import scala.concurrent.Future

trait UserRepository {
  def findUser(id: String): Future[Option[User]]

  def save(user: User): Future[User]
}
