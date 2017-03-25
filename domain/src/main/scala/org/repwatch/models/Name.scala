package org.repwatch.models

object Name {
  def apply(first: String, last: String): Option[Name] = {
    if (first == null || first == "" || last == null || last == "") {
      None
    } else {
      Some(new Name(first, last))
    }
  }
}
class Name(val first: String, val last: String) {
  override def toString: String = {
    first + " " + last
  }
}
