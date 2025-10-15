package com.sumeyrapolat.nomi.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sumeyrapolat.nomi.ui.theme.BackgroundDark
import com.sumeyrapolat.nomi.ui.theme.BackgroundLight
import com.sumeyrapolat.nomi.ui.theme.Gray100
import com.sumeyrapolat.nomi.ui.theme.Gray400
import com.sumeyrapolat.nomi.ui.theme.Gray950

@Composable
fun ContactTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { // 👈 yukarı kaymayan metin
            Text(
                text = label,
                fontSize = 12.sp,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                color = Gray400
            )
        },
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 14.sp,
            color = Gray950
        ),
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors( // 👈 senin sürümde bu var
            focusedContainerColor = BackgroundLight,
            unfocusedContainerColor = BackgroundLight,
            focusedIndicatorColor = Gray950,
            unfocusedIndicatorColor = Gray100,
            cursorColor = Gray950,
            focusedTextColor = BackgroundDark,
            unfocusedTextColor = Gray950
        )
    )
}
