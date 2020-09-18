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
package com.kolaemiola.data.utils

import com.kolaemiola.data.BuildConfig.FOURSQUARE_API_KEY
import com.kolaemiola.data.BuildConfig.FOURSQUARE_SECRET
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import okhttp3.mockwebserver.SocketPolicy
import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

internal class RequestDispatcher : Dispatcher() {

  override fun dispatch(request: RecordedRequest): MockResponse {
    return when (request.path) {
      "$QUERY_PATH/search?$SEARCH_QUERY" -> {
        createSuccessResponseFromFile(file = "foursquare_response.json")
      }
      else -> throw IllegalArgumentException("Unknown Request Path ${request.path}")
    }
  }

  val errorDispatcher = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
      return createErrorResponseFromFile(file = "error.json")
    }
  }

  private fun createSuccessResponseFromFile(file: String): MockResponse {
    return createResponseFromFile(file = file, response = MockResponse())
  }
  private fun createResponseFromFile(file: String, response: MockResponse): MockResponse {
    val inputStream = javaClass.classLoader
      .getResourceAsStream("api-response/$file")
    val source = inputStream.source().buffer()
    response.setBody(source.readString(StandardCharsets.UTF_8))
    return response
  }

  private fun createTimeOut(request: RecordedRequest): MockResponse {
    return MockResponse()
      .setSocketPolicy(SocketPolicy.NO_RESPONSE)
      .throttleBody(BYTES_PER_PERIOD, PERIOD, TimeUnit.SECONDS)
  }
  private fun createErrorResponseFromFile(file: String): MockResponse {
    val mockResponse = MockResponse()
    mockResponse.setResponseCode(400)
    return createResponseFromFile(file = file, response = mockResponse)
  }

  companion object {
    const val QUERY_PATH: String = "/v2/venues"
    const val SEARCH_QUERY = "client_id=$FOURSQUARE_API_KEY&client_secret=$FOURSQUARE_SECRET&categoryId=4d4b7105d754a06374d81259&ll=40.7484%2C-73.9857&near=new%2Byork&radius=250&limit=10&v=20180323"
    private const val BYTES_PER_PERIOD = 1024L
    private const val PERIOD = 2L
  }
}
