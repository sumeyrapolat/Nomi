package com.sumeyrapolat.nomi.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sumeyrapolat.nomi.ui.theme.Gray300
import com.sumeyrapolat.nomi.ui.theme.Gray950
import com.sumeyrapolat.nomi.ui.theme.NomiTheme

@Composable
fun ContactGroupHeader(letter: String) {
    Box(
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),

        ) {
        Text(
            text = letter,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Gray300,
        )
    }

}


@Preview(showBackground = true)
@Composable
fun ContactGroupHeaderPreview() {
    NomiTheme {
        ContactGroupHeader(letter = "A")
    }
}