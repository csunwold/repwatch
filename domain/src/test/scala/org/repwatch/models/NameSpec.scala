package org.repwatch.models

import org.scalatest.{FlatSpec, Matchers, OptionValues}

class NameSpec extends FlatSpec with Matchers with OptionValues {
  behavior of "apply"

  it should "return None when first is null" in {
    Name(null, "last") should be (None)
  }

  it should "return None when first is ''" in {
    Name("", "last") should be (None)
  }

  it should "return None when last is null" in {
    Name("first", null) should be (None)
  }

  it should "return None when last is ''" in {
    Name("first", "") should be (None)
  }

  it should "return Some(Name) when first and last are valid" in {
    val name = Name("first", "last")

    name.value.first should be ("first")
    name.value.last should be ("last")
  }
}
