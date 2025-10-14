package com.sumeyrapolat.nomi.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sumeyrapolat.nomi.ui.theme.BackgroundDark
import com.sumeyrapolat.nomi.ui.theme.BackgroundLight
import com.sumeyrapolat.nomi.ui.theme.Gray50
import com.sumeyrapolat.nomi.ui.theme.Gray100
import com.sumeyrapolat.nomi.ui.theme.Gray300
import com.sumeyrapolat.nomi.ui.theme.Gray900
import com.sumeyrapolat.nomi.ui.theme.Gray950
import com.sumeyrapolat.nomi.ui.theme.NomiTheme
import com.sumeyrapolat.nomi.ui.theme.PrimaryBlue
import com.sumeyrapolat.nomi.ui.theme.Typography

@Composable
fun RecentSearchesSection(
    recentSearches: List<String>,
    onClearAll: () -> Unit,
    onRemoveItem: (String) -> Unit,
    onSearchClick: (String) -> Unit
) {
    if (recentSearches.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            // Başlık kısmı
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SEARCH HISTORY",
                    style = Typography.labelLarge.copy(
                        color = Gray300,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = "Clear All",
                    color = PrimaryBlue,
                    style = Typography.labelLarge,
                    modifier = Modifier.clickable { onClearAll() }
                )
            }

            Spacer(Modifier.height(8.dp))

            // Kart görünümü (tüm geçmişi saran)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundLight),
                ) {
                Column(modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                ) {
                    recentSearches.forEachIndexed { index, query ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSearchClick(query) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { onRemoveItem(query) }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Remove Search",
                                    tint = BackgroundDark
                                )
                            }
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = query,
                                color = BackgroundDark,
                                style = Typography.titleMedium
                            )
                        }

                        if (index < recentSearches.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                                color = Gray100,
                                thickness = 0.5.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Recent Searches Preview")
@Composable
fun RecentSearchesSectionPreview() {
    NomiTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray50)
                .padding(16.dp)
        ) {
            RecentSearchesSection(
                recentSearches = listOf("Adam", "Jessica", "Tim"),
                onClearAll = {},
                onRemoveItem = {},
                onSearchClick = {}
            )
        }
    }
}
