package com.sumeyrapolat.nomi.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sumeyrapolat.nomi.R
import com.sumeyrapolat.nomi.ui.theme.Gray400

@Composable
fun ContactSavedInfoRow() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_info),
            contentDescription = null,
            tint = Gray400
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(id = R.string.contact_already_saved),
            style = MaterialTheme.typography.bodyMedium,
            color = Gray400
        )
    }
}
