/*
 *abiola 2024
 */

package com.mshdabiola.network

import io.ktor.client.HttpClient
import javax.inject.Inject

class INetworkDataSource @Inject constructor(
    private val httpClient: HttpClient,
) : NetworkDataSource {

    override suspend fun getRecommendation(): List<String> {
//        val response = httpClient.get(
//            Request.Recommendations(
//                limit = "10",
//                market = "NG",
//                seed_artists = "4NHQUGzhtTLFvgF5SZesLK",
//                seed_genres = "classical",
//                seed_tracks = "0c6xIDDpzE81m2q797ordA"
//            )
//        )
//        val netWorkTracks: PagingNetWorkTracks = if (response.status == HttpStatusCode.OK) {
//            response.body()
//        } else {
//            val message: Message = response.body()
//            throw Exception(message.error.message)
//        }
//
//
//        return netWorkTracks.tracks
//    }
        TODO()
    }
}
