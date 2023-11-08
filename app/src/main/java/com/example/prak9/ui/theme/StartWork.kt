package com.example.prak9.ui.theme
import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

fun startMyWork(context: Context) {
    val workRequest = OneTimeWorkRequestBuilder<MyWorker>().build()
    WorkManager.getInstance(context).enqueue(workRequest)
}
