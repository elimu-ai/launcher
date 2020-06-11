package ai.elimu.launcher.util;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashSet;
import java.util.Set;

import ai.elimu.model.enums.admin.ApplicationStatus;
import ai.elimu.model.enums.content.LiteracySkill;
import ai.elimu.model.enums.content.NumeracySkill;
import ai.elimu.model.v2.gson.application.ApplicationGson;
import timber.log.Timber;

public class CursorToApplicationConverter {

    /**
     * See https://github.com/elimu-ai/appstore/tree/master/app/schemas/ai.elimu.appstore.room.RoomDb and
     * https://github.com/elimu-ai/appstore/tree/master/app/src/main/java/ai/elimu/appstore/room.
     */
    public static ApplicationGson getApplication(Cursor cursor) {
        Timber.i("getApplication");

        int columnIndexId = cursor.getColumnIndex("id");
        Long id = cursor.getLong(columnIndexId);
        Timber.i("id: " + id);

        int columnIndexPackageName = cursor.getColumnIndex("packageName");
        String packageName = cursor.getString(columnIndexPackageName);
        Timber.i("packageName: " + packageName);

        int columnIndexApplicationStatus = cursor.getColumnIndex("applicationStatus");
        ApplicationStatus applicationStatus = ApplicationStatus.valueOf(cursor.getString(columnIndexApplicationStatus));
        Timber.i("applicationStatus: " + applicationStatus);

        int columnIndexLiteracySkills = cursor.getColumnIndex("literacySkills");
        String literacySkillsAsString = cursor.getString(columnIndexLiteracySkills);
        Timber.i("literacySkillsAsString: " + literacySkillsAsString);
        Set<LiteracySkill> literacySkills = new HashSet<>();
        try {
            JSONArray jsonArray = new JSONArray(literacySkillsAsString);
            Timber.i("jsonArray: " + jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                String value = jsonArray.getString(i);
                Timber.i("value: " + value);
                LiteracySkill literacySkill = LiteracySkill.valueOf(value);
                literacySkills.add(literacySkill);
            }
        } catch (JSONException e) {
            Timber.e(e);
        }

        int columnIndexNumeracySkills = cursor.getColumnIndex("numeracySkills");
        String numeracySkillsAsString = cursor.getString(columnIndexNumeracySkills);
        Timber.i("columnNumeracySkillsAsString: " + numeracySkillsAsString);
        Set<NumeracySkill> numeracySkills = new HashSet<>();
        try {
            JSONArray jsonArray = new JSONArray(numeracySkillsAsString);
            Timber.i("jsonArray: " + jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                String value = jsonArray.getString(i);
                Timber.i("value: " + value);
                NumeracySkill numeracySkill = NumeracySkill.valueOf(value);
                numeracySkills.add(numeracySkill);
            }
        } catch (JSONException e) {
            Timber.e(e);
        }

        ApplicationGson application = new ApplicationGson();
        application.setId(id);
        application.setPackageName(packageName);
        application.setApplicationStatus(applicationStatus);
        application.setLiteracySkills(literacySkills);
        application.setNumeracySkills(numeracySkills);
        return application;
    }
}
