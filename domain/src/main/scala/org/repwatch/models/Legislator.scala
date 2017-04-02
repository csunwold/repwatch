package org.repwatch.models

sealed trait Legislator {
  def name: Name
  def contactInformation: ContactInformation

  override def toString: String = {
    name.toString
  }
}

case class Senator(contactInformation: ContactInformation,
                   name: Name) extends Legislator

case class Representative(contactInformation: ContactInformation,
                          name: Name) extends Legislator
