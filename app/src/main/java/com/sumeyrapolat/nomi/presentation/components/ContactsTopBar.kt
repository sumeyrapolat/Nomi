package com.sumeyrapolat.nomi.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sumeyrapolat.nomi.R
import com.sumeyrapolat.nomi.ui.theme.Gray950
import com.sumeyrapolat.nomi.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsTopBar(
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.contacts_title),
            style = Typography.headlineMedium,
            color = Gray950
        )
        IconButton(onClick = onAddClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "Add Contact",
                tint = null
            )
        }
    }
}
