/*
 *abiola 2024
 */

package com.mshdabiola.network

interface NetworkDataSource {
    suspend fun getRecommendation(): List<String>
}
