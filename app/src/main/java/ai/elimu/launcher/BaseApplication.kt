package ai.elimu.launcher

import android.app.Application
import timber.log.Timber
import timber.log.Timber.Forest.plant

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Log config
        plant(Timber.DebugTree())
        Timber.d("onCreate")
    }
}
