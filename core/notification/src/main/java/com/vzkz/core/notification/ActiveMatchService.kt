package com.vzkz.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.vzkz.core.presentation.ui.formatted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import kotlin.getValue
import kotlin.time.Duration

class ActiveMatchService : Service() {

    private val notificationManager by lazy {
        getSystemService<NotificationManager>()!!
    }

    private val baseNotification by lazy {
        NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(com.vzkz.core.presentation.designsystem.R.drawable.logo_no_bg)
//            .setContentTitle(getString(R.string.active_match))
            .setContentTitle(getString(com.vzkz.core.presentation.ui.R.string.active_match))
    }

    private val elapsedTime by inject<StateFlow<Duration>>()

    private var serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val activityClass = intent.getStringExtra(EXTRA_ACTIVITY_CLASS)
                    ?: throw IllegalArgumentException("No activity class provided")
                start(Class.forName(activityClass))
            }

            ACTION_STOP -> stop()
        }
        return START_STICKY
    }

    private fun start(activityClass: Class<*>) {
        if (!_isServiceActive.value) {
            _isServiceActive.value = true
            createNotificationChannel()

            val activityIntent = Intent(applicationContext, activityClass).apply {
                data = "beepadel://active_match".toUri()
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            val pendingIntent = TaskStackBuilder.create(applicationContext).run {
                addNextIntentWithParentStack(activityIntent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }
            val notification = baseNotification
                .setContentText("00:00:00")
                .setContentIntent(pendingIntent)
                .build()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(
                    /* id = */ NOTIFICATION_ID,
                    /* notification = */ notification,
                    /* foregroundServiceType = */
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                    else
                        0

                )
            } else {
                startForeground(
                    /* id = */ NOTIFICATION_ID,
                    /* notification = */ notification,
                )
            }
            updateNotification()
        }
    }

    private fun updateNotification() {
       elapsedTime
            .onEach { elapsedTime ->
                val notification = baseNotification
                    .setContentText(elapsedTime.formatted())
                    .build()
                notificationManager.notify(1, notification)
            }
            .launchIn(serviceScope)
    }

    fun stop() {
        stopSelf()
        _isServiceActive.value = false
        serviceScope.cancel()

        serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(com.vzkz.core.presentation.ui.R.string.active_match),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                enableVibration(false)
                setSound(null, null)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private val _isServiceActive = MutableStateFlow(false)
        val isServiceActive = _isServiceActive.asStateFlow()
        private const val CHANNEL_ID = "active_match"
        private const val NOTIFICATION_ID = 1

        private const val ACTION_START = "ACTION_START"
        private const val ACTION_STOP = "ACTION_STOP"

        private const val EXTRA_ACTIVITY_CLASS = "EXTRA_ACTIVITY_CLASS"

        fun createStartIntent(context: Context, activityClass: Class<*>): Intent {
            return Intent(context, ActiveMatchService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_ACTIVITY_CLASS, activityClass.name)
            }
        }

        fun createStopIntent(context: Context): Intent {
            return Intent(context, ActiveMatchService::class.java).apply {
                action = ACTION_STOP
            }
        }
    }
}