package ai.elimu.launcher;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fetch Appstore version
        try {
            PackageInfo packageInfoAppstore = getPackageManager().getPackageInfo(BuildConfig.APPSTORE_APPLICATION_ID, 0);
            Timber.i( "packageInfoAppstore.versionCode: " + packageInfoAppstore.versionCode);
            // TODO: match available ContentProvider queries with the Appstore's versionCode
        } catch (PackageManager.NameNotFoundException e) {
            // The Appstore app has not been installed
            Timber.w( null, e);
            Toast.makeText(getApplicationContext(), "This launcher will not work until you install the Appstore app: " + BuildConfig.APPSTORE_APPLICATION_ID, Toast.LENGTH_LONG).show();
            // TODO: force the user to install the Appstore app
        }

        Intent intent = new Intent(getApplicationContext(), HomeScreensActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onBackPressed() {
        Timber.i("onBackPressed");
        // Do nothing
    }
}
