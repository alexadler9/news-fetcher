package ru.alexadler9.newsfetcher.mock

import android.os.SystemClock
import okhttp3.Interceptor
import okhttp3.Response

const val RESPONSE_DELAY = 500L

/**
 * Intercepts and processes requests.
 */
class MockingInterceptor(private val requestHandler: RequestsHandler) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url().encodedPath()
        val query = request.url().encodedQuery()
        if (requestHandler.shouldIntercept(path)) {
            // Get a mock response
            val response = requestHandler.proceed(request, path, query)
            SystemClock.sleep(RESPONSE_DELAY)
            return response
        }
        return chain.proceed(request)
    }
}