package com.sumeyrapolat.nomi.presentation.contacts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sumeyrapolat.nomi.presentation.base.BaseScreen
import com.sumeyrapolat.nomi.presentation.components.ContactsTopBar
import com.sumeyrapolat.nomi.presentation.components.EmptyContactsState
import com.sumeyrapolat.nomi.presentation.components.SearchBar
import com.sumeyrapolat.nomi.ui.theme.NomiTheme

@Composable
fun ContactsScreenContent(
    padding: PaddingValues,
    onAddClick: () -> Unit,
    onCreateClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp)
    ) {
        SearchBar(onSearch = {})
        Spacer(Modifier.height(24.dp))
        //TODO: burada state e göre empty ise EmptyContactsState değil ise ContactListSection kullanılacak
        EmptyContactsState(onCreateClick = onCreateClick)
    }
}

@Composable
fun ContactsScreen(
    onAddClick: () -> Unit,
    onCreateClick: () -> Unit
) {
    // BaseScreen'i kullanmaya devam edin
    BaseScreen(
        topBar = { ContactsTopBar(onAddClick = onAddClick) }
    ) { padding ->
        // İçeriği burada çağırın
        ContactsScreenContent(padding, onAddClick, onCreateClick)
    }
}


@Preview(showSystemUi = true, showBackground = true, name = "Contacts Screen (Safe Preview)")
@Composable
fun ContactsScreenPreview() {
    NomiTheme {
        // Preview için, sistem bağımlı BaseScreen yerine
        // basit bir Scaffold kullanabiliriz (veya sadece içeriği çağırabiliriz)
        Scaffold(
            topBar = { ContactsTopBar(onAddClick = {}) },
            content = { padding ->
                ContactsScreenContent(padding, onAddClick = {}, onCreateClick = {})
            }
        )
    }
}
