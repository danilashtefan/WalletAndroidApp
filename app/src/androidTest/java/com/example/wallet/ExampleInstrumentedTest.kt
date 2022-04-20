package com.example.wallet

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wallet.helpers.Strings
import com.example.wallet.ui.MainActivity
import com.example.wallet.ui.UsersApplication
import com.example.wallet.ui.theme.WalletTheme

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
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
    fun LoginWithIncorrectUsernameAndPassword(){
        composeTestRule.onNodeWithTag(Strings.USERNAME_TEXTFIELD_TAG).performTextInput("abc")
        composeTestRule.onNodeWithTag(Strings.PASSWORD_TEXTFIELD_TAG).performTextInput("abc")
        composeTestRule.onNodeWithText("Sign In").performClick()
        composeTestRule.onNodeWithText("Login failed. Username and password are incorrect").assertIsDisplayed()
    }

    @Test
    fun LoginWithoutInternet(){
        composeTestRule.onNodeWithTag(Strings.USERNAME_TEXTFIELD_TAG).performTextInput("abc")
        composeTestRule.onNodeWithTag(Strings.PASSWORD_TEXTFIELD_TAG).performTextInput("abc")
        composeTestRule.onNodeWithText("Sign In").performClick()
        composeTestRule.onNodeWithText("Login failed. Failed to connect").assertIsDisplayed()
    }

    @Test
    fun SuccessfulLogin(){
        composeTestRule.onNodeWithTag(Strings.USERNAME_TEXTFIELD_TAG).performTextInput("jack")
        composeTestRule.onNodeWithTag(Strings.PASSWORD_TEXTFIELD_TAG).performTextInput("1234")
    }
}