package com.aslam.co321_project.Distributor;

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
import com.aslam.co321_project.R;

import com.aslam.co321_project.Common.ViewDistribution;
import com.aslam.co321_project.Common.DeliverDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.aslam.co321_project.Distributor.MainActivity.databaseReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDistributorHome extends Fragment {

    private View view;
    private ListView myListView;
    private CustomListAdapter customListAdapter;
    private ArrayList<DeliverDetails> deliveryList = new ArrayList<>();

    public FragmentDistributorHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_distributor_home, container, false);
        myListView = view.findViewById(R.id.lvCommonListView);

        //TODO: searchview
//        SearchView searchView = view.findViewById(R.id.searchBar);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if(deliveryList.contains(query)){
//                    customListAdapter.getFilter().filter(query);
//                } else {
//                    Toast.makeText(getContext(), "No match found", Toast.LENGTH_SHORT).show();
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                //customListAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });

        try {
            setListView();
        } catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
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

    private void setListView() {
        final String distributorId = MainActivity.uid;
        databaseReference.child("distributorTask").child(distributorId).child("ongoingDeliveries").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deliveryList.clear();
                for(DataSnapshot tempSnapShot: dataSnapshot.getChildren()){
                    final String pharmacyId = tempSnapShot.child("pharmacyId").getValue().toString();
                    final String driverId = tempSnapShot.child("driverId").getValue().toString();
                    final String randomId = tempSnapShot.child("randomId").getValue().toString();
                    final String pharmacyName = tempSnapShot.child("pharmacyName").getValue().toString();
                    final String driverName = tempSnapShot.child("driverName").getValue().toString();
                    DeliverDetails deliverDetails = new DeliverDetails(pharmacyName, driverName, distributorId, pharmacyId, driverId,  randomId);

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

}


