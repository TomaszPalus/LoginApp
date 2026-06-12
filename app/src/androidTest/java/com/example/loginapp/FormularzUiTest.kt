package com.example.loginapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FormularzUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun test_czyElementyStartoweSaWidoczne() {
        // Weryfikacja obecności nagłówka tekstowego
        composeTestRule.onNodeWithText("Panel Logowania").assertIsDisplayed()

        // Weryfikacja pól formularza na podstawie etykiet tekstowych
        composeTestRule.onNodeWithText("E-mail").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hasło").assertIsDisplayed()

        // Weryfikacja przycisku logowania
        composeTestRule.onNodeWithText("Zaloguj").assertIsDisplayed()
    }

    @Test
    fun test_poprawneLogowanie_wyswietlaKomunikatSukcesu() {
        // Wprowadzenie poprawnego adresu e-mail
        composeTestRule.onNodeWithTag("email_input").performTextInput("student@uczelnia.pl")

        // Wprowadzenie hasła (min. 6 znaków)
        composeTestRule.onNodeWithTag("password_input").performTextInput("Tajnie123!")

        // Kliknięcie w przycisk logowania
        composeTestRule.onNodeWithTag("login_button").performClick()

        // Asercja: Czy pojawił się komunikat sukcesu
        composeTestRule.onNodeWithTag("success_message").assertIsDisplayed()
        composeTestRule.onNodeWithText("Zalogowano pomyślnie").assertIsDisplayed()
    }

    @Test
    fun test_blednyFormatEmail_blokujePrzycisk_i_wyswietlaBlad() {
        // Wprowadzenie niepoprawnego formatu e-mail oraz za krótkiego hasła
        composeTestRule.onNodeWithTag("email_input").performTextInput("bledny_email_at_domain.com")
        composeTestRule.onNodeWithTag("password_input").performTextInput("123")

        // Asercja: Przycisk powinien być nieaktywny
        composeTestRule.onNodeWithTag("login_button").assertIsNotEnabled()

        // Asercja: Czy komunikat o błędzie walidacji jest widoczny?
        composeTestRule.onNodeWithTag("error_message").assertIsDisplayed()
    }

    @Test
    fun test_rageClicking_StabilnoscInterfejsu() {
        composeTestRule.onNodeWithTag("email_input").performTextInput("user@test.pl")
        composeTestRule.onNodeWithTag("password_input").performTextInput("Haslo123")

        // Symulacja szybkiego, wielokrotnego klikania w przycisk (Rage Clicking)
        repeat(10) {
            composeTestRule.onNodeWithTag("login_button").performClick()
        }

        // Interfejs powinien zachować stabilność, a komunikat sukcesu powinien być widoczny
        composeTestRule.onNodeWithTag("success_message").assertIsDisplayed()
    }

    @Test
    fun test_rotacjaEkranu_zachowujeStanFormularza() {
        val wpisanyEmail = "test_rotacji@wp.pl"

        // 1. Wprowadzamy tekst za pomocą performTextReplacement (zapewnia trwałość stanu)
        composeTestRule.onNodeWithTag("email_input").performTextReplacement(wpisanyEmail)

        // 2. Pobieramy instancję urządzenia i wykonujemy fizyczny obrót
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.setOrientationLeft()
        composeTestRule.waitForIdle()

        // 3. Sprawdzamy czy tekst istnieje w polu po fizycznej rotacji ekranu emulatora
        composeTestRule.onNodeWithTag("email_input").assertTextContains(wpisanyEmail)

        // 4. Przywracamy domyślną orientację urządzenia
        device.setOrientationNatural()
        composeTestRule.waitForIdle()
    }

    @Test
    fun test_czyszczeniePol_resetujeStanPrzycisku() {
        // Wprowadzenie poprawnych danych
        composeTestRule.onNodeWithTag("email_input").performTextInput("test@example.com")
        composeTestRule.onNodeWithTag("password_input").performTextInput("Haslo123")

        // Sprawdzenie czy przycisk się aktywował
        composeTestRule.onNodeWithTag("login_button").assertIsEnabled()

        // Wyczyszczenie pól tekstowych
        composeTestRule.onNodeWithTag("email_input").performTextClearance()
        composeTestRule.onNodeWithTag("password_input").performTextClearance()

        // Asercja końcowa: Przycisk powinien być ponownie zablokowany
        composeTestRule.onNodeWithTag("login_button").assertIsNotEnabled()
    }
}