package com.aslam.co321_project.Driver;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.aslam.co321_project.Common.AboutUs;
import com.aslam.co321_project.Authentication.logIn;
import com.aslam.co321_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    public static String uid;
    static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public static boolean homeSelected = true;

    /****************  options menu handlers  **********************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the MainActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage("Are you sure?")
                    .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logOut();
                        }
                    })
                    .setNegativeButton("Cancel", null);

            AlertDialog alert = builder.create();
            alert.show();
            return true;
        } else if (id == R.id.action_aboutUs) {
            Intent intent = new Intent(this, AboutUs.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //this function will handle the logout process
    private void logOut() {
        //logout
        FirebaseAuth.getInstance().signOut();

        //clear the cache
        try{
            File dir = MainActivity.this.getCacheDir();
            deleteDir(dir);
        } catch (Exception e){
            e.printStackTrace();
        }

        //go to login activity
        Intent intent = new Intent(MainActivity.this, logIn.class);
        finish();
        finishAffinity();
        startActivity(intent);
    }

    //perform cache clear
    private boolean deleteDir(File dir) {
        if(dir != null && dir.isDirectory()){
            String [] children = dir.list();

            for(String s: children){
                boolean success = deleteDir(new File(dir, s));
                if(!success){
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()){
            return dir.delete();
        }
        return false;
    }

    /****************************************************************/


    /******************* bottom navigation bar handlers *************/
    private boolean loadFragment(Fragment fragment) {
        if(fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.driver_fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        Fragment fragment;
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home_pharm_driv:
                    homeSelected = true;
                    fragment = new FragmentDriverHome();
                    break;
                case R.id.navigation_past_pharm_driv:
                    homeSelected = false;
                    fragment = new FragmentDriverPast();
                    break;
                case R.id.navigation_my_account_pharm_driv:
                    fragment = new FragmentDriverMyAccount();
                    break;
                case R.id.navigation_noti_pharm_driver:
                    fragment = new FragmentDriverNotification();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    /*****************************************************************/



    //get parameters from previous activity
    private void getParams() {
        uid = getIntent().getStringExtra("uid");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        startService(new Intent(getBaseContext(), NotificationService.class)); //notification service

        Toolbar toolbar = findViewById(R.id.driverToolbar);
        setSupportActionBar(toolbar);

        getParams();

        BottomNavigationView navView = findViewById(R.id.nav_view_driver);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new FragmentDriverHome());
    }
}
