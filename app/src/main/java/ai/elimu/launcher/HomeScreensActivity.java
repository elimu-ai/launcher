package ai.elimu.launcher;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.andraskindler.parallaxviewpager.ParallaxViewPager;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ai.elimu.model.enums.content.LiteracySkill;
import ai.elimu.model.enums.content.NumeracySkill;
import ai.elimu.model.gson.admin.ApplicationGson;

//import ai.elimu.analytics.eventtracker.EventTracker;

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

        // Fetch Applications from the Appstore's ContentProvider
        List<ApplicationGson> applications = new ArrayList<>();
        Uri uri = Uri.parse("content://" + BuildConfig.APPSTORE_APPLICATION_ID + ".provider/application");
        Log.i(getClass().getName(), "uri: " + uri);
        Cursor cursor = getContentResolver(). query(uri, null, null, null, null);
        if (cursor != null) {
            Log.i(getClass().getName(), "cursor.getCount(): " + cursor.getCount());
            if (cursor.getCount() > 0) {
                boolean isLast = false;
                while (!isLast) {
                    cursor.moveToNext();

                    // Convert from database row to Application object

                    int columnId = cursor.getColumnIndex("_id");
                    Long id = cursor.getLong(columnId);
                    Log.i(getClass().getName(), "id: " + id);

                    int columnLocale = cursor.getColumnIndex("LOCALE");
                    String locale = cursor.getString(columnLocale);
                    Log.i(getClass().getName(), "locale: " + locale);

                    int columnPackageName = cursor.getColumnIndex("PACKAGE_NAME");
                    String packageName = cursor.getString(columnPackageName);
                    Log.i(getClass().getName(), "packageName: " + packageName);

                    int columnLiteracySkills = cursor.getColumnIndex("LITERACY_SKILLS");
                    String literacySkillsAsString = cursor.getString(columnLiteracySkills);
                    Log.i(getClass().getName(), "literacySkillsAsString: " + literacySkillsAsString);
                    Set<LiteracySkill> literacySkillSet = new HashSet<>();
                    try {
                        JSONArray jsonArray = new JSONArray(literacySkillsAsString);
                        Log.i(getClass().getName(), "jsonArray: " + jsonArray);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String value = jsonArray.getString(i);
                            Log.i(getClass().getName(), "value: " + value);
                            LiteracySkill literacySkill = LiteracySkill.valueOf(value);
                            literacySkillSet.add(literacySkill);
                        }
                    } catch (JSONException e) {
                        Log.e(getClass().getName(), null, e);
                    }

                    int columnNumeracySkills = cursor.getColumnIndex("NUMERACY_SKILLS");
                    String numeracySkillsAsString = cursor.getString(columnNumeracySkills);
                    Log.i(getClass().getName(), "columnNumeracySkillsAsString: " + numeracySkillsAsString);
                    Set<NumeracySkill> numeracySkillSet = new HashSet<>();
                    try {
                        JSONArray jsonArray = new JSONArray(numeracySkillsAsString);
                        Log.i(getClass().getName(), "jsonArray: " + jsonArray);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String value = jsonArray.getString(i);
                            Log.i(getClass().getName(), "value: " + value);
                            NumeracySkill numeracySkill = NumeracySkill.valueOf(value);
                            numeracySkillSet.add(numeracySkill);
                        }
                    } catch (JSONException e) {
                        Log.e(getClass().getName(), null, e);
                    }

                    ApplicationGson application = new ApplicationGson();
                    application.setId(id);
                    application.setPackageName(packageName);
                    application.setLiteracySkills(literacySkillSet);
                    application.setNumeracySkills(numeracySkillSet);
                    applications.add(application);

                    isLast = cursor.isLast();
                }
                Log.i(getClass().getName(), "cursor.isClosed(): " + cursor.isClosed());
                cursor.close();
            } else {
                Toast.makeText(getApplicationContext(), "cursor.getCount() == 0", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "cursor == null", Toast.LENGTH_LONG).show();
        }
        Log.i(getClass().getName(), "applications.size(): " + applications.size());
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
            Log.i(getClass().getName(), "onCreateView");

            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            Log.i(getClass().getName(), "sectionNumber: " + sectionNumber);

            int layoutIdentifier = getResources().getIdentifier("fragment_home_screen" + String.valueOf(sectionNumber), "layout", getActivity().getPackageName());
            View rootView = inflater.inflate(layoutIdentifier, container, false);

            if (sectionNumber == 1) {
                // 1. Tablet navigation

                tabletNavigationContainer = rootView.findViewById(R.id.tabletNavigationContainer);
                tabletNavigationImageView = rootView.findViewById(R.id.tabletNavigationImageView);
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
                                "ai.elimu.startguide",
                                "ai.elimu.tilt"
                                // TODO: add Kintsukuroi
                                // TODO: add Memory Game For Kids
                        );

                        initializeDialog(packageNames, null, null);
                    }
                });


                // 2. EGRA skills

                egraOralVocabularyContainer = rootView.findViewById(R.id.egraOralVocabularyContainer);
                egraOralVocabularyImageView = rootView.findViewById(R.id.egraOralVocabularyImageView);
                egraOralVocabularyImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "egraOralVocabularyImageView onClick");

                        // Fetch apps for category (Oral Vocabulary and Listening Comprehension)
                        // TODO: load dynamically from Appstore
                        List<String> packageNames = Arrays.asList(
                                "com.android.gallery3d",
                                "ai.elimu", // TODO: only use the Video launcher
                                "ai.elimu.imagepicker",
                                "ai.elimu.storybooks"
                        );

                        initializeDialog(packageNames, LiteracySkill.ORAL_VOCABULARY, null);
                    }
                });

                egraPhonemicAwarenessContainer = rootView.findViewById(R.id.egraPhonemicAwarenessContainer);
                egraPhonemicAwarenessImageView = rootView.findViewById(R.id.egraPhonemicAwarenessImageView);
                egraPhonemicAwarenessImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "egraPhonemicAwarenessImageView onClick");

                        // Fetch apps for category (Phonemic Awareness)
                        // TODO: load dynamically from Appstore
                        List<String> packageNames = Arrays.asList(
                                "com.ubongokids.alphabetEng",
                                "ai.elimu.soundcards"
                        );

                        initializeDialog(packageNames, LiteracySkill.PHONEMIC_AWARENESS, null);
                    }
                });

                egraLetterIdentificationContainer = rootView.findViewById(R.id.egraLetterIdentificationContainer);
                egraLetterIdentificationImageView = rootView.findViewById(R.id.egraLetterIdentificationImageView);
                egraLetterIdentificationImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "egraLetterIdentificationImageView onClick");

                        // Fetch apps for category (Letter Identification)
                        // TODO: load dynamically from Appstore
                        List<String> packageNames = Arrays.asList(
                                "com.ubongokids.alphabetEng",
                                "ai.elimu", // TODO: only use the Literacy launcher
                                "ai.elimu.handwriting",
                                "ai.elimu.chat",
                                "ai.elimu.visemes",
                                "ai.elimu.voltair",
                                "ai.elimu.walezi"
                        );

                        initializeDialog(packageNames, LiteracySkill.LETTER_IDENTIFICATION, null);
                    }
                });


                // 3. EGMA skills

                egmaOralCountingContainer = rootView.findViewById(R.id.egmaOralCountingContainer);
                egmaOralCountingImageView = rootView.findViewById(R.id.egmaOralCountingImageView);
                egmaOralCountingImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "egmaOralCountingImageView onClick");

                        // Fetch apps for category (Oral Counting)
                        // TODO: load dynamically from Appstore
                        List<String> packageNames = Arrays.asList(
                                "com.android.gallery3d",
                                "ai.elimu", // TODO: only use the Numeracy launcher
                                "fr.tvbarthel.apps.shapi" // TODO: move to previous EGMA category
                        );

                        initializeDialog(packageNames, null, NumeracySkill.ORAL_COUNTING);
                    }
                });

                egmaNumberIdentificationContainer = rootView.findViewById(R.id.egmaNumberIdentificationContainer);
                egmaNumberIdentificationImageView = rootView.findViewById(R.id.egmaNumberIdentificationImageView);
                egmaNumberIdentificationImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "egmaNumberIdentificationImageView onClick");

                        // Fetch apps for category (Number Identification)
                        // TODO: load dynamically from Appstore
                        List<String> packageNames = Arrays.asList(
                                "org.jempe.hockey",
                                "ai.elimu", // TODO: only use the Numeracy launcher
                                "ai.elimu.calculator",
                                "ai.elimu.chat",
                                "ai.elimu.handwriting_numbers",
                                "ai.elimu.nya",
                                "ai.elimu.tilt",
                                "ru.o2genum.coregame"
                        );

                        initializeDialog(packageNames, null, NumeracySkill.NUMBER_IDENTIFICATION);
                    }
                });

                egmaMissingNumberContainer = rootView.findViewById(R.id.egmaMissingNumberContainer);
                egmaMissingNumberImageView = rootView.findViewById(R.id.egmaMissingNumberImageView);
                egmaMissingNumberImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "egmaMissingNumberImageView onClick");

                        // Fetch apps for category (Missing Number and Quantity Discrimination)
                        // TODO: load dynamically from Appstore
                        List<String> packageNames = Arrays.asList(
                                "ai.elimu.missing_number",
                                "ai.elimu.nya.qd" // TODO: move to previous EGMA category (Quantity Discrimination)
                        );

                        initializeDialog(packageNames, null, NumeracySkill.MISSING_NUMBER);
                    }
                });
            } else if (sectionNumber == 2) {
                // 1. EGRA skills

                egraSyllableNamingContainer = rootView.findViewById(R.id.egraSyllableNamingContainer);
                egraSyllableNamingImageView = rootView.findViewById(R.id.egraSyllableNamingImageView);
                egraSyllableNamingImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "egraSyllableNamingImageView onClick");

                        // Fetch apps for category (Syllable Naming and Familiar Word Reading)
                        // TODO: load dynamically from Appstore
                        List<String> packageNames = Arrays.asList(
                                "ai.elimu.familiar_word_reading"
                        );

                        initializeDialog(packageNames, LiteracySkill.FAMILIAR_WORD_READING, null);
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

        private void initializeDialog(List<String> packageNames, LiteracySkill literacySkill, NumeracySkill numeracySkill) {
            Log.i(getClass().getName(), "initializeDialog");

            MaterialDialog materialDialog = new MaterialDialog.Builder(getContext())
                    .customView(R.layout.dialog_apps, true)
                    .theme(Theme.DARK)
                    .show();

            View customView = materialDialog.getCustomView();
            GridLayout appGridLayout = customView.findViewById(R.id.appGridLayout);

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
                    if ((literacySkill == LiteracySkill.ORAL_VOCABULARY) || (literacySkill == LiteracySkill.PHONEMIC_AWARENESS)) {
                        if ("ai.elimu".equals(activityInfo.packageName)
                                && !activityInfo.name.equals("ai.elimu.content.multimedia.video.VideosActivity")) {
                            continue;
                        }
                    } else if (literacySkill == LiteracySkill.LETTER_IDENTIFICATION) {
                        if ("ai.elimu".equals(activityInfo.packageName)
                                && !activityInfo.name.equals("ai.elimu.content.letter.LettersActivity")
                                && !activityInfo.name.equals("ai.elimu.content.multimedia.video.VideosActivity")) {
                            continue;
                        }
                    }

                    if (numeracySkill == NumeracySkill.ORAL_COUNTING) {
                        if ("ai.elimu".equals(activityInfo.packageName)
                                && !activityInfo.name.equals("ai.elimu.content.multimedia.video.VideosActivity")) {
                            continue;
                        }
                    } else if (numeracySkill == NumeracySkill.NUMBER_IDENTIFICATION) {
                        if ("ai.elimu".equals(activityInfo.packageName)
                                && !activityInfo.name.equals("ai.elimu.content.number.NumbersActivity")
                                && !activityInfo.name.equals("ai.elimu.content.multimedia.video.VideosActivity")) {
                            continue;
                        }
                    }

                    View appView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_apps_app_view, appGridLayout, false);

                    ImageView appIconImageView = appView.findViewById(R.id.appIconImageView);
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

//                            EventTracker.reportApplicationOpenedEvent(getContext(), activityInfo.packageName);
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
        Log.i(getClass().getName(), "onBackPressed");

        // Do nothing
    }
}
