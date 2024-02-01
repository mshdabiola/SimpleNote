/*
 *abiola 2024
 */

package com.mshdabiola.network.di

import com.mshdabiola.network.Config
import com.mshdabiola.network.INetworkDataSource
import com.mshdabiola.network.NetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun clientProvider() = HttpClient(Android) {
        install(Resources)
        install(Logging) {
            logger = io.ktor.client.plugins.logging.Logger.SIMPLE
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(
                Json {
                    this.ignoreUnknownKeys = true
                },
            )
        }
        defaultRequest {
            headers {
                this[HttpHeaders.Authorization] = "Bearer ${Config.token}"
                this[HttpHeaders.Accept] = "application/json"
                this[HttpHeaders.ContentType] = "application/json"
            }
            url {
                host = "api.spotify.com"
                protocol = URLProtocol.HTTPS
            }
        }
        install(UserAgent) {
            agent = "my app"
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(5)
            exponentialDelay()
        }
        install(HttpCache) {
            val file = File.createTempFile("abiola", "tem")
            publicStorage(FileStorage(file))
        }
    }
}

@InstallIn(SingletonComponent::class)
@Module
interface NetworkBind {

    @Binds
    @Singleton
    fun bindNetworkDataSource(iNetworkDataSource: INetworkDataSource): NetworkDataSource
}
