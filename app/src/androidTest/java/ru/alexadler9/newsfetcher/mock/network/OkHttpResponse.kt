package ru.alexadler9.newsfetcher.mock.network

import okhttp3.*
import okio.Buffer
import java.io.InputStream

private val EMPTY_BODY = ByteArray(0)

private val APPLICATION_JSON: MediaType = MediaType.parse("application/json")!!

fun successResponse(request: Request, stream: InputStream): Response {
    val buffer: Buffer = Buffer().readFrom(stream)
    return Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_1_1)
        .code(200)
        .message("OK")
        .body(ResponseBody.create(APPLICATION_JSON, buffer.size(), buffer))
        .build()
}

fun errorResponse(request: Request, code: Int, message: String): Response {
    return Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_1_1)
        .code(code)
        .message(message)
        .body(ResponseBody.create(APPLICATION_JSON, EMPTY_BODY))
        .build()
}