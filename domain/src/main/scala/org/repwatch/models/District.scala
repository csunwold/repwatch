package org.repwatch.models

class District(val juniorSenator: Senator,
               val number: Int,
               val representative: Representative,
               val seniorSenator: Senator,
               val state: State) {
  val senators = Set(juniorSenator, seniorSenator)
}
