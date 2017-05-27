package org.literacyapp.appstore.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import org.literacyapp.appstore.AppstoreApplication;
import org.literacyapp.appstore.dao.ApplicationDao;
import org.literacyapp.appstore.model.Application;
import org.literacyapp.model.enums.admin.ApplicationStatus;
import org.literacyapp.model.enums.content.LiteracySkill;
import org.literacyapp.model.enums.content.NumeracySkill;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Hide/show apps based on Student's current literacy/numeracy skills.
 */
public class StudentUpdatedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(getClass().getName(), "onReceive");

        ArrayList<String> availableLiteracySkillsStringArray = intent.getStringArrayListExtra("availableLiteracySkills");
        Log.i(getClass().getName(), "availableLiteracySkillsStringArray: " + availableLiteracySkillsStringArray);
        Set<LiteracySkill> availableLiteracySkills = new HashSet<>();
        for (String literacySkillAsString : availableLiteracySkillsStringArray) {
            LiteracySkill literacySkill = LiteracySkill.valueOf(literacySkillAsString);
            availableLiteracySkills.add(literacySkill);
        }
        Log.i(getClass().getName(), "availableLiteracySkills: " + availableLiteracySkills);

        ArrayList<String> availableNumeracySkillsStringArray = intent.getStringArrayListExtra("availableNumeracySkills");
        Log.i(getClass().getName(), "availableNumeracySkillsStringArray: " + availableNumeracySkillsStringArray);
        Set<NumeracySkill> availableNumeracySkills = new HashSet<>();
        for (String numeracySkillAsString : availableNumeracySkillsStringArray) {
            NumeracySkill numeracySkill = NumeracySkill.valueOf(numeracySkillAsString);
            availableNumeracySkills.add(numeracySkill);
        }
        Log.i(getClass().getName(), "availableNumeracySkills: " + availableNumeracySkills);

        Set<String> packagesToHide = new HashSet<>();

        if (!availableLiteracySkills.isEmpty() || !availableNumeracySkills.isEmpty()) {
            AppstoreApplication appstoreApplication = (AppstoreApplication) context.getApplicationContext();
            ApplicationDao applicationDao = appstoreApplication.getDaoSession().getApplicationDao();
            List<Application> applications = applicationDao.loadAll();
            for (Application application : applications) {
                Log.i(getClass().getName(), "packageName: " + application.getPackageName() + ", literacySkills: " + application.getLiteracySkills() + ", numeracySkills: " + application.getNumeracySkills());

                // Filter by LiteracySkill
                if (!application.getLiteracySkills().isEmpty() && !availableLiteracySkills.isEmpty()) {
                    // Hide the Application if it _only_ contains LiteracySkills not yet made available to the Student
                    boolean appContainsAtLeastOneAvailableLiteracySkill = false;
                    for (LiteracySkill literacySkill : application.getLiteracySkills()) {
                        if (availableLiteracySkills.contains(literacySkill)) {
                            appContainsAtLeastOneAvailableLiteracySkill = true;
                            break;
                        }
                    }

                    if (!appContainsAtLeastOneAvailableLiteracySkill) {
                        // Hide Application
                        Log.w(getClass().getName(), "LiteracySkill(s) not yet available. Hiding Application: " + application.getPackageName() + ", literacySkills: " + application.getLiteracySkills());
                        packagesToHide.add(application.getPackageName());
                    } else {
                        if (application.getApplicationStatus() == ApplicationStatus.ACTIVE) {
                            // Show Application
                            Log.i(getClass().getName(), "LiteracySkill(s) available. Showing Application: " + application.getPackageName() + ", literacySkills: " + application.getLiteracySkills());
                        }
                    }
                }

                // Filter by NumeracySkill
                if (!application.getNumeracySkills().isEmpty() && !availableNumeracySkills.isEmpty()) {
                    // Hide the Application if it _only_ contains NumeracySkills not yet made available to the Student
                    boolean appContainsAtLeastOneAvailableNumeracySkill = false;
                    for (NumeracySkill numeracySkill : application.getNumeracySkills()) {
                        if (availableNumeracySkills.contains(numeracySkill)) {
                            appContainsAtLeastOneAvailableNumeracySkill = true;
                            break;
                        }
                    }

                    if (!appContainsAtLeastOneAvailableNumeracySkill) {
                        // Hide Application
                        Log.w(getClass().getName(), "NumeracySkill(s) not yet available. Hiding Application: " + application.getPackageName() + ", numeracySkills: " + application.getNumeracySkills());
                        packagesToHide.add(application.getPackageName());
                    } else {
                        if (application.getApplicationStatus() == ApplicationStatus.ACTIVE) {
                            // Show Application
                            Log.i(getClass().getName(), "NumeracySkill(s) available. Showing Application: " + application.getPackageName() + ", numeracySkills: " + application.getNumeracySkills());
                        }
                    }
                }

                // No filter
                if (application.getLiteracySkills().isEmpty() && application.getNumeracySkills().isEmpty()) {
                    if (application.getApplicationStatus() == ApplicationStatus.ACTIVE) {
                        // Show Application
                        Log.i(getClass().getName(), "No skills required. Showing Application: " + application.getPackageName() + ", numeracySkills: " + application.getNumeracySkills());
                    }
                }
            }
        }

        AppstoreApplication appstoreApplication = (AppstoreApplication) context;
        appstoreApplication.setPackagesToHide(packagesToHide);
    }

}
