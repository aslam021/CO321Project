package com.aslam.co321_project.Driver;


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
import com.aslam.co321_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.aslam.co321_project.Driver.MainActivity.databaseReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDriverPast extends Fragment {
    private ArrayList<DeliverDetails> deliveryList = new ArrayList<>();
    private ListView myListView;
    private CustomListAdapter customListAdapter;

    public FragmentDriverPast() {
        // Required empty public constructor
    }

    //retrieve data from firebase and set ListView
    private void setListView() {
        final String driverId = MainActivity.uid;
        databaseReference.child("driverTask").child(driverId).child("pastDeliveries").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deliveryList.clear();
                for(DataSnapshot tempSnapShot: dataSnapshot.getChildren()){
                    final String pharmacyId = tempSnapShot.child("pharmacyId").getValue().toString();
                    final String driverId = tempSnapShot.child("driverId").getValue().toString();
                    final String distributorId = tempSnapShot.child("distributorId").getValue().toString();
                    final String randomId = tempSnapShot.child("randomId").getValue().toString();

                    final String pharmacyName = tempSnapShot.child("pharmacyName").getValue().toString();
                    final String cityName = tempSnapShot.child("cityName").getValue().toString();
                    DeliverDetails deliverDetails = new DeliverDetails(pharmacyName, cityName, distributorId, pharmacyId, driverId,  randomId);
                    deliveryList.add(deliverDetails);
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
        View view = inflater.inflate(R.layout.fragment_driver_past, container, false);

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
                Intent intent = new Intent(getContext(), ViewDistributionDriver.class);
                intent.putExtra("distributorId", deliveryList.get(position).getDistributorId());
                intent.putExtra("pharmacyId", deliveryList.get(position).getPharmacyId());
                intent.putExtra("driverId", deliveryList.get(position).getDriverId());
                intent.putExtra("randomId", deliveryList.get(position).getRandomId());
                intent.putExtra("status", "past");
                startActivity(intent);
            }
        });

        return view;
    }

}
