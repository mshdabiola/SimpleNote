/*
 *abiola 2024
 */

package com.mshdabiola.common.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val niaDispatcher: NiaDispatchers)

enum class NiaDispatchers {
    Default,
    IO,
}
