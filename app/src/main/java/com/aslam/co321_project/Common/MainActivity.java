package com.aslam.co321_project.Common;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.aslam.co321_project.Authentication.logIn;
import com.aslam.co321_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    String type;
    String uid;
    private ImageView logoImage;
    private Animation animBlink;

    @Override
    protected void onStart() {
        logoImage = findViewById(R.id.imageBlink);
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);

        logoImage.startAnimation(animBlink);
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_main);

        //check whether already logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Intent intent = new Intent(MainActivity.this, com.aslam.co321_project.Authentication.logIn.class);
            startActivity(intent);
            finish();
        } else {
            uid = user.getUid();
            startApp();
        }
    }

    //find the type of user and start the app
    private void startApp(){
        try{
            DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance().getReference().child("userInfo").child(uid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    type = dataSnapshot.child("type").getValue().toString();
                    logIn.type = type;
                    if(type.equals("Driver")){
                        startDriver();
                    } else if (type.equals("Pharmacist")){
                        startPharmacist();
                    } else {
                        startDistributor();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    //start app for driver
    private void startDriver() {
        Intent intent = new Intent(MainActivity.this, com.aslam.co321_project.Driver.MainActivity.class);
        intent.putExtra("uid", uid);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    //start app for pharmacist
    private void startPharmacist() {
        Intent intent = new Intent(MainActivity.this, com.aslam.co321_project.Pharmacist.MainActivity.class);
        intent.putExtra("uid", uid);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    //start app for distributor
    private void startDistributor() {
        Intent intent = new Intent(MainActivity.this, com.aslam.co321_project.Distributor.MainActivity.class);
        intent.putExtra("uid", uid);intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
