package ai.elimu.launcher.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ai.elimu.launcher.BuildConfig;
import ai.elimu.launcher.R;
import ai.elimu.launcher.util.CursorToApplicationConverter;
import ai.elimu.model.enums.content.LiteracySkill;
import ai.elimu.model.enums.content.NumeracySkill;
import ai.elimu.model.v2.gson.application.ApplicationGson;
import timber.log.Timber;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class HomeScreensActivity extends AppCompatActivity {

    private static final String APPSTORE_DOWNLOAD_URL = "https://github.com/elimu-ai/appstore/releases";

    public static final double WIDTH_INCREMENT = 0.1;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private View background;

    private ViewPager2 viewPager;

    private DotIndicator dotIndicator;

    private static List<ApplicationGson> applications;

    private boolean isRightToLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screens);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        // Check if the elimu.ai Appstore is already installed
        checkIfAppstoreIsInstalled();

        // Create the adapter that will return a fragment for each of the primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this);

        // Set up the ViewPager with the sections adapter.
        viewPager = findViewById(R.id.container);
        viewPager.setAdapter(mSectionsPagerAdapter);

        dotIndicator = findViewById(R.id.dotIndicator);

        background = findViewById(R.id.background);

        isRightToLeft = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL;

        //Increase the width of the background image to apply the parallax effect
        updateBackgroundImageWidth();

        final int pageTranslation = (int) (getDisplayWidth() * WIDTH_INCREMENT / (mSectionsPagerAdapter.getItemCount() -1));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Timber.i("onPageScrolled");

                if (positionOffset >= -1 && positionOffset <= 1) {
                    int translationX = - (int) ((position + positionOffset) * pageTranslation);

                    if (isRightToLeft) {
                        translationX = - (int) (translationX + getDisplayWidth() * WIDTH_INCREMENT);
                    }

                    background.setTranslationX(translationX);
                }
            }

            @Override
            public void onPageSelected(int position) {
                Timber.i("onPageSelected");

                if (isRightToLeft) {
                    dotIndicator.setSelectedItem(dotIndicator.getNumberOfItems() - 1 - position, true);
                } else {
                    dotIndicator.setSelectedItem(position, true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Timber.i("onPageScrollStateChanged");
            }
        });

        // Fetch Applications from the Appstore's ContentProvider
        applications = new ArrayList<>();
        Uri uri = Uri.parse("content://" + BuildConfig.APPSTORE_APPLICATION_ID + ".provider.application_provider/applications");
        Timber.i("uri: " + uri);
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            Timber.i("cursor.getCount(): " + cursor.getCount());
            if (cursor.getCount() > 0) {
                boolean isLast = false;
                while (!isLast) {
                    cursor.moveToNext();

                    // Convert from database row to Gson
                    ApplicationGson application = CursorToApplicationConverter.getApplication(cursor);

                    applications.add(application);

                    isLast = cursor.isLast();
                }
                Timber.i("cursor.isClosed(): " + cursor.isClosed());
                cursor.close();
            } else {
                Toast.makeText(getApplicationContext(), "cursor.getCount() == 0", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "cursor == null", Toast.LENGTH_LONG).show();
        }
        Timber.i("applications.size(): " + applications.size());
    }

    private void checkIfAppstoreIsInstalled() {
        try {
            PackageInfo packageInfoAppstore = getPackageManager().getPackageInfo(BuildConfig.APPSTORE_APPLICATION_ID, 0);
            Timber.i("packageInfoAppstore.versionCode: " + packageInfoAppstore.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            Timber.w(null, e);
            new MaterialDialog.Builder(this)
                    .content(getResources().getString(R.string.appstore_needed) + BuildConfig.APPSTORE_APPLICATION_ID)
                    .positiveText(getResources().getString(R.string.download))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            goToAppstoreDownload();
                        }
                    })
                    .show();
        }
    }

    private void goToAppstoreDownload() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(APPSTORE_DOWNLOAD_URL));
        startActivity(intent);
    }

    private void updateBackgroundImageWidth() {
        int backgroundWidth = (int) (getDisplayWidth() * (1 + WIDTH_INCREMENT));
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(backgroundWidth, MATCH_PARENT);
        background.setLayoutParams(layoutParams);
        if (isRightToLeft) {
            background.setTranslationX(-(float) (getDisplayWidth() * WIDTH_INCREMENT));
        }
    }

    private int getDisplayWidth() {
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        return displaySize.x;
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";


        // Skill: Tablet navigation

        private ConstraintLayout tabletNavigationContainer;
        private ImageView tabletNavigationImageView;


        // Skills: EGRA

        private ConstraintLayout egraOralVocabularyContainer;
        private ImageView egraOralVocabularyImageView;

        private ConstraintLayout egraPhonemicAwarenessContainer;
        private ImageView egraPhonemicAwarenessImageView;

        private ConstraintLayout egraLetterIdentificationContainer;
        private ImageView egraLetterIdentificationImageView;

        private ConstraintLayout egraSyllableNamingContainer;
        private ImageView egraSyllableNamingImageView;


        // Skills: EGMA

        private ConstraintLayout egmaOralCountingContainer;
        private ImageView egmaOralCountingImageView;

        private ConstraintLayout egmaNumberIdentificationContainer;
        private ImageView egmaNumberIdentificationImageView;

        private ConstraintLayout egmaMissingNumberContainer;
        private ImageView egmaMissingNumberImageView;


        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Timber.i("onCreateView");

            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            Timber.i("sectionNumber: " + sectionNumber);

            int layoutIdentifier = getResources().getIdentifier("fragment_home_screen" + String.valueOf(sectionNumber), "layout", getActivity().getPackageName());
            View rootView = inflater.inflate(layoutIdentifier, container, false);

            if (sectionNumber == 1) {
                // 1. Tablet navigation

                tabletNavigationContainer = rootView.findViewById(R.id.tabletNavigationContainer);
                tabletNavigationImageView = rootView.findViewById(R.id.tabletNavigationImageView);
                tabletNavigationImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Timber.i("tabletNavigationImageView onClick");

                        // Tablet Navigation
//                        initializeDialog(null, null);
                        initializeDialog(null, NumeracySkill.SHAPE_IDENTIFICATION);
                    }
                });


                // 2. EGRA skills

                egraOralVocabularyContainer = rootView.findViewById(R.id.egraOralVocabularyContainer);
                egraOralVocabularyImageView = rootView.findViewById(R.id.egraOralVocabularyImageView);
                egraOralVocabularyImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Timber.i("egraOralVocabularyImageView onClick");

                        // Oral Vocabulary and Listening Comprehension
                        // TODO: include LiteracySkill.LISTENING_COMPREHENSION
                        initializeDialog(LiteracySkill.ORAL_VOCABULARY, null);
                    }
                });

                egraPhonemicAwarenessContainer = rootView.findViewById(R.id.egraPhonemicAwarenessContainer);
                egraPhonemicAwarenessImageView = rootView.findViewById(R.id.egraPhonemicAwarenessImageView);
                egraPhonemicAwarenessImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Timber.i("egraPhonemicAwarenessImageView onClick");

                        // Phonemic Awareness
                        initializeDialog(LiteracySkill.PHONEMIC_AWARENESS, null);
                    }
                });

                egraLetterIdentificationContainer = rootView.findViewById(R.id.egraLetterIdentificationContainer);
                egraLetterIdentificationImageView = rootView.findViewById(R.id.egraLetterIdentificationImageView);
                egraLetterIdentificationImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Timber.i("egraLetterIdentificationImageView onClick");

                        // Letter Identification
                        initializeDialog(LiteracySkill.LETTER_IDENTIFICATION, null);
                    }
                });


                // 3. EGMA skills

                egmaOralCountingContainer = rootView.findViewById(R.id.egmaOralCountingContainer);
                egmaOralCountingImageView = rootView.findViewById(R.id.egmaOralCountingImageView);
                egmaOralCountingImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Timber.i("egmaOralCountingImageView onClick");

                        // Oral Counting
                        initializeDialog(null, NumeracySkill.ORAL_COUNTING);
                    }
                });

                egmaNumberIdentificationContainer = rootView.findViewById(R.id.egmaNumberIdentificationContainer);
                egmaNumberIdentificationImageView = rootView.findViewById(R.id.egmaNumberIdentificationImageView);
                egmaNumberIdentificationImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Timber.i("egmaNumberIdentificationImageView onClick");

                        // Number Identification
                        initializeDialog(null, NumeracySkill.NUMBER_IDENTIFICATION);
                    }
                });

                egmaMissingNumberContainer = rootView.findViewById(R.id.egmaMissingNumberContainer);
                egmaMissingNumberImageView = rootView.findViewById(R.id.egmaMissingNumberImageView);
                egmaMissingNumberImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Timber.i("egmaMissingNumberImageView onClick");

                        // Missing Number and Quantity Discrimination
                        initializeDialog(null, NumeracySkill.MISSING_NUMBER);
                    }
                });
            } else if (sectionNumber == 2) {
                // 1. EGRA skills

                egraSyllableNamingContainer = rootView.findViewById(R.id.egraSyllableNamingContainer);
                egraSyllableNamingImageView = rootView.findViewById(R.id.egraSyllableNamingImageView);
                egraSyllableNamingImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Timber.i("egraSyllableNamingImageView onClick");

                        // Syllable Naming and Familiar Word Reading
                        initializeDialog(LiteracySkill.FAMILIAR_WORD_READING, null);
                    }
                });


                // 2. EGMA skills

                // TODO
            } else if (sectionNumber == 3) {
                // TODO
            }  else if (sectionNumber == 4) {
                // TODO
            }

            return rootView;
        }

        private void initializeDialog(LiteracySkill literacySkill, NumeracySkill numeracySkill) {
            Timber.i("initializeDialog");

            String dialogTitle = null;
            if (false) { // TODO
                dialogTitle = "TABLET_NAVIGATION"; // TODO
            } else if (literacySkill != null) {
                dialogTitle = literacySkill.toString();
            } else if (numeracySkill != null) {
                dialogTitle = numeracySkill.toString();
            }

            MaterialDialog materialDialog = new MaterialDialog.Builder(getContext())
                    .customView(R.layout.dialog_apps, true)
                    .theme(Theme.DARK)
                    .title(dialogTitle)
                    .titleGravity(GravityEnum.CENTER)
                    .show();

            View customView = materialDialog.getCustomView();
            GridLayout appGridLayout = customView.findViewById(R.id.appGridLayout);

            for (final ApplicationGson application : applications) {
                Timber.i("application.getPackageName(): " + application.getPackageName());
                boolean isTabletNavigationSkill = false; // TODO
                Timber.i("isTabletNavigationSkill: " + isTabletNavigationSkill);
                boolean isLiteracySkill = application.getLiteracySkills().contains(literacySkill);
                Timber.i("isLiteracySkill: " + isLiteracySkill);
                boolean isNumeracySkill = application.getNumeracySkills().contains(numeracySkill);
                Timber.i("isNumeracySkill: " + isNumeracySkill);
                if (isTabletNavigationSkill || isLiteracySkill || isNumeracySkill) {
                    // Add Application to dialog

                    // Check if the Application is already installed. If not, skip it.
                    try {
                        PackageInfo packageInfoAppstore = getContext().getPackageManager().getPackageInfo(application.getPackageName(), 0);
                        Timber.i( "packageInfoAppstore.versionCode: " + packageInfoAppstore.versionCode);
                    } catch (PackageManager.NameNotFoundException e) {
                        Timber.i(e, "The Application has not been installed: " + application.getPackageName());
                        continue;
                    }

                    View appView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_apps_app_view, appGridLayout, false);

                    // Set app icon
                    ImageView appIconImageView = appView.findViewById(R.id.appIconImageView);
                    final PackageManager packageManager = getActivity().getPackageManager();
                    try {
                        ApplicationInfo applicationInfo = packageManager.getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
                        Resources resources = packageManager.getResourcesForApplication(application.getPackageName());
                        Drawable icon = resources.getDrawableForDensity(applicationInfo.icon, DisplayMetrics.DENSITY_XXHIGH, null);
                        appIconImageView.setImageDrawable(icon);
                    } catch (PackageManager.NameNotFoundException e) {
                        Timber.e(e);
                    }

                    // Open Application when pressing app icon
                    appIconImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Timber.i("appIconImageView onClick");

                            Intent intent = packageManager.getLaunchIntentForPackage(application.getPackageName());
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            startActivity(intent);

//                            EventTracker.reportApplicationOpenedEvent(getContext(), activityInfo.packageName);
                        }
                    });

                    appGridLayout.addView(appView);
                }
            }
        }

        @Override
        public void onStart() {
            Timber.i("onCreateView");
            super.onStart();
        }

        @Override
        public void onResume() {
            Timber.i("onResume");
            super.onResume();

            // Add subtle movements to the space ships

            ObjectAnimator objectAnimatorEGRA = ObjectAnimator.ofFloat(egraOralVocabularyContainer, "rotation", 2 + ((int) Math.random() * 3));
            objectAnimatorEGRA.setDuration(1000 + ((int) Math.random() * 1000));
            objectAnimatorEGRA.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimatorEGRA.setRepeatMode(ValueAnimator.REVERSE);
            objectAnimatorEGRA.start();

            ObjectAnimator objectAnimatorEgmaPhonemicAwareness = ObjectAnimator.ofFloat(egraPhonemicAwarenessContainer, "rotation", 2 + ((int) Math.random() * 3));
            objectAnimatorEgmaPhonemicAwareness.setDuration(1000 + ((int) Math.random() * 1000));
            objectAnimatorEgmaPhonemicAwareness.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimatorEgmaPhonemicAwareness.setRepeatMode(ValueAnimator.REVERSE);
            objectAnimatorEgmaPhonemicAwareness.start();

            ObjectAnimator objectAnimatorEGMA = ObjectAnimator.ofFloat(egmaOralCountingContainer, "rotation", 2 + ((int) Math.random() * 3));
            objectAnimatorEGMA.setDuration(1000 + ((int) Math.random() * 1000));
            objectAnimatorEGMA.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimatorEGMA.setRepeatMode(ValueAnimator.REVERSE);
            objectAnimatorEGMA.start();
        }
    }

    public static class SectionsPagerAdapter extends FragmentStateAdapter {

        public SectionsPagerAdapter(AppCompatActivity activity) {
            super(activity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

    @Override
    public void onBackPressed() {
        Timber.i("onBackPressed");

        // Do nothing
    }
}
