package com.aslam.co321_project.Driver;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.aslam.co321_project.Common.BoxConditions;
import com.aslam.co321_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDriverNotification extends Fragment {
    private DatabaseReference databaseReference = MainActivity.databaseReference;
    private String driverId = MainActivity.uid;

    private LinkedList<String> linkedList;
    private LinkedList<String> linkedListProblem;
    private ListView listView;

    public FragmentDriverNotification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_driver_notification, container, false);
        listView = view.findViewById(R.id.lvNotification_driver);

        retrieveData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String boxName = linkedList.get(position);

                databaseReference.child("EndNodes").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(boxName)){
                            Intent intent = new Intent(getContext(), BoxConditionForNoti.class);
                            intent.putExtra("boxName", boxName);
                            intent.putExtra("problem", linkedListProblem.get(position));
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "Sorry, this box has no data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void retrieveData() {
        databaseReference.child("notifications").child(driverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linkedList = new LinkedList<>();
                linkedListProblem = new LinkedList<>();

                for(DataSnapshot myDataSnapshot: dataSnapshot.getChildren()){
                    if((boolean)myDataSnapshot.child("read").getValue()==false){
                        String problem = myDataSnapshot.child("problem").getValue().toString();
                        String boxName = myDataSnapshot.getKey();
                        linkedListProblem.add(boxName + " | " + problem);
                        linkedList.add(boxName);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, linkedListProblem);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
