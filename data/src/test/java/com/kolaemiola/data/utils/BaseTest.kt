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

import com.kolaemiola.data.api.FourSquareAPI
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

internal open class BaseTest {

  private lateinit var mockWebServer: MockWebServer
  lateinit var fourSquareAPI: FourSquareAPI
  private lateinit var okHttpClient: OkHttpClient

  private lateinit var loggingInterceptor: HttpLoggingInterceptor

  @Before
  open fun setup() {
    mockWebServer = MockWebServer()
    mockWebServer.dispatcher = RequestDispatcher()
    mockWebServer.start()
    loggingInterceptor = HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.BODY
    }
    okHttpClient = buildOkHttpClient(loggingInterceptor)
    val moshi = Moshi.Builder()
      .add(KotlinJsonAdapterFactory()).build()
    fourSquareAPI = Retrofit.Builder()
      .baseUrl(mockWebServer.url("/"))
      .client(okHttpClient)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()
      .create(FourSquareAPI::class.java)
  }

  @After
  fun stopService() {
    mockWebServer.shutdown()
  }
  private fun buildOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor(httpLoggingInterceptor)
      .connectTimeout(60, TimeUnit.SECONDS)
      .readTimeout(60, TimeUnit.SECONDS)
      .build()
  }
}
