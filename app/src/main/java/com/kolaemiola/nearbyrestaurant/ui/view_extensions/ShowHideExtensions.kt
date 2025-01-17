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
package com.kolaemiola.nearbyrestaurant.ui.view_extensions

import android.content.Context
import android.view.View
import android.widget.Toast

internal fun View.show() {
  this.visibility = View.VISIBLE
}
internal fun View.gone() {
  this.visibility = View.GONE
}
internal fun View.hide() {
  this.visibility = View.INVISIBLE
}
internal fun Context.showToast(message: String) {
  Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
