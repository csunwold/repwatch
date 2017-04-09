package org.repwatch.repositories
import com.amazon.speech.speechlet.Session
import org.repwatch.models.{User, UserId, ZipCode}

import scala.concurrent.{Future}

class SessionAwareUserRepository(baseRepository: UserRepository, session: Session) extends UserRepository {
  override def findUser(id: String): Future[Option[User]] = {
    Option(session.getAttribute("ZipCode"))
      .map(z => z.asInstanceOf[String])
      .filter(_.trim.isEmpty)
      .flatMap(ZipCode(_))
      .map(z => new User(id = new UserId(id), zipCode = z))
      .map(Some(_))
      .map(Future.successful(_))
      .getOrElse({
        baseRepository.findUser(id)
      })
  }

  override def save(user: User): Future[User] = {
    session.setAttribute("ZipCode", user.zipCode.value)
    baseRepository.save(user)
  }
}
