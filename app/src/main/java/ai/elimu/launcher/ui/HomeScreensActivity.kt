package ai.elimu.launcher.ui

import ai.elimu.common.utils.ui.setLightStatusBar
import ai.elimu.common.utils.ui.setStatusBarColorCompat
import ai.elimu.launcher.BuildConfig
import ai.elimu.launcher.R
import ai.elimu.launcher.databinding.ActivityHomeScreensBinding
import ai.elimu.launcher.util.CursorToApplicationConverter
import ai.elimu.model.v2.enums.content.LiteracySkill
import ai.elimu.model.v2.enums.content.NumeracySkill
import ai.elimu.model.v2.gson.application.ApplicationGson
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.afollestad.materialdialogs.GravityEnum
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
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
        mSectionsPagerAdapter = SectionsPagerAdapter(this)

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

    class PlaceholderFragment : Fragment() {
        // Skill: Tablet navigation
        private var tabletNavigationImageView: ImageView? = null


        // Skills: EGRA
        private var egraOralVocabularyContainer: ConstraintLayout? = null
        private var egraOralVocabularyImageView: ImageView? = null

        private var egraPhonemicAwarenessContainer: ConstraintLayout? = null
        private var egraPhonemicAwarenessImageView: ImageView? = null

        private var egraLetterIdentificationImageView: ImageView? = null

        private var egraSyllableNamingImageView: ImageView? = null


        // Skills: EGMA
        private var egmaOralCountingContainer: ConstraintLayout? = null
        private var egmaOralCountingImageView: ImageView? = null

        private var egmaNumberIdentificationImageView: ImageView? = null

        private var egmaMissingNumberImageView: ImageView? = null


        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View? {
            Timber.i("onCreateView")

            val sectionNumber = arguments?.getInt(ARG_SECTION_NUMBER)
            Timber.i("sectionNumber: $sectionNumber")

            val layoutIdentifier = resources.getIdentifier(
                "fragment_home_screen$sectionNumber",
                "layout",
                activity?.packageName
            )
            val rootView = inflater.inflate(layoutIdentifier, container, false)

            if (sectionNumber == 1) {
                // 1. Tablet navigation

                tabletNavigationImageView = rootView.findViewById(R.id.tabletNavigationImageView)
                tabletNavigationImageView?.setOnClickListener {
                    Timber.i("tabletNavigationImageView onClick")
                    // Tablet Navigation
                    //                        initializeDialog(null, null);
                    initializeDialog(null, NumeracySkill.SHAPE_IDENTIFICATION)
                }


                // 2. EGRA skills
                egraOralVocabularyContainer =
                    rootView.findViewById(R.id.egraOralVocabularyContainer)
                egraOralVocabularyImageView =
                    rootView.findViewById(R.id.egraOralVocabularyImageView)
                egraOralVocabularyImageView?.setOnClickListener {
                    Timber.i("egraOralVocabularyImageView onClick")
                    // Oral Vocabulary and Listening Comprehension
                    // TODO: include LiteracySkill.LISTENING_COMPREHENSION
                    initializeDialog(LiteracySkill.ORAL_VOCABULARY, null)
                }

                egraPhonemicAwarenessContainer =
                    rootView.findViewById(R.id.egraPhonemicAwarenessContainer)
                egraPhonemicAwarenessImageView =
                    rootView.findViewById(R.id.egraPhonemicAwarenessImageView)
                egraPhonemicAwarenessImageView?.setOnClickListener {
                    Timber.i("egraPhonemicAwarenessImageView onClick")
                    // Phonemic Awareness
                    initializeDialog(LiteracySkill.PHONEMIC_AWARENESS, null)
                }

                egraLetterIdentificationImageView =
                    rootView.findViewById(R.id.egraLetterIdentificationImageView)
                egraLetterIdentificationImageView?.setOnClickListener {
                    Timber.i("egraLetterIdentificationImageView onClick")
                    // Letter Identification
                    initializeDialog(LiteracySkill.LETTER_IDENTIFICATION, null)
                }


                // 3. EGMA skills
                egmaOralCountingContainer = rootView.findViewById(R.id.egmaOralCountingContainer)
                egmaOralCountingImageView = rootView.findViewById(R.id.egmaOralCountingImageView)
                egmaOralCountingImageView?.setOnClickListener {
                    Timber.i("egmaOralCountingImageView onClick")
                    // Oral Counting
                    initializeDialog(null, NumeracySkill.ORAL_COUNTING)
                }

                egmaNumberIdentificationImageView =
                    rootView.findViewById(R.id.egmaNumberIdentificationImageView)
                egmaNumberIdentificationImageView?.setOnClickListener {
                    Timber.i("egmaNumberIdentificationImageView onClick")
                    // Number Identification
                    initializeDialog(null, NumeracySkill.NUMBER_IDENTIFICATION)
                }

                egmaMissingNumberImageView = rootView.findViewById(R.id.egmaMissingNumberImageView)
                egmaMissingNumberImageView?.setOnClickListener {
                    Timber.i("egmaMissingNumberImageView onClick")
                    // Missing Number and Quantity Discrimination
                    initializeDialog(null, NumeracySkill.MISSING_NUMBER)
                }
            } else if (sectionNumber == 2) {
                // 1. EGRA skills

                egraSyllableNamingImageView =
                    rootView.findViewById(R.id.egraSyllableNamingImageView)
                egraSyllableNamingImageView?.setOnClickListener {
                    Timber.i("egraSyllableNamingImageView onClick")
                    // Syllable Naming and Familiar Word Reading
                    initializeDialog(LiteracySkill.FAMILIAR_WORD_READING, null)
                }


                // 2. EGMA skills

                // TODO
            } else if (sectionNumber == 3) {
                // TODO
            } else if (sectionNumber == 4) {
                // TODO
            }

            return rootView
        }

        private fun initializeDialog(literacySkill: LiteracySkill?, numeracySkill: NumeracySkill?) {
            Timber.i("initializeDialog")

            var dialogTitle: String? = null
            if (false) { // TODO
                dialogTitle = "TABLET_NAVIGATION" // TODO
            } else if (literacySkill != null) {
                dialogTitle = literacySkill.toString()
            } else if (numeracySkill != null) {
                dialogTitle = numeracySkill.toString()
            }

            val materialDialog = MaterialDialog.Builder(requireContext())
                .customView(R.layout.dialog_apps, true)
                .theme(Theme.DARK)
                .title(dialogTitle ?: "")
                .titleGravity(GravityEnum.CENTER)
                .show()

            val customView = materialDialog.customView
            val appGridLayout = customView!!.findViewById<GridLayout>(R.id.appGridLayout)

            for (application in applications) {
                Timber.i("application.getPackageName(): %s", application.packageName)
                val isTabletNavigationSkill = false // TODO
                Timber.i("isTabletNavigationSkill: $isTabletNavigationSkill")
                val isLiteracySkill = application.literacySkills.contains(literacySkill)
                Timber.i("isLiteracySkill: $isLiteracySkill")
                val isNumeracySkill = application.numeracySkills.contains(numeracySkill)
                Timber.i("isNumeracySkill: $isNumeracySkill")
                if (isTabletNavigationSkill || isLiteracySkill || isNumeracySkill) {
                    // Add Application to dialog

                    // Check if the Application is already installed. If not, skip it.

                    try {
                        val packageInfoAppstore =
                            requireContext().packageManager.getPackageInfo(application.packageName, 0)
                        Timber.i("packageInfoAppstore.versionCode: %s", packageInfoAppstore.versionCode)
                    } catch (e: PackageManager.NameNotFoundException) {
                        Timber.i(
                            e,
                            "The Application has not been installed: %s", application.packageName
                        )
                        continue
                    }

                    val appView = LayoutInflater.from(activity)
                        .inflate(R.layout.dialog_apps_app_view, appGridLayout, false)

                    // Set app icon
                    val appIconImageView = appView.findViewById<ImageView>(R.id.appIconImageView)
                    val packageManager = activity?.packageManager ?: return
                    try {
                        val applicationInfo = packageManager.getApplicationInfo(
                            application.packageName,
                            PackageManager.GET_META_DATA
                        )
                        val resources =
                            packageManager.getResourcesForApplication(application.packageName)
                        val icon = resources.getDrawableForDensity(
                            applicationInfo.icon,
                            DisplayMetrics.DENSITY_XXHIGH,
                            null
                        )
                        appIconImageView.setImageDrawable(icon)
                    } catch (e: PackageManager.NameNotFoundException) {
                        Timber.e(e)
                    }

                    // Open Application when pressing app icon
                    appIconImageView.setOnClickListener {
                        Timber.i("appIconImageView onClick")
                        val intent =
                            packageManager.getLaunchIntentForPackage(application.packageName)
                        intent!!.addCategory(Intent.CATEGORY_LAUNCHER)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                        startActivity(intent)
                        //                            EventTracker.reportApplicationOpenedEvent(getContext(), activityInfo.packageName);
                    }

                    appGridLayout.addView(appView)
                }
            }
        }

        override fun onStart() {
            Timber.i("onCreateView")
            super.onStart()
        }

        override fun onResume() {
            Timber.i("onResume")
            super.onResume()

            egraOralVocabularyContainer?.let { container ->
                // Add subtle movements to the space ships
                val objectAnimatorEGRA = ObjectAnimator.ofFloat(
                    container,
                    "rotation",
                    (2 + (Math.random().toInt() * 3)).toFloat()
                )
                objectAnimatorEGRA.setDuration((1000 + (Math.random().toInt() * 1000)).toLong())
                objectAnimatorEGRA.repeatCount = ValueAnimator.INFINITE
                objectAnimatorEGRA.repeatMode = ValueAnimator.REVERSE
                objectAnimatorEGRA.start()
            }

            egraPhonemicAwarenessContainer?.let { container ->
                val objectAnimatorEgmaPhonemicAwareness = ObjectAnimator.ofFloat(
                    container,
                    "rotation",
                    (2 + (Math.random().toInt() * 3)).toFloat()
                )
                objectAnimatorEgmaPhonemicAwareness.setDuration(
                    (1000 + (Math.random().toInt() * 1000)).toLong()
                )
                objectAnimatorEgmaPhonemicAwareness.repeatCount = ValueAnimator.INFINITE
                objectAnimatorEgmaPhonemicAwareness.repeatMode = ValueAnimator.REVERSE
                objectAnimatorEgmaPhonemicAwareness.start()
            }

            egmaOralCountingContainer?.let { container ->
                val objectAnimatorEGMA = ObjectAnimator.ofFloat(
                    container,
                    "rotation",
                    (2 + (Math.random().toInt() * 3)).toFloat()
                )
                objectAnimatorEGMA.setDuration((1000 + (Math.random().toInt() * 1000)).toLong())
                objectAnimatorEGMA.repeatCount = ValueAnimator.INFINITE
                objectAnimatorEGMA.repeatMode = ValueAnimator.REVERSE
                objectAnimatorEGMA.start()
            }
        }

        companion object {
            private const val ARG_SECTION_NUMBER = "section_number"


            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun createFragment(position: Int): Fragment {
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getItemCount(): Int {
            return 4
        }
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
