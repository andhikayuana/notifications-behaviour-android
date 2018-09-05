package id.yuana.notification.demo

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

/**
 * @author Yuana andhikayuana@gmail.com
 * @since Sep, Wed 05 2018 10.08
 **/
class App : Application() {

    companion object {
        lateinit var instance: App
    }

    lateinit var cache: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        instance = this
        cache = getSharedPreferences("${BuildConfig.APPLICATION_ID}.cache", Context.MODE_PRIVATE)
    }
}