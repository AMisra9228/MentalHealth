package com.sample.mentalhealth

import org.junit.Assert.assertEquals
import org.junit.Test


class CalculatorTest {

    private val calculator = Calculator()

    @Test
    fun testAddition() {
        val result = calculator.add(4, 1)
        assertEquals(5, result)
    }
}