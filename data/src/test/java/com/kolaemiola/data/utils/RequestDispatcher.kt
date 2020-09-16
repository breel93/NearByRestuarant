package com.kolaemiola.data.utils

import com.kolaemiola.data.BuildConfig.FOURSQUARE_API_KEY
import com.kolaemiola.data.BuildConfig.FOURSQUARE_SECRET
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.net.HttpURLConnection


internal class RequestDispatcher : Dispatcher() {

  override fun dispatch(request: RecordedRequest): MockResponse {
    return when (request.path) {
      "$QUERY_PATH/search?$SEARCH_QUERY" -> {
        MockResponse()
          .setResponseCode(HttpURLConnection.HTTP_OK)
          .setBody(getJson("api-response/foursquare_response.json"))
      }
      else -> throw IllegalArgumentException("Unknown Request Path ${request.path.toString()}")
    }
  }

  companion object {
    const val QUERY_PATH: String = "/v2/venues"
    const val SEARCH_QUERY = "client_id=${FOURSQUARE_API_KEY}&client_secret${FOURSQUARE_SECRET}&categoryId=4d4b7105d754a06374d81259&near=new+york&radius=250&limit=10&v=20180323"

  }

}