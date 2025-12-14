package com.vzkz.match.data.networking

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.vzkz.common.general.data_generator.dummyMatch
import com.vzkz.common.general.data_generator.generateDummySet
import com.vzkz.core.data.networking.SPORT
import com.vzkz.core.data.networking.TYPE
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class CreateStravaActivityRequestTest {

    @Test
    fun `Create a correctly formatted strava activity request`() = runTest {
        // Arrange
        val name = "Pádel Match"
        val description = "Set 1: 6-4 / Set 2: 6-2 / Set 3: 2-6 / Set 4: 7-5 -> 🏆"
        val expectedRequest =
            CreateStravaActivityRequest(
                name = name,
                type = TYPE,
                sportType = SPORT,
                startDateLocal = "2025-06-29T14:30:24Z",
                elapsedTime = (1.hours + 30.minutes + 43.seconds).inWholeSeconds.toInt(),
                description = description
            )
        val initialMatch = dummyMatch()
        // Act
        val result = initialMatch.createRequestFromMatch(
            name = name,
            description = description
        )

        // Assert
        assertThat(result).isEqualTo(expectedRequest)
    }


    @Test
    fun `Create a correctly formatted strava activity request when loosing`() = runTest {
        // Arrange
        val name = "Pádel Match"
        val description ="Set 1: 6-4 / Set 2: 2-6 / Set 3: 6-7 -> ❌"
        val expectedRequest =
            CreateStravaActivityRequest(
                name = name,
                type = TYPE,
                sportType = SPORT,
                startDateLocal = "2025-06-29T14:30:24Z",
                elapsedTime = (1.hours + 30.minutes + 43.seconds).inWholeSeconds.toInt(),
                description = description
            )
        val initialMatch = dummyMatch().copy(
            setList = listOf(
                generateDummySet(false, 6, 4),
                generateDummySet(false, 2, 6),
                generateDummySet(false, 6, 7),
            )
        )
        // Act
        val result = initialMatch.createRequestFromMatch(
            name = name,
            description = description
        )

        // Assert
        assertThat(result).isEqualTo(expectedRequest)
    }

    @Test
    fun `Create a correctly formatted strava activity request when drawing`() = runTest {
        // Arrange
        val name = "Pádel Match"
        val description = "Set 1: 6-4 / Set 2: 2-6 -> 🟰"
        val expectedRequest =
            CreateStravaActivityRequest(
                name = name,
                type = TYPE,
                sportType = SPORT,
                startDateLocal = "2025-06-29T14:30:24Z",
                elapsedTime = (1.hours + 30.minutes + 43.seconds).inWholeSeconds.toInt(),
                description = description
            )
        val initialMatch = dummyMatch().copy(
            setList = listOf(
                generateDummySet(false, 6, 4),
                generateDummySet(false, 2, 6),
            )
        )
        // Act
        val result = initialMatch.createRequestFromMatch(
            name = name,
            description = description
        )

        // Assert
        assertThat(result).isEqualTo(expectedRequest)
    }
}