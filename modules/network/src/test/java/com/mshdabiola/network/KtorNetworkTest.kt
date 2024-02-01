/*
 *abiola 2024
 */

package com.mshdabiola.network

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class KtorNetworkTest {

    private lateinit var ktorNetwork: NetworkSource

    @Before
    fun setUp() {
        val engine = MockEngine { re ->
            respond(
                content = ByteReadChannel("""{"id":98,"name":"abiola"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val client = Client.get(engine)
        ktorNetwork = NetworkSource(client)
    }

    @After
    fun close() {
    }

    @Test
    fun get() = runTest {
        val model = ktorNetwork.get()
        assertEquals(model.id, 98)
    }
}
