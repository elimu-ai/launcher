package ai.elimu.launcher;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ai.elimu.launcher.ui.HomeScreensActivity;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the elimu.ai Appstore is already installed
        try {
            PackageInfo packageInfoAppstore = getPackageManager().getPackageInfo(BuildConfig.APPSTORE_APPLICATION_ID, 0);
            Timber.i( "packageInfoAppstore.versionCode: " + packageInfoAppstore.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            Timber.w( null, e);
            Toast.makeText(getApplicationContext(), "The elimu.ai Launcher will not work without the elimu.ai Appstore: " + BuildConfig.APPSTORE_APPLICATION_ID, Toast.LENGTH_LONG).show();
            // TODO: Add link to GitHub for downloading APK
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
