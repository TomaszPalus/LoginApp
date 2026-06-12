package com.example.loginapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    // Powiązanie widoku z ViewModelu, który właśnie utworzyłeś
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Panel Logowania",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Pole wejściowe dla adresu E-mail
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("E-mail") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("email_input"), // Tag używany przez testy UI
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pole wejściowe dla hasła
        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Hasło") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("password_input"), // Tag używany przez testy UI
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Przycisk logowania - aktywuje się tylko, gdy walidacja przejdzie pomyślnie
        Button(
            onClick = { viewModel.login() },
            enabled = viewModel.errorMessage == null && viewModel.email.isNotEmpty() && viewModel.password.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_button") // Tag używany przez testy UI
        ) {
            Text("Zaloguj")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sekcja wyświetlania błędów walidacji
        viewModel.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.testTag("error_message")
            )
        }

        // Sekcja wyświetlania komunikatu o sukcesie
        viewModel.successMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.testTag("success_message")
            )
        }
    }
}