package com.sumeyrapolat.nomi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sumeyrapolat.nomi.ui.theme.NomiTheme

data class IconItem(
    val name: String,
    val resId: Int
)

@Composable
fun OriginalIconsGrid() {
    val icons = listOf(
        IconItem("Done", R.drawable.ic_done),
        IconItem("Close", R.drawable.ic_close),
        IconItem("Phone", R.drawable.ic_phone),
        IconItem("Add", R.drawable.ic_add),
        IconItem("Contact", R.drawable.ic_contact),
        IconItem("Camera", R.drawable.ic_camera),
        IconItem("Gallery", R.drawable.ic_gallery),
        IconItem("Edit", R.drawable.ic_edit),
        IconItem("Delete", R.drawable.ic_delete),
        IconItem("Search", R.drawable.ic_search),
        IconItem("Search Off", R.drawable.ic_search_off),
        IconItem("Save", R.drawable.ic_bookmark),
        IconItem("Info", R.drawable.ic_info)
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(72.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(icons) { icon ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // 👇 Burada direkt painterResource kullanıyoruz
                    Image(
                        painter = painterResource(id = icon.resId),
                        contentDescription = icon.name,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = icon.name,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OriginalIconsPreview() {
    NomiTheme {
        OriginalIconsGrid()
    }
}
