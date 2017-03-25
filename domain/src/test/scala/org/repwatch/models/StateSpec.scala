package org.repwatch.models

import org.scalatest.{FlatSpec, Matchers, OptionValues}

class StateSpec extends FlatSpec with Matchers with OptionValues {
  behavior of "apply"

  it should "return None if code is null" in {
    State(null) should be (None)
  }

  it should "return None if code is not a state" in {
    State("VC") should be (None)
  }

  it should "return Some(state) when code is 'WA'" in {
    val state = State("WA")

    state.value.code should be ("WA")
  }
}
