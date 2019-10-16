package com.riccardobusetti.unibztimetable

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.riccardobusetti.unibztimetable.data.network.WebSiteUrl
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.riccardobusetti.unibztimetable", appContext.packageName)
    }

    @Test
    fun is_link_builded_correctly() {
        val link = WebSiteUrl.Builder()
            .withLanguage("it")
            .withDepartment("44")
            .withDegree("13075,13241")
            .withStudyPlan("16304")
            .fromDate("2019-10-04")
            .toDate("2020-06-01")
            .build()

        assertEquals(link.toString(), "https://www.unibz.it/it/timetable/?withDepartment=44&withDegree=13075%2C13241&studyPlan=16304%2C16969&fromDate=2019-10-04&toDate=2020-06-01")
    }
}
