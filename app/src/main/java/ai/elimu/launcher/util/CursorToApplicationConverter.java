package ai.elimu.launcher.util;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashSet;
import java.util.Set;

import ai.elimu.model.enums.content.LiteracySkill;
import ai.elimu.model.enums.content.NumeracySkill;
import ai.elimu.model.v1.gson.admin.ApplicationGson;
import timber.log.Timber;

public class CursorToApplicationConverter {

    /**
     * See See https://github.com/elimu-ai/appstore/blob/master/app/src/main/java/ai/elimu/appstore/dao/ApplicationDao.java
     */
    public static ApplicationGson getApplication(Cursor cursor) {
        Timber.i("getApplication");

        int columnId = cursor.getColumnIndex("_id");
        Long id = cursor.getLong(columnId);
        Timber.i("id: " + id);

        int columnLocale = cursor.getColumnIndex("LOCALE");
        String locale = cursor.getString(columnLocale);
        Timber.i("locale: " + locale);

        int columnPackageName = cursor.getColumnIndex("PACKAGE_NAME");
        String packageName = cursor.getString(columnPackageName);
        Timber.i("packageName: " + packageName);

        int columnLiteracySkills = cursor.getColumnIndex("LITERACY_SKILLS");
        String literacySkillsAsString = cursor.getString(columnLiteracySkills);
        Timber.i("literacySkillsAsString: " + literacySkillsAsString);
        Set<LiteracySkill> literacySkillSet = new HashSet<>();
        try {
            JSONArray jsonArray = new JSONArray(literacySkillsAsString);
            Timber.i("jsonArray: " + jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                String value = jsonArray.getString(i);
                Timber.i("value: " + value);
                LiteracySkill literacySkill = LiteracySkill.valueOf(value);
                literacySkillSet.add(literacySkill);
            }
        } catch (JSONException e) {
            Timber.e(e);
        }

        int columnNumeracySkills = cursor.getColumnIndex("NUMERACY_SKILLS");
        String numeracySkillsAsString = cursor.getString(columnNumeracySkills);
        Timber.i("columnNumeracySkillsAsString: " + numeracySkillsAsString);
        Set<NumeracySkill> numeracySkillSet = new HashSet<>();
        try {
            JSONArray jsonArray = new JSONArray(numeracySkillsAsString);
            Timber.i("jsonArray: " + jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                String value = jsonArray.getString(i);
                Timber.i("value: " + value);
                NumeracySkill numeracySkill = NumeracySkill.valueOf(value);
                numeracySkillSet.add(numeracySkill);
            }
        } catch (JSONException e) {
            Timber.e(e);
        }

        ApplicationGson application = new ApplicationGson();
        application.setId(id);
        application.setPackageName(packageName);
        application.setLiteracySkills(literacySkillSet);
        application.setNumeracySkills(numeracySkillSet);
        return application;
    }
}
