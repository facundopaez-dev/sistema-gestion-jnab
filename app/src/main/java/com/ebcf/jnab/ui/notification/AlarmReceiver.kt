package com.ebcf.jnab.ui.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Charla"
        val id = intent.getIntExtra("id", 0)

        val notificationHelper = NotificationHelper(context)
        notificationHelper.showNotification(
            id,
            title,
            "Tu charla favorita comienza en 1 hora."
        )
    }
}
