package com.kolaemiola.domain

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

internal class NoParamsException(errorMessage: String = noParamMessage) :
  IllegalArgumentException(errorMessage)

const val noParamMessage = "Your params cannot be null for this use case"

@OptIn(ExperimentalContracts::class)
fun <T : Any> requireParams(value: T?): T {
  contract {
    returns() implies (value != null)
  }

  if (value == null) {
    throw NoParamsException()
  } else {
    return value
  }
}