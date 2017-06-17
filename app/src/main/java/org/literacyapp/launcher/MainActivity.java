package org.literacyapp.launcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.literacyapp.appstore.AppSynchronizationActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AppsListActivity.class);
                startActivity(intent);
            }
        });

        Button buttonAppSynchronization = (Button) findViewById(R.id.app_synchronization);
        buttonAppSynchronization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AppSynchronizationActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = new Intent(getApplicationContext(), HomeScreensActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
