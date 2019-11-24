package com.aslam.co321_project.Authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aslam.co321_project.R;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PharmacyDet extends AppCompatActivity {

    EditText etDistName;
    EditText etDistPhone;
    EditText etDistAddress;

    String uid;
    String pharmacyName;
    String pharmacyPhone;
    String pharmacyAddress;
    String longitude;
    String latitude;

    private DatabaseReference databaseReferenceUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_pharmacy_det);

        getParams();

        etDistName = findViewById(R.id.editTextPharmacyNameReg);
        etDistPhone = findViewById(R.id.editTextPhonePharm);
        etDistAddress = findViewById(R.id.editTextAddressPharm);

        Button btnAdd = findViewById(R.id.buttonAddPharm);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pharmacyName = etDistName.getText().toString();
                pharmacyPhone = etDistPhone.getText().toString();
                pharmacyAddress = etDistAddress.getText().toString();

                try{
                    ulpoadPharmacyInfo();
                } catch (Exception e){
                    Toast.makeText(PharmacyDet.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnAddCoordinates = findViewById(R.id.buttonAddCoordinate);
        final MapView mapViewPharmacy = findViewById(R.id.mapViewPharmacy);
        btnAddCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapViewPharmacy.getVisibility()==View.VISIBLE){
                    mapViewPharmacy.setVisibility(View.GONE);
                } else {
                    mapViewPharmacy.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //upload pharmacy information to firebase
    private void ulpoadPharmacyInfo() {
        //user information
        databaseReferenceUserInfo = FirebaseDatabase.getInstance().getReference("pharmacies");
        Pharmacy pharmacy = new Pharmacy(uid,pharmacyName, pharmacyPhone, pharmacyAddress);
        databaseReferenceUserInfo.child(uid).setValue(pharmacy)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finishActivity();
                    }
                });
    }

    //finish the registration
    private void finishActivity() {
        Intent intent = new Intent(PharmacyDet.this, finishReg.class);
        intent.putExtra("uid", uid);
        intent.putExtra("type", "Pharmacist");
        startActivity(intent);
    }

    //function to get parameters from previous activity
    private void getParams() {
        //get parameters from previous activity
        uid = getIntent().getStringExtra("uid");
    }
}