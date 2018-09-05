package id.yuana.notification.demo.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import id.yuana.notification.demo.App
import id.yuana.notification.demo.BuildConfig
import id.yuana.notification.demo.R
import id.yuana.notification.demo.model.NotificationMessage
import id.yuana.notification.demo.ui.MainActivity


/**
 * @author Yuana andhikayuana@gmail.com
 * @since Sep, Wed 05 2018 10.08
 **/
object NotificationUtil {

    private const val NOTIFICATION_CHANNEL_ID: String = "${BuildConfig.APPLICATION_ID}.notification.channel"

    fun show(context: Context, notificationMessage: NotificationMessage) = NotificationManagerCompat.from(context)
            .notify(notificationMessage.id, createNotificationBuilder(context, notificationMessage).build())


    private fun createNotificationBuilder(context: Context, notificationMessage: NotificationMessage): NotificationCompat.Builder {

        val ringtoneUri = getRingtoneUri()

        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.apply {
            setContentTitle(notificationMessage.title)
            setContentText(notificationMessage.message)
            setTicker(notificationMessage.message)
            setSmallIcon(R.mipmap.ic_launcher)
            setLargeIcon(getDrawable(context))
            color = ContextCompat.getColor(context, R.color.colorPrimary)
            setGroupSummary(true)
            setGroup("NOTIFICATION_${notificationMessage.roomId}")
            setAutoCancel(true)
            setSound(null)
            setLights(Color.GREEN, 500, 500)
            setVibrate(longArrayOf(100, 0, 100, 0))
            priority = NotificationCompat.PRIORITY_HIGH
            setContentIntent(createPendingIntent(context))
        }

        if (isOreoOrHigher()) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "Chat", importance)
            val att = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            channel.setSound(null, att)
            channel.enableLights(true)
            channel.lightColor = Color.GREEN
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 0, 100, 0)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }

        return notificationBuilder
    }

    private fun getRingtoneUri(): Uri? {
        val ringtone = App.instance.cache.getString(MainActivity.PREF_RINGTONE, null)
        return if (ringtone == null) RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) else Uri.parse(ringtone)
    }

    private fun createPendingIntent(context: Context): PendingIntent? {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    fun isOreoOrHigher() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    private fun getDrawable(context: Context): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, R.mipmap.ic_launcher)
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        var width = drawable!!.intrinsicWidth
        width = if (width > 0) width else 1
        var height = drawable.intrinsicHeight
        height = if (height > 0) height else 1

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }
}