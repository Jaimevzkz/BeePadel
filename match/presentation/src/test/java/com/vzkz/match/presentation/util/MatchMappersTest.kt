package com.vzkz.match.presentation.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.vzkz.common.generateSet
import com.vzkz.common.match
import com.vzkz.common.set
import com.vzkz.match.presentation.match_history.model.MatchUi
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.UUID
import kotlin.math.min
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class MatchMappersTest {
    private val matchUI = MatchUi(
        isMatchWon = true,
        formatedSetList = match().setList.map { it.toFormattedSet() },
        dateTimeUtc = "Jun 29, 2025 - 02:30PM",
        elapsedTime = "01:30:43",
        matchId = UUID.randomUUID()
    )

    @Test
    fun `formatting a completed set works`() {
        val expectedResult = Pair(6, 4)
        val set = set()

        val result = set.toFormattedSet()

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `formatting an incomplete set works`() {
        val expectedResult = Pair(5, 4)
        val set = set().copy(gameList = set().gameList.dropLast(1))

        val result = set.toFormattedSet()

        assertThat(result).isEqualTo(expectedResult)
    }

    @ParameterizedTest
    @CsvSource(
        "1, 0, 34, 01:00:34",
        "0, 23, 0, 00:23:00",
    )
    fun `elapsed time is formatted correctly`(
        hours: Int,
        minutes: Int,
        seconds: Int,
        expectedFormat: String
    ) {
        val match = match().copy(elapsedTime = hours.hours + minutes.minutes + seconds.seconds)

        val result = match.elapsedTime.formatted()

        assertThat(result).isEqualTo(expectedFormat)
    }

    @Test
    fun `from Match to MatchUi mapping`() {
        val expectedResult = matchUI

        val result = match().toMatchUi()

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `loosing a match is recognized`() {
        val expectedResult = matchUI.copy(
            isMatchWon = false,
            formatedSetList = List(6) { Pair(0, 6) }
        )

        val result = match()
            .copy(
                setList = List(6) { generateSet(0, 6) }
            )
            .toMatchUi()

        assertThat(result).isEqualTo(expectedResult)
    }
}