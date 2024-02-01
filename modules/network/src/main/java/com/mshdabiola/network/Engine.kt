/*
 *abiola 2024
 */

package com.mshdabiola.network

import io.ktor.client.engine.HttpClientEngine
import javax.inject.Inject

class Engine @Inject constructor(
    private val httpClientEngine: HttpClientEngine,
)
