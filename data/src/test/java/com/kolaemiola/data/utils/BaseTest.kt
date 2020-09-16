package com.kolaemiola.data.utils

import com.kolaemiola.data.api.FourSquareAPI
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

internal open class BaseTest{

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
  fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
    val inputStream = javaClass.classLoader!!
      .getResourceAsStream("api-response/$fileName")
    val source = inputStream.source().buffer()

    val mockResponse = MockResponse()

    for ((key, value) in headers) {
      mockResponse.addHeader(key, value)
    }
    mockWebServer.enqueue(
      mockResponse
        .setBody(source.readString(Charsets.UTF_8))
    )
  }
  private fun buildOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor(httpLoggingInterceptor)
      .connectTimeout(60, TimeUnit.SECONDS)
      .readTimeout(60, TimeUnit.SECONDS)
      .build()
  }
}