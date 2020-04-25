package com.a.getmimo

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.a.getmimo.data.source.remote.MimoDB
import com.a.getmimo.ui.MainActivity
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.Matchers.not
import org.hamcrest.core.StringContains.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.get


class MainUiTest : KoinTest {

    @get:Rule
    val mockWebServerRule = MockWebServerRule()

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, false, false)


    @Before
    fun setUp() {

        mockWebServerRule.server.enqueue(
            MockResponse().fromJson(
                ApplicationProvider.getApplicationContext(),
                "lesson.json"
            )
        )

        val resource = OkHttp3IdlingResource.create("OkHttp", get<MimoDB>().okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }

    @Test
    fun gettingLessons() {
        activityTestRule.launchActivity(null)

        onView(withId(R.id.main_first))
            .check(matches(isDisplayed()))
        onView(withId(R.id.main_first))
            .check(matches(withText(containsString("var "))))

        //wrong answer
        onView(withId(R.id.main_editText)).perform(typeText("num"))
        onView(withId(R.id.main_button)).check(matches(not(isEnabled())))

        // right answer
        onView(withId(R.id.main_editText)).perform(replaceText("number = "), closeSoftKeyboard())
        onView(withId(R.id.main_button)).check(matches(isEnabled()))

        // checking successful message
        onView(withId(R.id.main_button)).perform(click())
        onView(withId(R.id.done_title)).check(matches(isDisplayed()))
    }
}