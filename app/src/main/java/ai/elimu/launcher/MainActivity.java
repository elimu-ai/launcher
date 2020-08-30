package ai.elimu.launcher;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ai.elimu.launcher.ui.HomeScreensActivity;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static final String APPSTORE_DOWNLOAD_URL = "https://github.com/elimu-ai/appstore/releases";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the elimu.ai Appstore is already installed
        try {
            PackageInfo packageInfoAppstore = getPackageManager().getPackageInfo(BuildConfig.APPSTORE_APPLICATION_ID, 0);
            Timber.i( "packageInfoAppstore.versionCode: " + packageInfoAppstore.versionCode);

            Intent intent = new Intent(getApplicationContext(), HomeScreensActivity.class);
            startActivity(intent);

        } catch (PackageManager.NameNotFoundException e) {
            Timber.w( null, e);
            Toast.makeText(getApplicationContext(), "The elimu.ai Launcher will not work without the elimu.ai Appstore: " + BuildConfig.APPSTORE_APPLICATION_ID, Toast.LENGTH_LONG).show();

            goToAppstoreDownload();
        }

        finish();
    }

    private void goToAppstoreDownload() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(APPSTORE_DOWNLOAD_URL));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Timber.i("onBackPressed");

        // Do nothing
    }
}
