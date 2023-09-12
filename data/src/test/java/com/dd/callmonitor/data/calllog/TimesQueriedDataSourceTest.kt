package com.dd.callmonitor.data.calllog

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class TimesQueriedDataSourceTest {

    private val scope = TestScope()

    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = scope,
        produceFile = {
            RuntimeEnvironment
                .getApplication()
                .preferencesDataStoreFile("TimesQueriedDataSourceTestDataStore")
        }
    )

    private val underTest = TimesQueriedDataSource(dataStore)

    @Test
    fun incrementAndGetReturnsOneFirstTime() = scope.runTest {
        val id = 4L

        val output = underTest.incrementAndGet(id)

        assertEquals(1, output)
    }

    @Test
    fun incrementAndGetIncrementsOnEveryCall() = scope.runTest {
        val id = 3L

        assertEquals(1, underTest.incrementAndGet(id))
        assertEquals(2, underTest.incrementAndGet(id))
    }

    @Test
    fun incrementAndGetReturnsDifferentOutputsForDifferentIds() = scope.runTest {
        val id1 = 13L
        val id2 = 23L

        assertEquals(1, underTest.incrementAndGet(id1))
        assertEquals(1, underTest.incrementAndGet(id2))

        assertEquals(2, underTest.incrementAndGet(id1))
        assertEquals(2, underTest.incrementAndGet(id2))
    }
}
