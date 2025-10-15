package com.sumeyrapolat.nomi.presentation.components

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun RequestMediaPermissions(
    onCameraGranted: () -> Unit,
    onGalleryGranted: () -> Unit
) {
    val context = LocalContext.current

    // Kamera izni
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) onCameraGranted()
        else Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
    }

    // Galeri izni (Android sürümüne göre değişiyor)
    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) onGalleryGranted()
        else Toast.makeText(context, "Gallery permission denied", Toast.LENGTH_SHORT).show()
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedButton(onClick = {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }) {
            Text("Request Camera Permission")
        }

        OutlinedButton(onClick = {
            val permission =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    Manifest.permission.READ_MEDIA_IMAGES
                else
                    Manifest.permission.READ_EXTERNAL_STORAGE

            galleryPermissionLauncher.launch(permission)
        }) {
            Text("Request Gallery Permission")
        }
    }
}
