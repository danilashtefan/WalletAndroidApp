package com.example.wallet

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.wallet.helpers.Strings
import com.example.wallet.ui.MainActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
//    val server = MockWebServer()
//
//    @Before
//    fun setUp() {
//        server.start(8080)
//    }
//
//    @After
//    fun finally() {
//        server.shutdown()
//    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.wallet", appContext.packageName)
    }

    @Test
    fun LoginWithEmptyFieldsTest() {
        composeTestRule.onNodeWithText("Sign In").performClick()
        composeTestRule.onNodeWithText("Username or password are too short").assertIsDisplayed()
    }

    @Test
    fun LoginWithIncorrectUsernameAndPassword() {
        composeTestRule.onNodeWithTag(Strings.USERNAME_TEXTFIELD_TAG).performTextInput("abc")
        composeTestRule.onNodeWithTag(Strings.PASSWORD_TEXTFIELD_TAG).performTextInput("abc")
        composeTestRule.onNodeWithText("Sign In").performClick()
        composeTestRule.onNodeWithText("Login failed. Username and password are incorrect")
            .assertIsDisplayed()
    }

    @Test
    fun LoginWithoutInternet() {
        composeTestRule.onNodeWithTag(Strings.USERNAME_TEXTFIELD_TAG).performTextInput("abc")
        composeTestRule.onNodeWithTag(Strings.PASSWORD_TEXTFIELD_TAG).performTextInput("abc")
        composeTestRule.onNodeWithText("Sign In").performClick()
        composeTestRule.onNodeWithText("Login failed. Failed to connect").assertIsDisplayed()
    }

    @Test
    fun SuccessfulLogin() {
        //server.enqueue(MockResponse().setBody(readFileWithoutNewLineFromResources("expense_response.json")))
        composeTestRule.onNodeWithTag(Strings.USERNAME_TEXTFIELD_TAG).performTextInput("jack")
        composeTestRule.onNodeWithTag(Strings.PASSWORD_TEXTFIELD_TAG).performTextInput("1234")

    }

    @Throws(IOException::class)
    fun readFileWithoutNewLineFromResources(fileName: String): String {
        var inputStream: InputStream? = null
        try {
            inputStream =
                javaClass.classLoader?.getResourceAsStream(fileName)
            val builder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(inputStream))

            var str: String? = reader.readLine()
            while (str != null) {
                builder.append(str)
                str = reader.readLine()
            }
            return builder.toString()
        } finally {
            inputStream?.close()
        }
    }
}
