package com.aslam.co321_project.Driver;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.aslam.co321_project.Authentication.logIn;
import com.aslam.co321_project.Common.BoxConditions;
import com.aslam.co321_project.Common.UploadDeliveryDetails;
import com.aslam.co321_project.Common.ViewDistribution;
import com.aslam.co321_project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.UUID;

public class ViewDistributionDriver extends AppCompatActivity {

    private String distributorId;
    private String pharmacyId;
    private String driverId;
    private String randomId;
    private String distributorName;
    private String pharmacyName;
    private String driverName;
    private String cityName;

    private String status;

    private String phoneLeft = "";
    private String phoneRight = "";

    private ListView listView;
    private LinkedList<String> linkedList; //boxList
    private UploadDeliveryDetails uploadDeliveryDetails;

    private Button buttonDistributorCall;
    private Button buttonPharmacyCall;
    Button btnDelivered;
    private TextView textView;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_view_distribution);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        buttonDistributorCall = findViewById(R.id.buttonDistributorCall_driver);
        buttonPharmacyCall = findViewById(R.id.buttonPharmacyCall_driver);
        btnDelivered = findViewById(R.id.btnDelivered_driver);
        btnDelivered.setEnabled(false);
        listView = findViewById(R.id.lvViewDistribution_driver);
        textView = findViewById(R.id.tvTransInfo_driver);

        getParams();
        toolBarHandler();
        setupTheActivity();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String boxName = linkedList.get(position);

                databaseReference.child("EndNodes").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(boxName)){
                            Intent intent = new Intent(ViewDistributionDriver.this, BoxConditions.class);
                            intent.putExtra("boxName", boxName);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ViewDistributionDriver.this, "Sorry, this box has no data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        buttonDistributorCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneLeft.length()>0){
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + phoneLeft));

                    try{
                        startActivity(callIntent);
                    } catch (Exception e){
                        Toast.makeText(ViewDistributionDriver.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ViewDistributionDriver.this, "no number found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonPharmacyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneRight.length()>0){
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + phoneRight));

                    try{
                        startActivity(callIntent);
                    } catch (Exception e){
                        Toast.makeText(ViewDistributionDriver.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ViewDistributionDriver.this, "no number found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void toolBarHandler() {
        Toolbar toolbar = findViewById(R.id.toolbarBoxStatus_driver);
        toolbar.setTitle("Box list");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //get parameters from previous activity
    private void getParams() {
        pharmacyId = getIntent().getStringExtra("pharmacyId");
        distributorId = getIntent().getStringExtra("distributorId");
        driverId = getIntent().getStringExtra("driverId");
        randomId = getIntent().getStringExtra("randomId");
        status = getIntent().getStringExtra("status");
    }

    private void setupTheActivity() {
        getPharmacyAddress();
        getPharmacyPhone();
        getBoxDataDriver();
        getDistributorPhone();

        if(status.equals("ongoing")){
            handleFloatingButton();
            handleDeliverButton();
            getDistributorName();
        }
    }

    private void getDistributorName() {
        databaseReference.child("distributors").child(distributorId).child("shopName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                distributorName = dataSnapshot.getValue().toString();
                getPharmacyName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPharmacyName() {
        databaseReference.child("pharmacies").child(pharmacyId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pharmacyName = dataSnapshot.child("pharmacyName").getValue().toString();
                String address = dataSnapshot.child("pharmacyAddress").getValue().toString();
                String [] splittedBoxArray = address.split("\\s+");
                cityName = splittedBoxArray[splittedBoxArray.length-1];

                getDriverName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDriverName() {
        databaseReference.child("userInfo").child(driverId).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                driverName = dataSnapshot.getValue().toString();

                uploadDeliveryDetails = new UploadDeliveryDetails(distributorName, pharmacyName, driverName, cityName, distributorId, pharmacyId, driverId, randomId, linkedList);
                btnDelivered.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDistributorPhone() {
        databaseReference.child("userInfo").child(distributorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phoneDistributor = dataSnapshot.child("phone").getValue().toString();
                phoneLeft = phoneDistributor;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getBoxDataDriver() {
        //get the box list
        databaseReference.child("driverTask").child(MainActivity.uid).child(status+"Deliveries").child(randomId).child("boxList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linkedList = new LinkedList<>();
                for(DataSnapshot myDataSnapshot: dataSnapshot.getChildren()){
                    linkedList.add(myDataSnapshot.getValue().toString());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewDistributionDriver.this, android.R.layout.simple_list_item_1, linkedList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPharmacyPhone() {
        databaseReference.child("userInfo").child(pharmacyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phonePharmacy = dataSnapshot.child("phone").getValue().toString();
                phoneRight = phonePharmacy;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPharmacyAddress() {
        databaseReference.child("pharmacies").child(pharmacyId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String addressToDisplay = dataSnapshot.child("pharmacyAddress").getValue().toString();
                String pharmacyName = dataSnapshot.child("pharmacyName").getValue().toString();

                if(status.equals("ongoing")){
                    textView.setText("This boxes are transported to\n" + pharmacyName +"\n"+ addressToDisplay);
                } else {
                    textView.setText("This boxes were transported to\n" + pharmacyName +"\n"+ addressToDisplay);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void handleDeliverButton() {
        btnDelivered.setVisibility(View.VISIBLE);
        btnDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewDistributionDriver.this);

                builder.setMessage("delivered?")
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handleDelivered();
                            }
                        })
                        .setNegativeButton("Cancel", null);

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    //method to execute when delivered button is clicked
    private void handleDelivered() {
        //update & delete one by one
        updateDistributor();
    }

    private void updateDistributor() {

        databaseReference.child("distributorTask").child(distributorId).child("pastDeliveries").child(randomId).setValue(uploadDeliveryDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updatePharmacist();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                updateDistributor();
            }
        });
    }

    private void updatePharmacist() {
        databaseReference.child("pharmacyTask").child(pharmacyId).child("pastDeliveries").child(randomId).setValue(uploadDeliveryDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateDriver();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        updatePharmacist();
                    }
                });
    }

    private void updateDriver() {
        databaseReference.child("driverTask").child(MainActivity.uid).child("pastDeliveries").child(randomId).setValue(uploadDeliveryDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        deleteDistributor();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        updateDriver();
                    }
                });
    }

    private void deleteDistributor() {
        databaseReference.child("distributorTask").child(distributorId).child("ongoingDeliveries").child(randomId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                deletePharmacist();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                deleteDistributor();
            }
        });
    }

    private void deletePharmacist() {
        databaseReference.child("pharmacyTask").child(pharmacyId).child("ongoingDeliveries").child(randomId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                deleteDriver();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                deletePharmacist();
            }
        });
    }

    private void deleteDriver() {
        databaseReference.child("driverTask").child(MainActivity.uid).child("ongoingDeliveries").child(randomId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ViewDistributionDriver.this, "Done", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                deleteDriver();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void handleFloatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab_driver);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("pharmacies").child(pharmacyId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String latitude = dataSnapshot.child("latitude").getValue().toString();
                        String longitude = dataSnapshot.child("longitude").getValue().toString();

                        if(latitude.length()>0 && longitude.length()>0){
                            try {
                                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            } catch (Exception e){
                                Toast.makeText(ViewDistributionDriver.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ViewDistributionDriver.this, "Sorry, Location is not provided", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}