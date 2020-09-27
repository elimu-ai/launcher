package ai.elimu.launcher;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ai.elimu.launcher.ui.HomeScreensActivity;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
