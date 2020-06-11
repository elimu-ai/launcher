package ai.elimu.launcher.util;

import android.database.Cursor;

import ai.elimu.model.enums.admin.ApplicationStatus;
import ai.elimu.model.v2.gson.application.ApplicationGson;
import timber.log.Timber;

public class CursorToApplicationConverter {

    /**
     * See https://github.com/elimu-ai/appstore/tree/master/app/schemas/ai.elimu.appstore.room.RoomDb
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

        ApplicationGson application = new ApplicationGson();
        application.setId(id);
        application.setPackageName(packageName);
        application.setApplicationStatus(applicationStatus);
        return application;
    }
}
