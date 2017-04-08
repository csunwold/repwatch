package org.repwatch.models

object ZipCode {
  def apply(value: String) : Option[ZipCode] = {
    Option(value)
      .map(_.trim)
      .filter(_.matches("^\\d{5}(?:[-\\s]\\d{4})?$"))
      .map(new ZipCode(_))
  }
}

class ZipCode(val value: String) extends AnyVal
