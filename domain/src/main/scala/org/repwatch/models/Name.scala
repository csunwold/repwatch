package org.repwatch.models

case class Name(first: String, last: String) {
  override def toString: String = {
    first + " " + last
  }
}
