package org.repwatch.repositories
import org.repwatch.models.User

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class InMemoryUserRepository extends UserRepository {
  private val users = new scala.collection.mutable.HashMap[String, User]

  override def findUser(id: String): Future[Option[User]] = Future {
    users.get(id)
  }

  override def save(user: User): Future[User] = Future {
    users.put(user.id.value, user)

    user
  }
}
