package ai.elimu.launcher.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ai.elimu.launcher.R;
import timber.log.Timber;

public class CategoryOverlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.i("onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_overlay);
    }
}
