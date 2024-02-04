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
    // Key is the intercepted request path, and value is the map of pairs [query-pattern -> path-to-JSON-file with the response]
    private val responsesMap = mapOf(
        "v2/top-headlines" to mapOf(
            ".*q=query.*page=1".toRegex() to "articles-query.json",
            ".*q=query.*page=2".toRegex() to "articles-empty.json",
            ".*q=&.*page=1".toRegex() to "articles-p1.json",
            ".*q=&.*page=2".toRegex() to "articles-p2.json",
            ".*q=&.*page=3".toRegex() to "articles-empty.json"
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

    fun proceed(request: Request, path: String, q: String?): Response {
        val query: String = q ?: ""
        if (query.contains("bad-request", ignoreCase = true)) {
            // Testing agreement: code 400 will be issued for the "bad-request" query.
            return errorResponse(request, 400, "Error for path $path")
        }
        responsesMap.keys.forEach { pathKey ->
            if (path.contains(pathKey)) {
                val mockResponsePaths = responsesMap[pathKey]!!
                mockResponsePaths.keys.forEach { pattern ->
                    if (pattern.matches(query)) {
                        val mockResponsePath: String = mockResponsePaths[pattern]!!
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