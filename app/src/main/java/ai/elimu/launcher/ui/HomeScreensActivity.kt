package ai.elimu.launcher.ui

import ai.elimu.common.utils.ui.setLightStatusBar
import ai.elimu.common.utils.ui.setStatusBarColorCompat
import ai.elimu.launcher.BuildConfig
import ai.elimu.launcher.R
import ai.elimu.launcher.databinding.ActivityHomeScreensBinding
import ai.elimu.launcher.util.CursorToApplicationConverter
import ai.elimu.model.v2.gson.application.ApplicationGson
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.afollestad.materialdialogs.MaterialDialog
import timber.log.Timber
import java.util.Locale
import androidx.core.net.toUri
import androidx.core.text.layoutDirection

class HomeScreensActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeScreensBinding
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    private var isRightToLeft = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreensBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        // Check if the elimu.ai Appstore is already installed
        checkIfAppstoreIsInstalled()

        // Create the adapter that will return a fragment for each of the primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(this, applications)

        // Set up the ViewPager with the sections adapter.
        binding.container.setAdapter(mSectionsPagerAdapter)

        isRightToLeft =
            Locale.getDefault().layoutDirection == View.LAYOUT_DIRECTION_RTL

        //Increase the width of the background image to apply the parallax effect
        updateBackgroundImageWidth()

        val pageTranslation =
            (displayWidth * WIDTH_INCREMENT / (mSectionsPagerAdapter!!.itemCount - 1)).toInt()

        binding.container.let {
            it.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {
                    Timber.i("onPageScrolled")

                    if (positionOffset >= -1 && positionOffset <= 1) {
                        var translationX = -((position + positionOffset) * pageTranslation).toInt()

                        if (isRightToLeft) {
                            translationX = -(translationX + this@HomeScreensActivity.displayWidth * WIDTH_INCREMENT).toInt()
                        }

                        binding.background.translationX = translationX.toFloat()
                    }
                }

                override fun onPageSelected(position: Int) {
                    Timber.i("onPageSelected")
                }

                override fun onPageScrollStateChanged(state: Int) {
                    Timber.i("onPageScrollStateChanged")
                }
            })
            binding.dotIndicator.attachTo(it)
        }

        // Fetch Applications from the Appstore's ContentProvider
        val uri =
            ("content://" + BuildConfig.APPSTORE_APPLICATION_ID + ".provider.application_provider/applications").toUri()
        Timber.i("uri: $uri")
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            Timber.i("cursor.getCount(): %s", cursor.count)
            if (cursor.count > 0) {
                var isLast = false
                applications.clear()
                while (!isLast) {
                    cursor.moveToNext()

                    // Convert from database row to Gson
                    val application = CursorToApplicationConverter.getApplication(cursor)

                    applications.add(application)

                    isLast = cursor.isLast
                }
                Timber.i("cursor.isClosed(): %s", cursor.isClosed)
                cursor.close()
            } else {
                Toast.makeText(applicationContext, "cursor.getCount() == 0", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            Toast.makeText(applicationContext, "cursor == null", Toast.LENGTH_LONG).show()
        }
        Timber.i("applications.size(): %s", applications.size)

        window.apply {
            setLightStatusBar()
            setStatusBarColorCompat(R.color.colorPrimaryDark)
        }
    }

    private fun checkIfAppstoreIsInstalled() {
        try {
            val packageInfoAppstore =
                packageManager.getPackageInfo(BuildConfig.APPSTORE_APPLICATION_ID, 0)
            Timber.i("packageInfoAppstore.versionCode: %s", packageInfoAppstore.versionCode)
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e)
            MaterialDialog.Builder(this)
                .content(resources.getString(R.string.appstore_needed) + BuildConfig.APPSTORE_APPLICATION_ID)
                .positiveText(resources.getString(R.string.download))
                .onPositive { dialog, which ->
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        APPSTORE_DOWNLOAD_URL.toUri()
                    )
                    startActivity(intent)
                }
                .show()
        }
    }

    private fun updateBackgroundImageWidth() {
        val backgroundWidth = (displayWidth * (1 + WIDTH_INCREMENT)).toInt()
        val layoutParams =
            ConstraintLayout.LayoutParams(backgroundWidth, ViewGroup.LayoutParams.MATCH_PARENT)
        binding.background.layoutParams = layoutParams
        if (isRightToLeft) {
            binding.background.translationX =
                -(displayWidth * WIDTH_INCREMENT).toFloat()
        }
    }

    private val displayWidth: Int
        get() {
            val displaySize = Point()
            windowManager.defaultDisplay.getSize(displaySize)
            return displaySize.x
        }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        Timber.i("onBackPressed")

        // Do nothing
    }

    companion object {
        private const val APPSTORE_DOWNLOAD_URL = "https://github.com/elimu-ai/appstore/releases"

        const val WIDTH_INCREMENT: Double = 0.1

        private val applications: MutableList<ApplicationGson> by lazy { mutableListOf() }
    }
}
