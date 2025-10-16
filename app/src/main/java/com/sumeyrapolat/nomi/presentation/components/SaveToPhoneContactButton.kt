package com.sumeyrapolat.nomi.presentation.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.sp
import com.sumeyrapolat.nomi.R
import com.sumeyrapolat.nomi.ui.theme.Gray100
import com.sumeyrapolat.nomi.ui.theme.Gray950
import com.sumeyrapolat.nomi.ui.theme.NomiTheme

@Composable
fun SaveToPhoneContactButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isContactAlreadySaved: Boolean = false,
    onClick: () -> Unit
) {
    val isButtonEnabled = enabled && !isContactAlreadySaved
    val borderColor = if (isButtonEnabled) Gray950 else Gray100
    val contentColor = if (isButtonEnabled) Gray950 else Gray100

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = onClick,
            enabled = isButtonEnabled,
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, borderColor), // 🔹 dinamik border rengi
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = contentColor,           // 🔹 text & icon rengi
                disabledContentColor = Gray100,
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bookmark),
                    contentDescription = null,
                    tint = contentColor // 🔹 icon rengi
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.save_to_phone_contact),
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.titleMedium.copy(color = contentColor)
                )
            }
        }

        if (isContactAlreadySaved) {
            ContactSavedInfoRow()
        }
    }
}

