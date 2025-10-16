package com.sumeyrapolat.nomi.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sumeyrapolat.nomi.R
import com.sumeyrapolat.nomi.ui.theme.BackgroundDark
import com.sumeyrapolat.nomi.ui.theme.BackgroundLight
import com.sumeyrapolat.nomi.ui.theme.Gray300
import com.sumeyrapolat.nomi.ui.theme.NomiTheme
import com.sumeyrapolat.nomi.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = {
            text = it
            onSearch(it)
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.contacts_search_hint),
                color = Gray300,
                fontSize = 12.sp,
                style = Typography.bodySmall
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = Gray300
            )
        },
        textStyle = Typography.bodyMedium,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = BackgroundLight,
            unfocusedContainerColor = BackgroundLight,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = BackgroundDark, // ✅ görünür metin
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground, // ✅ görünür metin
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    NomiTheme { // Assuming you have a theme wrapper like this
        SearchBar(onSearch = {})
    }
}
