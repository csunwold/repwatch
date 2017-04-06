package org.repwatch.models

object ZipCode {
  def apply(value: String) : Option[ZipCode] = {
    if (value != null && value.trim != "") {
      Some(new ZipCode(value))
    } else {
      None
    }
  }
}
// TODO - Validation and tests?
class ZipCode(val value: String) extends AnyVal