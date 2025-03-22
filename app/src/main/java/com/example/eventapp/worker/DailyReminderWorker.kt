package com.example.eventapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.eventapp.MainActivity
import com.example.eventapp.R
import com.example.eventapp.Retrofit.ApiConfig
import com.example.eventapp.Response.ListEventsItem
import java.text.SimpleDateFormat
import java.util.*

class DailyReminderWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val TAG = "DailyReminderWorker"
        private const val CHANNEL_ID = "EVENT_REMINDER"
        private const val CHANNEL_NAME = "Event Reminder"
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "DailyReminderWorker dimulai...")

        return try {
            val response = ApiConfig.getApiService().getNearestEvent()
            if (response.isSuccessful) {
                val events = response.body()?.listEvents

                if (!events.isNullOrEmpty()) {
                    val upcomingEvent = events.find { shouldShowReminder(it.beginTime) }

                    if (upcomingEvent != null) {
                        Log.d(TAG, "Event ditemukan: ${upcomingEvent.name} - ${upcomingEvent.beginTime}")
                        sendNotification(upcomingEvent)
                    } else {
                        Log.d(TAG, "Tidak ada event yang valid (sudah lewat atau tidak ditemukan)")
                    }
                } else {
                    Log.d(TAG, "Tidak ada event yang ditemukan dari API")
                }
            } else {
                Log.e(TAG, "Gagal mendapatkan response dari API: ${response.code()}")
            }
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Terjadi error: ${e.message}")
            Result.failure()
        }
    }

    private fun sendNotification(event: ListEventsItem) {
        val context = applicationContext
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Buat Notification Channel jika belum ada (hanya untuk Android O+)
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)

        // Intent ke MainActivity dengan event_id
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("event_id", event.id)
            putExtra("event_type", arrayOf("upcoming"))
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("Upcoming Event")
            .setContentText("${event.name} at ${event.beginTime}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(1001, builder.build())

        Log.d(TAG, "Notifikasi dikirim dengan event_id: ${event.id}")
    }

    private fun shouldShowReminder(eventDate: String): Boolean {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val eventTime = formatter.parse(eventDate)?.time ?: return false
            eventTime > System.currentTimeMillis() // Hanya event yang belum lewat
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
