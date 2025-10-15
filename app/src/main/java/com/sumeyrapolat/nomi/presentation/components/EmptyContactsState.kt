package com.sumeyrapolat.nomi.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sumeyrapolat.nomi.R
import com.sumeyrapolat.nomi.ui.theme.Gray900
import com.sumeyrapolat.nomi.ui.theme.Gray950
import com.sumeyrapolat.nomi.ui.theme.Mulish
import com.sumeyrapolat.nomi.ui.theme.NomiTheme
import com.sumeyrapolat.nomi.ui.theme.PrimaryBlue
import com.sumeyrapolat.nomi.ui.theme.Typography

@Composable
fun EmptyContactsState(
    onCreateClick: () -> Unit
) {
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_contact),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.contacts_no_contacts_title),
            color = Gray950,
            style = Typography.headlineSmall,
            fontSize = 15.sp,
            )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(id = R.string.contacts_no_contacts_subtitle),
            style = Typography.titleSmall,
            fontSize = 12.sp,
            color = Gray900
        )
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onCreateClick) {
            Text(
                text = stringResource(id = R.string.contacts_create_new),
                color = PrimaryBlue,
                style = Typography.labelSmall,
                fontSize = 12.sp,
                )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp", name = "Empty State Preview")
@Composable
fun EmptyContactsStatePreview() {
    // Wrap your composable in your application's theme to ensure correct colors and typography
    NomiTheme {
        EmptyContactsState(
            onCreateClick = {
                // In a preview, this lambda doesn't need to do anything,
                // but you can add a print statement for debugging if needed.
                println("Create New Contact clicked in preview.")
            }
        )
    }
}