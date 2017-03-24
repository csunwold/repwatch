package org.repwatch.models

sealed trait Legislator {
  def name: Name
  def contactInformation: ContactInformation

  override def toString: String = {
    name.toString
  }
}

case class Senator(name: Name, contactInformation: ContactInformation) extends Legislator

case class Representative(name: Name, contactInformation: ContactInformation) extends Legislator
