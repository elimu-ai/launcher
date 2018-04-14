package ai.elimu.launcher;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.andraskindler.parallaxviewpager.ParallaxViewPager;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;

import java.util.ArrayList;
import java.util.List;

import ai.elimu.launcher.util.CursorToApplicationConverter;
import ai.elimu.model.enums.content.LiteracySkill;
import ai.elimu.model.enums.content.NumeracySkill;
import ai.elimu.model.gson.admin.ApplicationGson;
import timber.log.Timber;

//import ai.elimu.analytics.eventtracker.EventTracker;

public class HomeScreensActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ParallaxViewPager viewPager;

    private DotIndicator dotIndicator;

    private static List<ApplicationGson> applications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screens);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        // Create the adapter that will return a fragment for each of the primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = findViewById(R.id.container);
        viewPager.setBackgroundResource(R.drawable.background);
        viewPager.setAdapter(mSectionsPagerAdapter);

        dotIndicator = findViewById(R.id.dotIndicator);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Timber.i("onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                Timber.i("onPageSelected");

                dotIndicator.setSelectedItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Timber.i("onPageScrollStateChanged");
            }
        });

        // Fetch Applications from the Appstore's ContentProvider
        applications = new ArrayList<>();
        Uri uri = Uri.parse("content://" + BuildConfig.APPSTORE_APPLICATION_ID + ".provider/application");
        Timber.i("uri: " + uri);
        Cursor cursor = getContentResolver(). query(uri, null, null, null, null);
        if (cursor != null) {
            Timber.i("cursor.getCount(): " + cursor.getCount());
            if (cursor.getCount() > 0) {
                boolean isLast = false;
                while (!isLast) {
                    cursor.moveToNext();

                    // Convert from database row to ApplicationGson object
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

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";


        // Skill: Tablet navigation

        private RelativeLayout tabletNavigationContainer;
        private ImageView tabletNavigationImageView;


        // Skills: EGRA

        private RelativeLayout egraOralVocabularyContainer;
        private ImageView egraOralVocabularyImageView;

        private RelativeLayout egraPhonemicAwarenessContainer;
        private ImageView egraPhonemicAwarenessImageView;

        private RelativeLayout egraLetterIdentificationContainer;
        private ImageView egraLetterIdentificationImageView;

        private RelativeLayout egraSyllableNamingContainer;
        private ImageView egraSyllableNamingImageView;


        // Skills: EGMA

        private RelativeLayout egmaOralCountingContainer;
        private ImageView egmaOralCountingImageView;

        private RelativeLayout egmaNumberIdentificationContainer;
        private ImageView egmaNumberIdentificationImageView;

        private RelativeLayout egmaMissingNumberContainer;
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
                        initializeDialog(null, null);
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

            MaterialDialog materialDialog = new MaterialDialog.Builder(getContext())
                    .customView(R.layout.dialog_apps, true)
                    .theme(Theme.DARK)
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

                    View appView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_apps_app_view, appGridLayout, false);

                    // Set app icon
                    ImageView appIconImageView = appView.findViewById(R.id.appIconImageView);
                    final PackageManager packageManager = getActivity().getPackageManager();
                    try {
                        ApplicationInfo applicationInfo = packageManager.getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
                        Resources resources = packageManager.getResourcesForApplication(application.getPackageName());
                        Drawable icon;
                        if (Build.VERSION.SDK_INT >= 22) {
                            icon = resources.getDrawableForDensity(applicationInfo.icon, DisplayMetrics.DENSITY_XXHIGH, null);
                        } else {
                            // This method was deprecated in API level 22
                            icon = resources.getDrawableForDensity(applicationInfo.icon, DisplayMetrics.DENSITY_XXHIGH);
                        }
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "SECTION 4";
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        Timber.i("onBackPressed");

        // Do nothing
    }
}
