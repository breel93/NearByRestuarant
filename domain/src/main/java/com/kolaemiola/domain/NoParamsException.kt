/**
 *  Designed and developed by Kola Emiola
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
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
