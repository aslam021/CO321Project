package com.aslam.co321_project.Driver;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.aslam.co321_project.Common.BoxConditions;
import com.aslam.co321_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.LinkedList;

public class BoxConditionForNoti extends AppCompatActivity {

    private String boxName;
    private String problemM;
    private String problem;

    private GraphView graph;
    private TextView infoProblem;
    private TextView infoStable;

    private String [] graphReadings = new String[5];
    private String relayNode = "rn1001";

    private String driverId = MainActivity.uid;
    private LineGraphSeries<DataPoint> series;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_box_condition_for_noti);

        graph = findViewById(R.id.graphNoti);
        infoProblem = findViewById(R.id.infoProblem);
        infoStable = findViewById(R.id.infoStable);

        getParams();
        toolBarHandler();

        String [] splitted = problemM.split("\\s+");
        problem = splitted[splitted.length-1];

        infoProblem.setText("Problem: " + problem);

        retrieveData(problem);
//        setColor();

    }

    private void setColor() {
        databaseReference.child("notifications").child(driverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot myDataSnapshot: dataSnapshot.getChildren()){
                    if((boolean)myDataSnapshot.child("read").getValue()==false){
                        infoStable.setTextColor(Color.RED);
                        series.setColor(Color.RED);
                    } else {
                        infoStable.setTextColor(Color.GREEN);
                        series.setColor(Color.GREEN);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void retrieveData(final String problem) {

        if (!problem.equals("Stability")){
            infoStable.setVisibility(View.GONE);
            graph.setVisibility(View.VISIBLE);

            try{
                databaseReference.child("Readings").child(relayNode).child(boxName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot boxStatusSnapShot : dataSnapshot.getChildren()) {
                            graphReadings[i] = boxStatusSnapShot.child(problem).getValue().toString();
                            i++;
                            if (i>4) break;
                        }
                        drawGraph();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (Exception e){
                AlertDialog.Builder builder = new AlertDialog.Builder(BoxConditionForNoti.this);

                builder.setMessage("This box has no data")
                        .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        else {
            graph.setVisibility(View.GONE);
            infoStable.setVisibility(View.VISIBLE);
            databaseReference.child("Readings").child(relayNode).child(boxName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int i = 0;
                    for (DataSnapshot boxStatusSnapShot : dataSnapshot.getChildren()) {
                        String text = boxStatusSnapShot.child(problem).getValue().toString();
                        infoStable.setText(text);

                        if (text.equals("Stable")) infoStable.setTextColor(Color.GREEN);
                        else infoStable.setTextColor(Color.RED);

                        i++;
                        if (i>4) break;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }




    }

    private void drawGraph() {
        series = new LineGraphSeries<>(new DataPoint[] {

                new DataPoint(1, Double.valueOf(graphReadings[0])),

                new DataPoint(2, Double.valueOf(graphReadings[1])),

                new DataPoint(3, Double.valueOf(graphReadings[2])),

                new DataPoint(4, Double.valueOf(graphReadings[3])),

                new DataPoint(5, Double.valueOf(graphReadings[4]))

        });
        series.isDrawDataPoints();
        series.setDrawDataPoints(true);
        series.setThickness(3);
        series.setColor(Color.RED);
        series.setAnimated(true);

        graph.addSeries(series);
    }

    //get parameters from previous activity
    private void getParams() {
        boxName = getIntent().getStringExtra("boxName");
        problemM = getIntent().getStringExtra("problem");
    }

    private void toolBarHandler() {
        Toolbar toolbar = findViewById(R.id.infoToolBar);
        toolbar.setTitle(boxName);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
