package com.sumeyrapolat.nomi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.sumeyrapolat.nomi.presentation.contacts.ContactsScreen
import com.sumeyrapolat.nomi.ui.theme.NomiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Status Bar simgelerini koyu yapma ayarı (Açık Gray100 arka plan için şart)
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = true

        setContent {
            NomiTheme {
                // ✅ Scaffold kaldırıldı, padding geçmeye gerek yok artık
                ContactsScreen(
                )
            }
        }
    }
}
