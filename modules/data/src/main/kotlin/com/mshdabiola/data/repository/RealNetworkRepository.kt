/*
 *abiola 2024
 */

package com.mshdabiola.data.repository

import com.mshdabiola.network.NetworkSource
import javax.inject.Inject

class RealNetworkRepository @Inject constructor(
    private val networkSource: NetworkSource,
) : NetworkRepository {
    override suspend fun get() {
        networkSource.get()
    }
}
