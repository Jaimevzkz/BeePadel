package com.vzkz.match.presentation.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.vzkz.common.game
import com.vzkz.common.set
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ExtensionsTest {
    @Test
    fun `formatting a completed set works`(){
        val expectedResult = Pair(6,4)
        val set = set()

        val result = set.toFormattedSet()

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `formatting an incomplete set works`(){
        val expectedResult = Pair(5,4)
        val set = set().copy(gameList = set().gameList.dropLast(1))

        val result = set.toFormattedSet()

        assertThat(result).isEqualTo(expectedResult)
    }
}