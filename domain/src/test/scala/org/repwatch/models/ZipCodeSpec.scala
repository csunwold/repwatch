package org.repwatch.models

import org.scalatest.{FlatSpec, Matchers, OptionValues}

// scalastyle:off null
class ZipCodeSpec extends FlatSpec with Matchers with OptionValues {
  behavior of "apply"

  it should "return None if value is null" in {
    ZipCode(null) should be (None)
  }

  it should "return None if value is empty string" in {
    ZipCode("") should be (None)
  }

  it should "return None if value is only white space" in {
    ZipCode("    ") should be (None)
  }

  it should "return None if value is invalid zip code" in {
    ZipCode("3") should be (None)
  }

  it should "return Some(ZipCode) if value is valid zip code" in {
    ZipCode("20500").value.value should be ("20500")
  }
}
