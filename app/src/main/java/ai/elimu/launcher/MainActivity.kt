package ai.elimu.launcher

import ai.elimu.launcher.ui.HomeScreensActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(applicationContext, HomeScreensActivity::class.java)
        startActivity(intent)

        finish()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        Timber.i("onBackPressed")

        // Do nothing
    }
}
