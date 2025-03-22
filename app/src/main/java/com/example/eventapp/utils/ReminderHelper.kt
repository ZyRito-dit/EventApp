package com.example.eventapp.utils

import android.content.Context
import android.util.Log
import androidx.work.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object ReminderHelper {

    fun scheduleDailyReminder(context: Context, eventDate: String) {
        if (!shouldShowReminder(eventDate)) {
            Log.d("ReminderHelper", "Event sudah lewat, tidak menjadwalkan pengingat.")
            cancelDailyReminder(context)
            return
        }


        val delay = calculateDelayUntil9AM()

        val workRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            2, TimeUnit.MINUTES
        ).setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "DailyReminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        Log.d("ReminderHelper", "Pengingat harian dijadwalkan untuk event pada $eventDate.")
    }

    fun cancelDailyReminder(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork("DailyReminder")
        Log.d("ReminderHelper", "Pengingat harian dibatalkan.")
    }


    private fun shouldShowReminder(eventDate: String): Boolean {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val eventTime = formatter.parse(eventDate)?.time ?: return false
            eventTime > System.currentTimeMillis()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    private fun calculateDelayUntil9AM(): Long {
        val now = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }


        if (now.after(targetTime)) {
            targetTime.add(Calendar.DAY_OF_YEAR, 1)
        }

        return targetTime.timeInMillis - now.timeInMillis
    }
}
