/*
 *abiola 2024
 */

package com.mshdabiola.network

import com.mshdabiola.network.model.Model
import com.mshdabiola.network.request.Articles3
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import javax.inject.Inject

class NetworkSource @Inject constructor(
    private val httpClient: HttpClient,
) {

    suspend fun get(): Model {
        return httpClient.get(Articles3.New()).body()
    }
}
