package ai.elimu.launcher.util

import ai.elimu.model.v2.enums.admin.ApplicationStatus
import ai.elimu.model.v2.enums.content.LiteracySkill
import ai.elimu.model.v2.enums.content.NumeracySkill
import ai.elimu.model.v2.gson.application.ApplicationGson
import android.database.Cursor
import org.json.JSONArray
import org.json.JSONException
import timber.log.Timber

object CursorToApplicationConverter {
    /**
     * See https://github.com/elimu-ai/appstore/tree/master/app/schemas/ai.elimu.appstore.room.RoomDb and
     * https://github.com/elimu-ai/appstore/tree/master/app/src/main/java/ai/elimu/appstore/room.
     */
    fun getApplication(cursor: Cursor): ApplicationGson {
        Timber.i("getApplication")

        val columnIndexId = cursor.getColumnIndex("id")
        val id = cursor.getLong(columnIndexId)
        Timber.i("id: $id")

        val columnIndexPackageName = cursor.getColumnIndex("packageName")
        val packageName = cursor.getString(columnIndexPackageName)
        Timber.i("packageName: $packageName")

        val columnIndexApplicationStatus = cursor.getColumnIndex("applicationStatus")
        val applicationStatus =
            ApplicationStatus.valueOf(cursor.getString(columnIndexApplicationStatus))
        Timber.i("applicationStatus: $applicationStatus")

        val columnIndexLiteracySkills = cursor.getColumnIndex("literacySkills")
        val literacySkillsAsString = cursor.getString(columnIndexLiteracySkills)
        Timber.i("literacySkillsAsString: $literacySkillsAsString")
        val literacySkills: MutableSet<LiteracySkill> = HashSet()
        try {
            val jsonArray = JSONArray(literacySkillsAsString)
            Timber.i("jsonArray: $jsonArray")
            for (i in 0 until jsonArray.length()) {
                val value = jsonArray.getString(i)
                Timber.i("value: $value")
                val literacySkill = LiteracySkill.valueOf(value)
                literacySkills.add(literacySkill)
            }
        } catch (e: JSONException) {
            Timber.e(e)
        }

        val columnIndexNumeracySkills = cursor.getColumnIndex("numeracySkills")
        val numeracySkillsAsString = cursor.getString(columnIndexNumeracySkills)
        Timber.i("columnNumeracySkillsAsString: $numeracySkillsAsString")
        val numeracySkills: MutableSet<NumeracySkill> = HashSet()
        try {
            val jsonArray = JSONArray(numeracySkillsAsString)
            Timber.i("jsonArray: $jsonArray")
            for (i in 0 until jsonArray.length()) {
                val value = jsonArray.getString(i)
                Timber.i("value: $value")
                val numeracySkill = NumeracySkill.valueOf(value)
                numeracySkills.add(numeracySkill)
            }
        } catch (e: JSONException) {
            Timber.e(e)
        }

        val application = ApplicationGson()
        application.id = id
        application.packageName = packageName
        application.applicationStatus = applicationStatus
        application.literacySkills = literacySkills
        application.numeracySkills = numeracySkills
        return application
    }
}
