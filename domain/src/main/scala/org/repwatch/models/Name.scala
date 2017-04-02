package org.repwatch.models

object Name {
  def apply(fullName: String): Option[Name] = {
    if (fullName == null) {
      None
    } else {
      val splitName = fullName.split(' ')

      if (splitName.size == 2) {
        Some(new Name(first = splitName(0), middle = None, last = splitName(1)))
      } else if (splitName.size == 3) {
        Some(new Name(first = splitName(0), middle = Some(splitName(1)), last = splitName(2)))
      } else {
        // We don't know how to parse a name of that size
        None
      }
    }
  }

  def apply(first: String, last: String): Option[Name] = {
    if (first == null || first == "" || last == null || last == "") {
      None
    } else {
      Some(new Name(first, None, last))
    }
  }
}
class Name(val first: String, val middle: Option[String], val last: String) {
  override def toString: String = {
    val formattedMiddle = middle.map(m => s" $m ").getOrElse(" ")
    s"${first}${formattedMiddle}${last}"
  }
}
