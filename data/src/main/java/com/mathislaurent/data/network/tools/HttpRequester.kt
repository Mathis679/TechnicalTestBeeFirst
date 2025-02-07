package com.mathislaurent.data.network.tools

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class HttpRequester private constructor(
    val endpoint: String?,
    val method: HttpMethod,
    val params: HashMap<String, Any>?,
    val dataFormat: DataFormat
) {

    suspend inline fun <reified T> request(): T = withContext(Dispatchers.IO) {
        if (endpoint == null) {
            throw HttpException.MissingEndpointException
        }
        val rawData = when (method) {
            HttpMethod.GET -> {
                get()
            }
            HttpMethod.POST -> {
                // TODO
                throw HttpException.UnsupportedHttpMethod
            }
            HttpMethod.PUT -> {
                // TODO
                throw HttpException.UnsupportedHttpMethod
            }
            HttpMethod.DELETE -> {
                // TODO
                throw HttpException.UnsupportedHttpMethod
            }
        }
        return@withContext parseData(rawData)
    }

    suspend inline fun <reified T> parseData(rawData: String): T = withContext(Dispatchers.IO) {
        when (dataFormat) {
            DataFormat.JSON -> {
                Gson().fromJson(rawData, T::class.java)
            }
        }
    }

    suspend fun get(): String = withContext(Dispatchers.IO) {
        var urlStr = endpoint
        if (params?.isNotEmpty() == true) {
            params.forEach { entry ->
                urlStr = urlStr?.replace("{${entry.key}}", entry.value.toString())
            }
        }
        val url = URL(urlStr)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.doInput = true
        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return@withContext connection.inputStream.bufferedReader().use { it.readText() }
        } else {
            val responseMessage = connection.responseMessage
            throw HttpException.ErrorEndpointException(
                responseCode = responseCode,
                responseMessage = responseMessage
            )
        }
    }

    data class Builder(
        private var endpoint: String? = null,
        private var method: HttpMethod = HttpMethod.GET,
        private var params: HashMap<String, Any>? = null,
        private var dataFormat: DataFormat = DataFormat.JSON
    ) {
        fun endpoint(endpoint: String) = apply { this.endpoint = endpoint }
        fun method(method: HttpMethod) = apply { this.method = method }
        fun params(params: HashMap<String, Any>) = apply { this.params = params }
        fun dataFormat(dataFormat: DataFormat) = apply { this.dataFormat = dataFormat }
        fun build() = HttpRequester(
            endpoint = endpoint,
            method = method,
            params = params,
            dataFormat = dataFormat
        )
    }

}

enum class HttpMethod {
    GET,
    POST,
    PUT,
    DELETE
}

enum class DataFormat {
    JSON
}

sealed class HttpException(override val message: String? = null): Exception() {
    data object MissingEndpointException: HttpException()
    data class ErrorEndpointException(val responseCode: Int, val responseMessage: String)
        : HttpException("Error HTTP code : $responseCode\nmessage: $responseMessage")
    data object UnsupportedHttpMethod: HttpException()
}