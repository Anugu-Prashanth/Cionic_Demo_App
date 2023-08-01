package com.example.cionicdemoapp

import io.mockk.MockKAnnotations

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
open class BaseTest {
    init {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }
}