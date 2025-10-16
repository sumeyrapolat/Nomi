package com.sumeyrapolat.nomi.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sumeyrapolat.nomi.R
import com.sumeyrapolat.nomi.ui.theme.Gray900
import com.sumeyrapolat.nomi.ui.theme.Gray950
import com.sumeyrapolat.nomi.ui.theme.NomiTheme
import com.sumeyrapolat.nomi.ui.theme.Typography

@Composable
fun NoResultsState() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_close), // 🔍 ya da uygun bir “no results” icon
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.contacts_no_results_title),
            color = Gray950,
            style = Typography.headlineSmall
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = stringResource(id = R.string.contacts_no_results_subtitle),
            style = Typography.titleMedium,
            textAlign = TextAlign.Center,
            color = Gray900
        )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp", name = "No Results Preview")
@Composable
fun NoResultsStatePreview() {
    NomiTheme {
        NoResultsState()
    }
}
