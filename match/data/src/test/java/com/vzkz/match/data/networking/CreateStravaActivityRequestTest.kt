package com.vzkz.match.data.networking

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.vzkz.common.general.data_generator.dummyMatch
import com.vzkz.core.data.networking.SPORT
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.exp
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class CreateStravaActivityRequestTest {

    @Test
    fun `Create a correctly formatted strava activity request`() = runTest {
        // Arrange
        val expectedRequest =
            CreateStravaActivityRequest(
                name = "Pádel Match",
                type = SPORT,
                sport_type = SPORT,
                start_date_local = "2025-06-29T14:30:24Z",
                elapsed_time = (1.hours + 30.minutes + 43.seconds).inWholeSeconds.toInt(),
                description = "Set 1: 6-4 / Set 2: 6-2 / Set 3: 2-6 / Set 4: 7-5 -> WON"
            )
        val initialMatch = dummyMatch()
        // Act
        val result = initialMatch.createRequestFromMatch()

        // Assert
        assertThat(result).isEqualTo(expectedRequest)
    }

}