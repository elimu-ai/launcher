package org.literacyapp.launcher;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
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

import java.util.ArrayList;
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

        private RelativeLayout tabletNavigationContainer;
        private ImageView tabletNavigationImageView;

        private RelativeLayout egraOralVocabularyContainer;
        private ImageView egraOralVocabularyImageView;

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
                tabletNavigationContainer = (RelativeLayout) rootView.findViewById(R.id.tabletNavigationContainer);
                tabletNavigationImageView = (ImageView) rootView.findViewById(R.id.tabletNavigationImageView);
                tabletNavigationImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "tabletNavigationImageView onClick");

//                        Intent intent = new Intent(getContext(), CategoryOverlayActivity.class);
//                        startActivity(intent);

                        MaterialDialog materialDialog = new MaterialDialog.Builder(getContext())
                                .customView(R.layout.dialog_apps, true)
                                .theme(Theme.DARK)
                                .show();

                        View customView = materialDialog.getCustomView();
                        GridLayout appGridLayout = (GridLayout) customView.findViewById(R.id.appGridLayout);

                        // Fetch apps for category
                        // TODO: fetch dynamically from Appstore
                        List<String> packageNames = Arrays.asList(
                                "com.android.camera2",
                                "com.android.gallery3d",
                                "fr.tvbarthel.apps.cameracolorpicker.foss.kids",
                                "org.literacyapp.startguide",
                                "org.literacyapp.tilt"
                        );
                        Intent intent = new Intent(Intent.ACTION_MAIN, null);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        final PackageManager packageManager = getActivity().getPackageManager();
                        List<ResolveInfo> availableActivities = packageManager.queryIntentActivities(intent, 0);
                        for (ResolveInfo resolveInfo : availableActivities) {
                            final String packageName = resolveInfo.activityInfo.packageName;
                            Log.i(getClass().getName(), "packageName: " + packageName);
                            CharSequence label = resolveInfo.loadLabel(packageManager);
                            Log.i(getClass().getName(), "label: " + label);
                            Drawable icon = resolveInfo.loadIcon(packageManager);
                            Log.i(getClass().getName(), "icon: " + icon);

                            if (packageNames.contains(packageName)) {
                                View appView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_apps_app_view, appGridLayout, false);

                                ImageView appIconImageView = (ImageView) appView.findViewById(R.id.appIconImageView);
                                appIconImageView.setImageDrawable(icon);

                                appIconImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Log.i(getClass().getName(), "appIconImageView onClick");

                                        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
                                        startActivity(intent);
                                    }
                                });

                                appGridLayout.addView(appView);
                            }
                        }
                    }
                });

                egraOralVocabularyContainer = (RelativeLayout) rootView.findViewById(R.id.egraOralVocabularyContainer);
                egraOralVocabularyImageView = (ImageView) rootView.findViewById(R.id.egraOralVocabularyImageView);
                egraOralVocabularyImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "egraOralVocabularyImageView onClick");


                    }
                });
            }

            return rootView;
        }

        @Override
        public void onStart() {
            Log.i(getClass().getName(), "onCreateView");
            super.onStart();

            // Add subtle movements to the space ships
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(egraOralVocabularyContainer, "rotation", 2);
            objectAnimator.setDuration(1000);
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
            objectAnimator.start();
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
