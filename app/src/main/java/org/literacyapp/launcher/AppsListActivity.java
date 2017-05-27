package org.literacyapp.launcher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.literacyapp.appstore.AppstoreApplication;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class AppsListActivity extends AppCompatActivity {
    private PackageManager manager;
    private List<Item> apps;

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_list);

        loadApps();
        loadListView();
        addClickListener();
    }

    private void loadApps(){
        AppstoreApplication appstoreApplication = (AppstoreApplication) getApplicationContext();
        Set<String> packagesToHide = appstoreApplication.getPackagesToHide();
        manager = getPackageManager();
        apps = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> availableActivities = manager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : availableActivities){
            //exclude apps which should be hidden
            if ((packagesToHide == null) || (!packagesToHide.contains(resolveInfo.activityInfo.packageName))){
                Item app = new Item();
                app.label = resolveInfo.activityInfo.packageName; //get app package
                app.name = resolveInfo.loadLabel(manager); //get app name
                app.icon = resolveInfo.loadIcon(manager); //get app icon
                apps.add(app);
            }
        }
    }

    private void loadListView(){
        list = (ListView) findViewById(R.id.list);

        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, R.layout.item, apps){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.item, null);
                }

                ImageView appIcon = (ImageView) convertView.findViewById(R.id.icon);
                appIcon.setImageDrawable(apps.get(position).icon);

                TextView appName = (TextView) convertView.findViewById(R.id.name);
                appName.setText(apps.get(position).name);

                return convertView;
            }
        };

        //sort the apps ascending by name
        adapter.sort(new Comparator<Item>() {
            @Override
            public int compare(Item item, Item t1) {
                String app1Name = item.name.toString();
                String app2Name = t1.name.toString();
                return app1Name.compareTo(app2Name);
            }
        });

        list.setAdapter(adapter);
    }

    private void addClickListener(){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = manager.getLaunchIntentForPackage(apps.get(position).label.toString());
                startActivity(intent);
            }
        });
    }
}
