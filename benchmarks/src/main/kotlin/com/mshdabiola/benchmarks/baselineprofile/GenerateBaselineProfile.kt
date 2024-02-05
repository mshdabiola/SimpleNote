/*
 *abiola 2022
 */

package com.mshdabiola.benchmarks.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import com.mshdabiola.benchmarks.PACKAGE_NAME
import com.mshdabiola.benchmarks.flingElementDownUp
import com.mshdabiola.benchmarks.startActivity
import com.mshdabiola.benchmarks.waitAndFindObject
import org.junit.Rule
import org.junit.Test

class GenerateBaselineProfile {
    @get:Rule val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() =
        baselineProfileRule.collect(PACKAGE_NAME) {
            startActivity()

            repeat(4) {
                device.waitAndFindObject(By.res("add"), 1000)
                    .click()

                device.waitAndFindObject(By.res("detail:title"), 5000)
                    .text = "Title"
                device.waitAndFindObject(By.res("detail:content:0"), 5000)
                    .text = "content"

                device.waitAndFindObject(By.res("detail:topbar:back"), 5000)
                    .click()
            }

//            val list= device.waitAndFindObject(By.res("main:list"),10000)
//
//            if (list.isScrollable) {
//                device.flingElementDownUp(list)
//            }


        }
}
