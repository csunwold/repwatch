package org.repwatch.models

object Name {
  def apply(fullName: String): Option[Name] = {
    Option(fullName)
      .map(_.split(' '))
      .flatMap(splitName => {
        if (splitName.size == 2) {
          Some(new Name(first = splitName(0), middle = None, last = splitName(1)))
        } else if (splitName.size == 3) {
          Some(new Name(first = splitName(0), middle = Some(splitName(1)), last = splitName(2)))
        } else {
          // We don't know how to parse a name of that size
          None
        }
      })
  }
}
class Name(val first: String, val middle: Option[String], val last: String) {
  override def toString: String = {
    val formattedMiddle = middle.map(m => s" $m ").getOrElse(" ")
    s"${first}${formattedMiddle}${last}"
  }
}
