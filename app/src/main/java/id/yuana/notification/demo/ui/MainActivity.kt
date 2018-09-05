package id.yuana.notification.demo.ui

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import id.yuana.notification.demo.App
import id.yuana.notification.demo.BuildConfig
import id.yuana.notification.demo.R
import id.yuana.notification.demo.model.NotificationMessage
import id.yuana.notification.demo.util.NotificationUtil
import id.yuana.notification.demo.util.edit
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val RC_RINGTONE_PICKER = 1
        const val PREF_RINGTONE = "PREF_RINGTONE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNotificationChannel.setOnClickListener { actionNotificationChannel() }
        btnChangeRingtone.setOnClickListener { actionChangeRingtone() }
        btnShowNotification.setOnClickListener { actionShowNotification() }

    }

    private fun actionNotificationChannel() {
        if (NotificationUtil.isOreoOrHigher()) {
            val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, "${BuildConfig.APPLICATION_ID}.notification.channel")
            startActivity(intent)
        }
    }

    private fun actionShowNotification() {
        NotificationUtil.show(this@MainActivity, createNotificationMessage())

    }

    private fun createNotificationMessage(): NotificationMessage =
            NotificationMessage(1, "Jarjit Messaging", "Halo", 12)


    private fun actionChangeRingtone() {
        val currentTone = RingtoneManager.getActualDefaultRingtoneUri(this@MainActivity, RingtoneManager.TYPE_ALARM)
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ringtone")
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
        startActivityForResult(intent, RC_RINGTONE_PICKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_RINGTONE_PICKER && resultCode == Activity.RESULT_OK) {
            val uri = data!!.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            App.instance.cache.edit {
                putString(PREF_RINGTONE, uri.toString())
            }
        }
    }
}
