package com.example.prak9.ui.theme
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.prak9.R
import kotlinx.coroutines.delay


class MyWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        try {
            // Здесь выполняется фоновая работа.
            // Например, можете добавить задержку в 5 секунд.
            Log.d("doWork", "Началось выполнение работы")

            // Создаем всплывающее уведомление
            val notification = NotificationCompat.Builder(applicationContext, "channel_id")
                .setSmallIcon(androidx.core.R.drawable.notification_template_icon_bg)// Проверьте, что "ic_notification" - это правильное имя вашей иконки
                .setContentTitle("Работа")
                .setContentText("Работа сделана")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            // Отображаем уведомление
            val notificationManager = NotificationManagerCompat.from(applicationContext)
            if (ActivityCompat.checkSelfPermission(
                    this.applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                delay(10000)
                notificationManager.notify(1, notification)
                return Result.success()
            }
            else{
                return Result.failure()
            }




        } catch (e: Exception) {
            // Обработка ошибок и возврат Result.failure() в случае неудачи
            Log.e("doWork", "Произошла ошибка во время выполнения работы", e)
            return Result.failure()
        }
    }
}