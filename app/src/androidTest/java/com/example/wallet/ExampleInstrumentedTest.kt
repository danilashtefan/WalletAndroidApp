package com.example.wallet

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.example.wallet.api.WalletWebService
import com.example.wallet.helpers.Strings
import com.example.wallet.requests.LoginRequest
import com.example.wallet.ui.MainActivity
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.InputStreamReader


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


class ExampleInstrumentedTest {
    val server = MockWebServer()
    var baseUrl: HttpUrl = server.url("")
    var service =
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()
    var api = service.create(WalletWebService.WalletAPI::class.java)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
    }

    @After
    fun finally() {
        server.shutdown()
    }

    @Test
    fun mockTest() {
        server.enqueue(MockResponse().setBody(readStringFromFile("test_response.json")))
        var test = api.test().execute().body()
        if (test != null) {
            assertEquals("TEST", test.testVal)
        }
    }

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
    fun deserializeTransactions(){
        server.enqueue(MockResponse().setBody(readStringFromFile("transactions_response")))
        runBlocking {
            var response = api.getFilteredExpanses("test_header")
            assertNotNull(response)
        }
    }

    @Test
    fun deserializeWallets(){
        server.enqueue(MockResponse().setBody(readStringFromFile("wallets_response")))

        runBlocking {
            var response = api.getFilteredWallets("test_header")
            assertNotNull(response)
        }
    }

    @Test
    fun deserializeCategories(){
        server.enqueue(MockResponse().setBody(readStringFromFile("categories_response")))

        runBlocking {
            var response = api.getFilteredExpenseCategories("test_header")
            assertNotNull(response)
        }
    }

    @Test
    fun LoginWithIncorrectUsernameAndPassword() {
        server.enqueue(MockResponse().setBody(readStringFromFile("login_response")))
        composeTestRule.onNodeWithTag(Strings.USERNAME_TEXTFIELD_TAG).performTextInput("abc")
        composeTestRule.onNodeWithTag(Strings.PASSWORD_TEXTFIELD_TAG).performTextInput("abc")
        composeTestRule.onNodeWithText("Sign In").performClick()
        //Check also deserialization of response
        runBlocking {
            var response = api.login(LoginRequest("abc","abc"))
            if (response != null) {
                assertEquals("Test_access", response.access_token)
                assertEquals("Test_refresh", response.refresh_token)
            }
        }
        composeTestRule.onNodeWithText("Login failed. Username and password are incorrect").assertIsDisplayed()

    }


    @Test
    fun SuccessfulLogin() {
        navigateToTheAllTransactionsScreen()
        //This is how successfull navigation is checked
        composeTestRule.onNodeWithText("Set budget").assertIsDisplayed()

    }

    @Test
    fun addTransaction(){
        navigateToTheAllTransactionsScreen()
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())
    }

    @Test
    fun setBudget(){
        navigateToTheAllTransactionsScreen()
        composeTestRule.onNodeWithText("Set budget").performClick()
        composeTestRule.onNodeWithTag(Strings.SET_BUDGET_TEXTFIELD_TAG).performTextInput("999999")
        composeTestRule.onNodeWithText("Confirm").performClick()
        composeTestRule.onNodeWithText("Budget set: 999999 HUF").assertExists()
    }

    @Test
    fun exceedBudget(){
        navigateToTheAllTransactionsScreen()
        composeTestRule.onNodeWithText("Set budget").performClick()
        composeTestRule.onNodeWithTag(Strings.SET_BUDGET_TEXTFIELD_TAG).performTextInput("1")
        composeTestRule.onNodeWithText("Confirm").performClick()
        composeTestRule.onNodeWithTag(Strings.EXCEED_BUDGET_TEXTFIELD_TAG).assertIsDisplayed()
    }

    private fun navigateToTheAllTransactionsScreen() {
        composeTestRule.onNodeWithTag(Strings.USERNAME_TEXTFIELD_TAG).performTextInput("jack")
        composeTestRule.onNodeWithTag(Strings.PASSWORD_TEXTFIELD_TAG).performTextInput("1234")
        composeTestRule.onNodeWithText("Sign In").performClick()
    }


    fun readStringFromFile(fileName: String): String {
        try {
            val inputStream = (InstrumentationRegistry.getInstrumentation().targetContext
                .applicationContext).assets.open(fileName)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        } catch (e: IOException) {
            throw e
        }
    }
}
