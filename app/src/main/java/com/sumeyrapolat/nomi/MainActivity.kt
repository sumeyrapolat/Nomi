package com.sumeyrapolat.nomi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sumeyrapolat.nomi.presentation.components.*
import com.sumeyrapolat.nomi.ui.theme.NomiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NomiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DummySwipeTestScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    )
                }
            }
        }
    }
}

@Composable
fun DummySwipeTestScreen(modifier: Modifier = Modifier) {
    var showEditSheet by remember { mutableStateOf(false) }
    var showDeleteSheet by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SwipeableContactRow(
            name = "Alice 2",
            phoneNumber = "+1234567890",
            onEditClick = { showEditSheet = true },
            onDeleteClick = { showDeleteSheet = true }
        )

        SwipeableContactRow(
            name = "Benjamin",
            phoneNumber = "+905551112233",
            onEditClick = { showEditSheet = true },
            onDeleteClick = { showDeleteSheet = true }
        )

        SwipeableContactRow(
            name = "Clara",
            phoneNumber = "+905556667788",
            onEditClick = { showEditSheet = true },
            onDeleteClick = { showDeleteSheet = true }
        )
    }

    // ✨ Edit Bottom Sheet (AddNewContact)
    AddNewContactBottomSheet(
        isVisible = showEditSheet,
        onDismiss = { showEditSheet = false },
        onSave = { first, last, phone ->
            println("Saved contact: $first $last ($phone)")
            showEditSheet = false
        }
    )

    // 🗑 Delete Bottom Sheet
    DeleteContactBottomSheet(
        isVisible = showDeleteSheet,
        onDismiss = { showDeleteSheet = false },
        onConfirm = {
            println("Contact deleted")
            showDeleteSheet = false
        }
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800, name = "BottomSheet Swipe Preview")
@Composable
fun DummySwipeTestPreview() {
    NomiTheme {
        DummySwipeTestScreen()
    }
}
