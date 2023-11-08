package com.example.prak9

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.SurfaceView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.prak9.ui.theme.MyWorker
import com.example.prak9.ui.theme.Prak9Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "My Channel"
            val channelDescription = "Описание вашего канала уведомлений"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("channel_id", channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        setContent {
            MyApp()
        }
    }
}
private fun downloadImage(url: String): Bitmap {
    val inputStream = URL(url).openStream()
    return BitmapFactory.decodeStream(inputStream)
}
private fun savePhoto(data: ByteArray, filesDir: File) {
    try {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val date = sdf.format(Date())
        val path = filesDir
        val letDirectory = File(path, "Photos")
        letDirectory.mkdirs()
        val photoFile = File(letDirectory, "$date.txt")
        FileOutputStream(photoFile).use {
            it.write(data)
        }
        val file = File(path, "date.txt")
        var text = "$date;"
        file.appendText(text)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
//    Surface(color = MaterialTheme.colorScheme.background) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Center
//        ) {
//            var url by remember { mutableStateOf("") }
//            var bitmap by remember { mutableStateOf<Bitmap?>(null) }
//
//            TextField(
//                value = url,
//                onValueChange = { url = it },
//                label = { Text("Enter image URL") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Button(
//
//                onClick = {
//
//                    GlobalScope.launch(Dispatchers.IO) {
//                        val downloadedBitmap = downloadImage(url)
//                        launch(Dispatchers.Main) {
//                            bitmap = downloadedBitmap
//                        }
//
//                    }
//                },
//                modifier = Modifier.align(Alignment.CenterHorizontally)
//            ) {
//                Text("Download")
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            bitmap?.let {
//                Image(
//                    bitmap = it.asImageBitmap(),
//                    contentDescription = "Downloaded image",
//                    contentScale = ContentScale.FillWidth,
//                    modifier = Modifier.fillMaxWidth()
//                )
//            } ?: Image(
//                painter = painterResource(id = R.drawable.ic_menu_report_image),
//                contentDescription = "Placeholder image",
//                contentScale = ContentScale.FillWidth,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//    }
    val navController = rememberNavController()

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text(text = "My App") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(

                content = {
                    IconButton(onClick = { navController.navigate("screen1") }, modifier = Modifier.size(100.dp)) {
                        Icon(Icons.Filled.Home, contentDescription = "web image getter",Modifier.size(100.dp))

                    }
                    IconButton(onClick = { navController.navigate("screen2") }, modifier = Modifier.size(100.dp)) {
                        Icon(Icons.Filled.DateRange, contentDescription = "notification button",Modifier.size(100.dp))
                    }
                    IconButton(onClick = { navController.navigate("screen3") }, modifier = Modifier.size(100.dp)) {
                        Icon(Icons.Filled.List, contentDescription = "Camera",Modifier.size(100.dp))
                    }

                }
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "screen1",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("screen1") {
                Screen1()
            }
            composable("screen2") {
                Screen2()
            }
            composable("screen3") {
                Screen3()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen1() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            var url by remember { mutableStateOf("") }
            var bitmap by remember { mutableStateOf<Bitmap?>(null) }

            TextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("Enter image URL") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    GlobalScope.launch(Dispatchers.IO) {
                        val downloadedBitmap = downloadImage(url)
                        launch(Dispatchers.Main) {
                            bitmap = downloadedBitmap
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Download")
            }

            Spacer(modifier = Modifier.height(16.dp))

            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Downloaded image",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
            } ?: Image(
                painter = painterResource(id = R.drawable.ic_menu_report_image),
                contentDescription = "Placeholder image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun Screen2() {
    val localContext = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = {
                    val workRequest = OneTimeWorkRequestBuilder<MyWorker>().build()
                    WorkManager.getInstance(localContext).enqueue(workRequest) },
                modifier = Modifier.size(500.dp)
            ) {
               Icon(imageVector = Icons.Default.Email, contentDescription = "notif button",Modifier.size(400.dp))
            }
        }
    }
}

@Composable
fun Screen3() {
    val context = LocalContext.current
    val camera = remember { Camera.open(0) }
    val surfaceView = remember { SurfaceView(context) }
    val surfaceHolder = remember { surfaceView.holder }

    LaunchedEffect(surfaceHolder) {

            // Открываем камеру
            camera.setPreviewDisplay(surfaceHolder)
            camera.startPreview()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    camera.takePicture(null, null) { data, camera ->
                        context.getExternalFilesDir(null)?.let { it1 -> savePhoto(data, it1) }
                        camera.startPreview()
                    }
                },
                modifier = Modifier.padding(20.dp)
            ) {
                Text("Capture")
            }

        }
        AndroidView(
            factory = { surfaceView },
            modifier = Modifier.fillMaxSize()
        )
    }
}

