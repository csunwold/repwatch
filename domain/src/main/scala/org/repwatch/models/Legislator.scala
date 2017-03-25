package org.repwatch.models

sealed trait Legislator {
  def name: Name
  def contactInformation: ContactInformation
  def district: District

  override def toString: String = {
    name.toString
  }
}

case class Senator(contactInformation: ContactInformation,
                   district: District,
                   name: Name) extends Legislator

case class Representative(contactInformation: ContactInformation,
                          district: District,
                          name: Name) extends Legislator
