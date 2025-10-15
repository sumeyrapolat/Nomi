package com.sumeyrapolat.nomi.presentation.components

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import java.util.Objects

/**
 * Kamera işlemlerini yöneten ve başlatma fonksiyonunu sağlayan bir state holder.
 *
 * @param onImageTaken Fotoğraf başarıyla çekildiğinde URI'ı ileten bir callback.
 * @return Kamera işlemlerini başlatmak için bir [CameraManager] nesnesi.
 */
@Composable
fun rememberCameraManager(
    onImageTaken: (Uri) -> Unit
): CameraManager {
    val context = LocalContext.current
    var tempUri by remember { mutableStateOf<Uri?>(null) }

    // Çekilen fotoğrafı almak için bir launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                tempUri?.let { uri ->
                    onImageTaken(uri)
                }
            }
        }
    )

    // İzin istemek için bir launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // İzin verildiyse, geçici bir URI oluştur ve kamerayı başlat
                val uri = createImageUri(context)
                tempUri = uri
                cameraLauncher.launch(uri)
            }
            // İzin reddedildiyse, bu örnekte bir şey yapmıyoruz.
            // Gerekirse burada kullanıcıya bir mesaj gösterebilirsiniz.
        }
    )

    // Bu `remember` bloğu, dışarıya verilecek `CameraManager` nesnesinin
    // gereksiz yere yeniden oluşturulmasını engeller.
    return remember {
        CameraManager(
            launch = { // <-- Düzeltildi: 'onLaunch' yerine 'launch'
                // Gerçek bir uygulamada, önce iznin olup olmadığını kontrol etmek daha iyidir.
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        )
    }
}

/**
 * Kamera başlatma fonksiyonunu tutan basit bir veri sınıfı.
 */
data class CameraManager(
    val launch: () -> Unit
)

/**
 * Kameranın fotoğrafı kaydedeceği geçici bir dosya için URI oluşturur.
 */
private fun createImageUri(context: Context): Uri {
    val imageFile = File.createTempFile(
        "JPEG_${System.currentTimeMillis()}_",
        ".jpg",
        context.cacheDir
    ).apply {
        createNewFile()
    }

    return FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "${context.packageName}.provider", // Manifest'teki authority ile aynı olmalı
        imageFile
    )
}