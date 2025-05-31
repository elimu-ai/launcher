package ai.elimu.launcher.ui

import ai.elimu.launcher.R
import ai.elimu.model.v2.enums.content.LiteracySkill
import ai.elimu.model.v2.enums.content.NumeracySkill
import ai.elimu.model.v2.gson.application.ApplicationGson
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.GravityEnum
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import timber.log.Timber

class PlaceHolderFragment : Fragment() {

    private val apps: MutableList<ApplicationGson> = mutableListOf()

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
        val spacing = requireContext().resources.getDimension(R.dimen.dialog_view_app_item_space).toInt()

        for (application in apps) {
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
                        0,
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
                    intent?.let {
                        intent.addCategory(Intent.CATEGORY_LAUNCHER)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                        startActivity(intent)
                    } ?: Timber.w("No launch intent found for package: ${application.packageName}")

                }
                val columnCount = appGridLayout.columnCount
                val pos = appGridLayout.childCount
                val row = pos / columnCount
                val column = pos % columnCount

                val gridLayoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(row)
                    columnSpec = GridLayout.spec(column, 1f)
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    setMargins(spacing, spacing, spacing, spacing)
                }

                appGridLayout.addView(appView, gridLayoutParams)
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
                (2 + (Math.random() * 3)).toFloat()
            )
            objectAnimatorEGRA.setDuration((1000 + (Math.random() * 1000)).toLong())
            objectAnimatorEGRA.repeatCount = ValueAnimator.INFINITE
            objectAnimatorEGRA.repeatMode = ValueAnimator.REVERSE
            objectAnimatorEGRA.start()
        }

        egraPhonemicAwarenessContainer?.let { container ->
            val objectAnimatorEgmaPhonemicAwareness = ObjectAnimator.ofFloat(
                container,
                "rotation",
                (2 + (Math.random() * 3)).toFloat()
            )
            objectAnimatorEgmaPhonemicAwareness.setDuration(
                (1000 + (Math.random() * 1000)).toLong()
            )
            objectAnimatorEgmaPhonemicAwareness.repeatCount = ValueAnimator.INFINITE
            objectAnimatorEgmaPhonemicAwareness.repeatMode = ValueAnimator.REVERSE
            objectAnimatorEgmaPhonemicAwareness.start()
        }

        egmaOralCountingContainer?.let { container ->
            val objectAnimatorEGMA = ObjectAnimator.ofFloat(
                container,
                "rotation",
                (2 + (Math.random() * 3)).toFloat()
            )
            objectAnimatorEGMA.setDuration((1000 + (Math.random() * 1000)).toLong())
            objectAnimatorEGMA.repeatCount = ValueAnimator.INFINITE
            objectAnimatorEGMA.repeatMode = ValueAnimator.REVERSE
            objectAnimatorEGMA.start()
        }
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"


        fun newInstance(sectionNumber: Int, apps: MutableList<ApplicationGson>): PlaceHolderFragment {
            val fragment = PlaceHolderFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            fragment.apps.addAll(apps)
            return fragment
        }
    }
}