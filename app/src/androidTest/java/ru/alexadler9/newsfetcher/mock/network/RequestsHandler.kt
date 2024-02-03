package ru.alexadler9.newsfetcher.mock.network

import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.io.InputStream

/**
 * Determines which requests should be mocked using test responses.
 */
class RequestsHandler {

    // List of requests that will be mocked
    // Key is the intercepted request path, and value is the map of pairs [query -> path to JSON file with the response]
    private val responsesMap = mapOf(
        "v2/top-headlines" to mapOf(
            "page=1" to "articles-p1.json",
            "page=2" to "articles-p2.json",
            "page=3" to "articles-p3.json"
        )
    )

    /**
     * Determines whether the request should be intercepted.
     */
    fun shouldIntercept(path: String): Boolean {
        for (interceptUrl in responsesMap.keys) {
            if (path.contains(interceptUrl)) {
                return true
            }
        }
        return false
    }

    fun proceed(request: Request, path: String, query: String?): Response {
        responsesMap.keys.forEach { pathKey ->
            if (path.contains(pathKey)) {
                val mockResponsePaths = responsesMap[pathKey]!!
                val q = query ?: ""
                mockResponsePaths.keys.forEach { queryKey ->
                    if (q.contains(queryKey)) {
                        val mockResponsePath: String = mockResponsePaths[queryKey]!!
                        return createResponseFromAssets(request, mockResponsePath)
                    }
                }
            }
        }
        return errorResponse(request, 500, "Incorrectly intercepted request")
    }

    /**
     * Creates a Response using a JSON file.
     */
    private fun createResponseFromAssets(
        request: Request,
        assetPath: String
    ): Response {
        return try {
            val stream: InputStream =
                InstrumentationRegistry.getInstrumentation().context.assets.open(assetPath)
            stream.use {
                successResponse(request, stream)
            }
        } catch (e: IOException) {
            errorResponse(request, 500, e.message ?: "IOException")
        }
    }
}