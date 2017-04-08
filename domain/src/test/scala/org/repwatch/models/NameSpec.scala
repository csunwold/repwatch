package org.repwatch.models

import org.scalatest.{FlatSpec, Matchers, OptionValues}

// scalastyle:off null
class NameSpec extends FlatSpec with Matchers with OptionValues {
  behavior of "apply"

  it should "return None when fullName is null" in {
    val name = Name(null)

    name should be (None)
  }

  it should "return None when fullName is empty" in {
    val name = Name("")

    name should be (None)
  }

  it should "return Some(Name) when given full name" in {
    val name = Name("first last")

    name.value.first should be ("first")
    name.value.last should be ("last")
  }

  it should "return Some(Name) when given full name containing middle name" in {
    val name = Name("first middle last")

    name.value.first should be ("first")
    name.value.middle.value should be ("middle")
    name.value.last should be ("last")
  }

  behavior of "toString"

  it should "return 'First Name' when first = 'First' and last = 'Name'" in {
    val name = new Name(first = "First", middle = None, last = "Name")

    name.toString should be ("First Name")
  }

  it should "return 'First Middle Last' when first = 'First', middle = 'Middle' and last = 'Last'" in {
    val name = new Name(first = "First", middle = Some("Middle"), last = "Last")

    name.toString should be ("First Middle Last")
  }
}
