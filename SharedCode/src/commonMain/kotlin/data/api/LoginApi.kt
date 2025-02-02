package org.kotlin.mpp.mobile.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.defaultSerializer
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import kotlinx.serialization.json.Json
import org.kotlin.mpp.mobile.data.entity.AuthorizationRequest
import org.kotlin.mpp.mobile.data.entity.AuthorizationResponse
import org.kotlin.mpp.mobile.util.constants.AUTH_TOKEN
import org.kotlin.mpp.mobile.util.constants.AUTH_URL
import org.kotlin.mpp.mobile.util.log

class LoginApi(engine: HttpClientEngine) {

    private val client = HttpClient(engine) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun authorizeUser(username : String, password: String): AuthorizationResponse {
        val json = defaultSerializer()
        val response = client.post<HttpResponse> {
            url {
                protocol = URLProtocol.HTTPS
                host = AUTH_URL
                encodedPath = AUTH_TOKEN
            }
            body = json.write(AuthorizationRequest(CLIENT_ID, username, password))
            accept(ContentType.Application.Json)
        }
        val jsonBody = response.readText()
        return Json.parse(AuthorizationResponse.serializer(), jsonBody)
    }

    companion object {
        private const val CLIENT_ID = 13
    }
}