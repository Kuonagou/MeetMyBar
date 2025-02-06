package com.example.frontend.data.api

import com.example.frontend.data.vo.BarVo
import com.example.frontend.data.vo.DrinkVo
import com.example.frontend.data.vo.SimpleBarVo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.component.KoinComponent


class MeetMyBarAPI(
    private val client: HttpClient,
    private val baseUrl: String = " http://leop.letareau.fr:8080",
) : KoinComponent {

    suspend fun getDrinks(): List<DrinkVo> {
        return client.get("$baseUrl/drink") {
            contentType(ContentType.Application.Json)
        }.body<List<DrinkVo>>()
    }

    suspend fun getBars(): List<BarVo> {
        return client.get("$baseUrl/bar") {
            contentType(ContentType.Application.Json)
        }.body<List<BarVo>>()
    }

    suspend fun postBar(bar: SimpleBarVo): HttpResponse {
        return client.post("$baseUrl/bar") {
            contentType(ContentType.Application.Json)
            setBody(bar)
        }.body<HttpResponse>()
    }
}
