package com.vzkz.core.data.import_export

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.vzkz.common.general.TestDispatchers
import com.vzkz.common.general.data_generator.dummyMatch
import com.vzkz.common.general.data_generator.dummySingleMatchJson
import com.vzkz.common.general.fake.FakeLocalStorageRepository
import com.vzkz.common.test.util.MainCoroutineExtension
import com.vzkz.core.data.import_export.model.ExportMatchListSerializable
import com.vzkz.core.database.domain.LocalStorageRepository
import com.vzkz.core.domain.model.Match
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.ZonedDateTime
import kotlin.math.exp

@OptIn(InternalSerializationApi::class, ExperimentalCoroutinesApi::class)
class ImportExportToJsonTest {

    private lateinit var testDispatchers: TestDispatchers

    private lateinit var fakeLocalStorageRepository: FakeLocalStorageRepository

    private lateinit var json: Json

    //SUT
    private lateinit var importExportToJson: ImportExportToJson

    val matchList = mutableListOf<Match>()

    @BeforeEach
    fun setUp() {
        testDispatchers = TestDispatchers(mainCoroutineExtension.testDispatcher)

        fakeLocalStorageRepository = FakeLocalStorageRepository()

        json = Json {
            prettyPrint = true
            encodeDefaults = true
            ignoreUnknownKeys = true
            explicitNulls = false
        }

        importExportToJson = ImportExportToJson(
            localStorageRepository = fakeLocalStorageRepository,
            json = json
        )
        runBlocking {
            matchList.add(dummyMatch())
            repeat(3) { matchList.add(dummyMatch(randomizeUUIDs = true)) }
            fakeLocalStorageRepository.insertMatchList(matchList)
        }
    }

    companion object {
        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }

    @Test
    fun `export writes valid json`() = runTest {
        // Arrange
        val output = ByteArrayOutputStream()

        // Act
        importExportToJson.exportData(output)

        val jsonString = output.toString(Charsets.UTF_8.name())
        val exportMatchListSerializable =
            json.decodeFromString<ExportMatchListSerializable>(jsonString)

        // Assert
        assertThat(exportMatchListSerializable.items.size).isEqualTo(4)
    }

    @Test
    fun `Mapping a ExportMatchSerializable to Match`() = runTest {
        // Arrange
        val output = ByteArrayOutputStream()

        // Act
        importExportToJson.exportData(output)

        val jsonString = output.toString(Charsets.UTF_8.name())
        val exportMatchListSerializable =
            json.decodeFromString<ExportMatchListSerializable>(jsonString)
        val mappedMatch = exportMatchListSerializable.items.first().toMatch()

        // Assert
        val dummyMatch = dummyMatch()
        assertThat(mappedMatch).isEqualTo(dummyMatch)
    }

    @Test
    fun `import reads valid json`() = runTest {
        fakeLocalStorageRepository.deleteAllMatches()

        val input = ByteArrayInputStream(dummySingleMatchJson().toByteArray())

        importExportToJson.importData(input)

        assertThat(fakeLocalStorageRepository.getMatchHistory().first().size).isEqualTo(1)
    }

    @Test
    fun `Export data, clear db, then import, restores db`() = runTest {
        // Arrange
        val output = ByteArrayOutputStream()

        // Act
        importExportToJson.exportData(output)
        fakeLocalStorageRepository.deleteAllMatches()
        assertThat(fakeLocalStorageRepository.getMatchHistory().first().size).isEqualTo(0)

        val input = ByteArrayInputStream(output.toByteArray())
        importExportToJson.importData(input)

        // Assert
        assertThat(fakeLocalStorageRepository.getMatchHistory().first())
            .isEqualTo(matchList)
    }
}












