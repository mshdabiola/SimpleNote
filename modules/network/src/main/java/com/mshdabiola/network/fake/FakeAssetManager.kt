/*
 *abiola 2022
 */

package com.mshdabiola.network.fake

import java.io.InputStream

fun interface FakeAssetManager {
    fun open(fileName: String): InputStream
}
