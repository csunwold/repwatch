package org.repwatch.models

object ZipCode {
  def apply(value: String) : Option[ZipCode] = {
    if (value != null && value.trim != "" && value.trim.matches("^\\d{5}(?:[-\\s]\\d{4})?$")) {
      Some(new ZipCode(value))
    } else {
      None
    }
  }
}

class ZipCode(val value: String) extends AnyVal