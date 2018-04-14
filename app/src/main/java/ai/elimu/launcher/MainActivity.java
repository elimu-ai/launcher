package ai.elimu.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
        //do nothing
    }
}
