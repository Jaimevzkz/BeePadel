package com.vzkz.match.presentation.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.vzkz.common.general.data_generator.generateDummySet
import com.vzkz.common.general.data_generator.dummyMatch
import com.vzkz.common.general.data_generator.dummySet
import com.vzkz.match.presentation.match_history.model.MatchUi
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class MatchMappersTest {
    private val match = dummyMatch()
    private val matchUI = MatchUi(
        isMatchWon = true,
        formatedSetList = dummyMatch().setList.map { it.getGamesForSet() },
        dateTimeUtc = "Jun 29, 2025 - 04:30PM",
        elapsedTime = "01:30:43",
        matchId = match.matchId
    )


    @Test
    fun `formatting a completed set works`() {
        val expectedResult = Pair(6, 4)
        val set = dummySet()

        val result = set.getGamesForSet()

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `formatting an incomplete set works`() {
        val expectedResult = Pair(5, 4)
        val set = dummySet().copy(gameList = dummySet().gameList.dropLast(1))

        val result = set.getGamesForSet()

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `formatting a complete match works`() {
        val expectedResult = Pair(3, 1)

        val result = dummyMatch().getSetsForMatch()

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `formatting a match with incomplete sets ignores them`() {
        val expectedResult = Pair(2, 1)
        val modifiedMatch = dummyMatch().copy(
            setList = listOf(
                generateDummySet(randomizeUUIDs = true, games1 = 5, games2 = 3),
                generateDummySet(randomizeUUIDs = true, games1 = 6, games2 = 3),
                generateDummySet(randomizeUUIDs = true, games1 = 2, games2 = 6),
                generateDummySet(randomizeUUIDs = true, games1 = 6, games2 = 4),
                generateDummySet(randomizeUUIDs = true, games1 = 6, games2 = 5),
            )
        )

        val result = modifiedMatch.getSetsForMatch()

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
        val match = dummyMatch().copy(elapsedTime = hours.hours + minutes.minutes + seconds.seconds)

        val result = match.elapsedTime.formatted()

        assertThat(result).isEqualTo(expectedFormat)
    }

    @Test
    fun `from Match to MatchUi mapping`() {
        val expectedResult = matchUI

        val result = match.toMatchUi()

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `loosing a match is recognized`() {
        val expectedResult = matchUI.copy(
            isMatchWon = false,
            formatedSetList = List(6) { Pair(0, 6) }
        )

        val result = match
            .copy(
                setList = List(6) {
                    generateDummySet(
                        randomizeUUIDs = true,
                        games1 = 0,
                        games2 = 6
                    )
                }
            )
            .toMatchUi()

        assertThat(result).isEqualTo(expectedResult)
    }
}