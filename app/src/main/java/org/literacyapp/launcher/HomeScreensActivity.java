package org.literacyapp.launcher;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.andraskindler.parallaxviewpager.ParallaxViewPager;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;

import org.literacyapp.analytics.eventtracker.EventTracker;

import java.util.Arrays;
import java.util.List;

public class HomeScreensActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ParallaxViewPager viewPager;

    private DotIndicator dotIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screens);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ParallaxViewPager) findViewById(R.id.container);
        viewPager.setBackgroundResource(R.drawable.background);
        viewPager.setAdapter(mSectionsPagerAdapter);

        dotIndicator = (DotIndicator) findViewById(R.id.dotIndicator);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i(getClass().getName(), "onPageScrolled");

            }

            @Override
            public void onPageSelected(int position) {
                Log.i(getClass().getName(), "onPageSelected");

                dotIndicator.setSelectedItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i(getClass().getName(), "onPageScrollStateChanged");

            }
        });
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
            Log.i(getClass().getName(), "onCreateView");

            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            Log.i(getClass().getName(), "sectionNumber: " + sectionNumber);

            int layoutIdentifier = getResources().getIdentifier("fragment_home_screen" + String.valueOf(sectionNumber), "layout", getActivity().getPackageName());
            View rootView = inflater.inflate(layoutIdentifier, container, false);

            if (sectionNumber == 1) {
                // 1. Tablet navigation

                tabletNavigationContainer = (RelativeLayout) rootView.findViewById(R.id.tabletNavigationContainer);
                tabletNavigationImageView = (ImageView) rootView.findViewById(R.id.tabletNavigationImageView);
                tabletNavigationImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "tabletNavigationImageView onClick");

                        // Fetch apps for category (Tablet Navigation)
                        // TODO: load dynamically from Appstore
                        List<String> packageNames = Arrays.asList(
                                "com.android.camera2",
                                "com.android.gallery3d",
                                "com.android.soundrecorder",
                                "cc.openframeworks.inkSpace",
                                "com.google.fpl.liquidfunpaint",
                                "org.dsandler.apps.markers",
                                "org.esteban.piano",
                                "fr.tvbarthel.apps.cameracolorpicker.foss.kids",
                                "org.literacyapp.startguide",
                                "org.literacyapp.tilt"
                                // TODO: add "Shapi" // TODO: move to EGMA shape identification
                                // TODO: add Kintsukuroi
                                // TODO: add Memory Game For Kids
                        );

                        initializeDialog(packageNames);
                    }
                });


                // 2. EGRA skills

                egraOralVocabularyContainer = (RelativeLayout) rootView.findViewById(R.id.egraOralVocabularyContainer);
                egraOralVocabularyImageView = (ImageView) rootView.findViewById(R.id.egraOralVocabularyImageView);
                egraOralVocabularyImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "egraOralVocabularyImageView onClick");

                        // Fetch apps for category (Oral Vocabulary and Listening Comprehension)
                        // TODO: load dynamically from Appstore
                        List<String> packageNames = Arrays.asList(
                                "com.android.gallery3d",
                                "org.literacyapp", // TODO: only use the Video launcher
                                "org.literacyapp.imagepicker",
                                "org.literacyapp.storybooks"
                        );

                        initializeDialog(packageNames);
                    }
                });

                egraPhonemicAwarenessContainer = (RelativeLayout) rootView.findViewById(R.id.egraPhonemicAwarenessContainer);
                egraPhonemicAwarenessImageView = (ImageView) rootView.findViewById(R.id.egraPhonemicAwarenessImageView);
                egraPhonemicAwarenessImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "egraPhonemicAwarenessImageView onClick");

                        // Fetch apps for category (Phonemic Awareness)
                        // TODO: load dynamically from Appstore
                        List<String> packageNames = Arrays.asList(
                                "com.ubongokids.alphabetEng",
                                "org.literacyapp.soundcards"
                        );

                        initializeDialog(packageNames);
                    }
                });

                egraLetterIdentificationContainer = (RelativeLayout) rootView.findViewById(R.id.egraLetterIdentificationContainer);
                egraLetterIdentificationImageView = (ImageView) rootView.findViewById(R.id.egraLetterIdentificationImageView);
                egraLetterIdentificationImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "egraLetterIdentificationImageView onClick");

                        // Fetch apps for category (Letter Identification)
                        // TODO: load dynamically from Appstore
                        List<String> packageNames = Arrays.asList(
                                "com.ubongokids.alphabetEng",
                                "org.literacyapp", // TODO: only use the Literacy launcher
                                "org.literacyapp.handwriting",
                                "org.literacyapp.chat",
                                "org.literacyapp.visemes",
                                "org.literacyapp.voltair",
                                "org.literacyapp.walezi"
                        );

                        initializeDialog(packageNames);
                    }
                });


                // 3. EGMA skills

                egmaOralCountingContainer = (RelativeLayout) rootView.findViewById(R.id.egmaOralCountingContainer);
                egmaOralCountingImageView = (ImageView) rootView.findViewById(R.id.egmaOralCountingImageView);
                egmaOralCountingImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "egmaOralCountingImageView onClick");

                        // Fetch apps for category (Oral Counting)
                        // TODO: load dynamically from Appstore
                        List<String> packageNames = Arrays.asList(
                                "com.android.gallery3d",
                                "org.literacyapp", // TODO: only use the Numeracy launcher
                                "com.ubongokids.ratmathEng"
                        );

                        initializeDialog(packageNames);
                    }
                });

                egmaNumberIdentificationContainer = (RelativeLayout) rootView.findViewById(R.id.egmaNumberIdentificationContainer);
                egmaNumberIdentificationImageView = (ImageView) rootView.findViewById(R.id.egmaNumberIdentificationImageView);
                egmaNumberIdentificationImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "egmaNumberIdentificationImageView onClick");

                        // Fetch apps for category (Number Identification)
                        // TODO: load dynamically from Appstore
                        List<String> packageNames = Arrays.asList(
                                "com.ubongokids.ratmathEng",
                                "org.jempe.hockey",
                                "org.literacyapp", // TODO: only use the Numeracy launcher
                                "org.literacyapp.calculator",
                                "org.literacyapp.chat",
                                "org.literacyapp.handwriting_numbers",
                                "org.literacyapp.nya",
                                "org.literacyapp.tilt",
                                "ru.o2genum.coregame"
                        );

                        initializeDialog(packageNames);
                    }
                });
            } else if (sectionNumber == 2) {
                // 1. EGRA skills

                // TODO: syllable naming


                // 2. EGMA skills

                // TODO
            } else if (sectionNumber == 3) {
                // TODO
            }  else if (sectionNumber == 4) {
                // TODO
            }

            return rootView;
        }

        private void initializeDialog(List<String> packageNames) {
            Log.i(getClass().getName(), "initializeDialog");

            MaterialDialog materialDialog = new MaterialDialog.Builder(getContext())
                    .customView(R.layout.dialog_apps, true)
                    .theme(Theme.DARK)
                    .show();

            View customView = materialDialog.getCustomView();
            GridLayout appGridLayout = (GridLayout) customView.findViewById(R.id.appGridLayout);

            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            final PackageManager packageManager = getActivity().getPackageManager();
            List<ResolveInfo> availableActivities = packageManager.queryIntentActivities(intent, 0);
            for (ResolveInfo resolveInfo : availableActivities) {
                final ActivityInfo activityInfo = resolveInfo.activityInfo;
                Log.i(getClass().getName(), "activityInfo: " + activityInfo);

                Log.i(getClass().getName(), "activityInfo.packageName: " + activityInfo.packageName);
                Log.i(getClass().getName(), "activityInfo.name: " + activityInfo.name);

                final ComponentName componentName = new ComponentName(activityInfo.packageName, activityInfo.name);
                Log.i(getClass().getName(), "componentName: " + componentName);

                CharSequence label = resolveInfo.loadLabel(packageManager);
                Log.i(getClass().getName(), "label: " + label);

                Drawable icon = resolveInfo.loadIcon(packageManager);
                Log.i(getClass().getName(), "icon: " + icon);

                if (packageNames.contains(activityInfo.packageName)) {
                    View appView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_apps_app_view, appGridLayout, false);

                    ImageView appIconImageView = (ImageView) appView.findViewById(R.id.appIconImageView);
                    appIconImageView.setImageDrawable(icon);

                    appIconImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i(getClass().getName(), "appIconImageView onClick");

                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            intent.setComponent(componentName);
                            startActivity(intent);

                            EventTracker.reportApplicationOpenedEvent(getContext(), activityInfo.packageName);
                        }
                    });

                    appGridLayout.addView(appView);
                }
            }
        }

        @Override
        public void onStart() {
            Log.i(getClass().getName(), "onCreateView");
            super.onStart();
        }

        @Override
        public void onResume() {
            Log.i(getClass().getName(), "onResume");
            super.onResume();

            // Add subtle movements to the space ships
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(egraOralVocabularyContainer, "rotation", 2 + ((int) Math.random() * 3));
            objectAnimator.setDuration(1000 + ((int) Math.random() * 1000));
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
            objectAnimator.start();

//            objectAnimator = ObjectAnimator.ofFloat(egraPhonemicAwarenessContainer, "rotation", 2 + ((int) Math.random() * 3));
//            objectAnimator.start();
//
//            objectAnimator = ObjectAnimator.ofFloat(egraLetterIdentificationContainer, "rotation", 2 + ((int) Math.random() * 3));
//            objectAnimator.start();
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
        Log.i(getClass().getName(), "onBackPressed");

        // Do nothing
    }
}
