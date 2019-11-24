package com.aslam.co321_project.Pharmacist;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.aslam.co321_project.Common.CustomListAdapter;
import com.aslam.co321_project.Common.DeliverDetails;
import com.aslam.co321_project.Common.ViewDistribution;
import com.aslam.co321_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.aslam.co321_project.Pharmacist.MainActivity.databaseReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPharmacistPast extends Fragment {
    private ArrayList<DeliverDetails> deliveryList = new ArrayList<>();

    private CustomListAdapter customListAdapter;

    private ListView myListView;

    private String distributorName;
    private String driverName;

    public FragmentPharmacistPast() {
        // Required empty public constructor
    }

    private void setListView() {
        final String pharmacyId = MainActivity.uid;
        databaseReference.child("pharmacyTask").child(pharmacyId).child("ongoingDeliveries").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deliveryList.clear();
                for(DataSnapshot tempSnapShot: dataSnapshot.getChildren()){
                    final String pharmacyId = tempSnapShot.child("pharmacyId").getValue().toString();
                    final String driverId = tempSnapShot.child("driverId").getValue().toString();
                    final String distributorId = tempSnapShot.child("distributorId").getValue().toString();
                    final String randomId = tempSnapShot.child("randomId").getValue().toString();

                    databaseReference.child("distributors").child(distributorId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            distributorName = dataSnapshot.child("shopName").getValue().toString();
                            databaseReference.child("userInfo").child(driverId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    driverName = dataSnapshot.child("name").getValue().toString();

                                    DeliverDetails deliverDetails = new DeliverDetails(distributorName, driverName, distributorId, pharmacyId, driverId, randomId);

                                    deliveryList.add(deliverDetails);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                customListAdapter = new CustomListAdapter(getContext(), R.layout.simplerow, deliveryList);
                myListView.setAdapter(customListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pharmacist_past, container, false);

        myListView = view.findViewById(R.id.lvCommonListViewPast);

        try {
            //setlistview
            setListView();
        } catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ViewDistribution.class);
                intent.putExtra("distributorId", deliveryList.get(position).getDistributorId());
                intent.putExtra("pharmacyId", deliveryList.get(position).getPharmacyId());
                intent.putExtra("driverId", deliveryList.get(position).getDriverId());
                intent.putExtra("randomId", deliveryList.get(position).getRandomId());
                startActivity(intent);
            }
        });
        return view;
    }

}
