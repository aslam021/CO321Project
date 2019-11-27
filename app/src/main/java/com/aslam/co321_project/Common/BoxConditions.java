package com.aslam.co321_project.Common;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.aslam.co321_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class BoxConditions extends AppCompatActivity {

    private TextView tvBoxInfo;
    private TextView tvTemperature;
    private TextView tvPressure;
    private TextView tvHumidity;
    private GraphView graphTemperature;
    private GraphView graphHumidity;
    private GraphView graphPressure;
    private int pressure = 0;
    private int humidity = 0;
    private int temp = 0;

    private String boxName = "";
    private String relayNode = "rn1001";
    private String [] readingTemperature = new String[5];
    private String [] readingPressure = new String[5];
    private String [] readingHumidity = new String[5];

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_box_conditions);

        tvBoxInfo = findViewById(R.id.tvBoxInfo);
        tvTemperature = findViewById(R.id.tvTemp);
        tvPressure = findViewById(R.id.tvPressure);
        tvHumidity = findViewById(R.id.tvHumad);
        graphTemperature = findViewById(R.id.graphTemperature);
        graphHumidity = findViewById(R.id.graphHumidity);
        graphPressure = findViewById(R.id.graphPressure);

        getParams();
        toolBarHandler();

        try{
            retrieveBoxInfo();
            retrieveBoxStatus();
        } catch (Exception e){
            Toast.makeText(BoxConditions.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void getParams() {
        boxName = getIntent().getStringExtra("boxName");
    }

    private void toolBarHandler() {
        Toolbar toolbar = findViewById(R.id.toolbarBoxStatus);
        toolbar.setTitle(boxName);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    // graph view handler
    public void graphTemp(View v) {
        graphTemperature.removeAllSeries();

        tvTemperature.setBackgroundResource(R.drawable.shadoweffect);
        tvPressure.setBackgroundResource(android.R.color.background_light);
        tvHumidity.setBackgroundResource(R.color.white);

        graphTemperature.setVisibility(View.VISIBLE);
        graphPressure.setVisibility(View.GONE);
        graphHumidity.setVisibility(View.GONE);

        temp=0;
        drawGraphTemperature();

        tvTemperature.setText("Temperature (C)");
        tvPressure.setText("Pressure");
        tvHumidity.setText("Humidity");
    }

    // graph view handler
    public void graphPress(View v){
        graphPressure.removeAllSeries();

        tvPressure.setBackgroundResource(R.drawable.shadoweffect);
        tvTemperature.setBackgroundResource(R.color.white);
        tvHumidity.setBackgroundResource(R.color.white);

        graphPressure.setVisibility(View.VISIBLE);
        graphTemperature.setVisibility(View.GONE);
        graphHumidity.setVisibility(View.GONE);

        pressure=0;
        drawGraphPressure();

        tvTemperature.setText("Temperature");
        tvPressure.setText("Pressure (Pa)");
        tvHumidity.setText("Humidity");
    }

    // graph view handler
    public void graphHumid(View v){
        graphHumidity.removeAllSeries();

        tvHumidity.setBackgroundResource(R.drawable.shadoweffect);
        tvTemperature.setBackgroundResource(R.color.white);
        tvPressure.setBackgroundResource(R.color.white);

        graphHumidity.setVisibility(View.VISIBLE);
        graphPressure.setVisibility(View.GONE);
        graphTemperature.setVisibility(View.GONE);

        humidity=0;
        drawGraphHumidity();

        tvTemperature.setText("Temperature");
        tvPressure.setText("Pressure");
        tvHumidity.setText("Humidity (%)");
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void retrieveBoxStatus() {
        try{
            databaseReference.child("Readings").child(relayNode).child(boxName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int i = 0;
                    for (DataSnapshot boxStatusSnapShot : dataSnapshot.getChildren()) {
                        readingHumidity[i] = boxStatusSnapShot.child("Humidity").getValue().toString();
                        readingTemperature[i] = boxStatusSnapShot.child("Temperature").getValue().toString();
                        readingPressure[i] = boxStatusSnapShot.child("Pressure").getValue().toString();
                        i++;
                        if (i>4) break;
                    }
                    drawGraphTemperature();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(BoxConditions.this);

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

    private void retrieveBoxInfo() {
        try{
            databaseReference.child("EndNodes").child(boxName).child("Items").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String s="";
                    for (DataSnapshot boxInfoSnapShot : dataSnapshot.getChildren()) {
                        s = s + boxInfoSnapShot.getKey() +" : "+boxInfoSnapShot.getValue().toString()+"\n";
                    }

                    if(s.length()==0){
                        s = "nothing is in the box";
                    } else {
                        s = s.substring(0, s.length()-1);
                    }

                    tvBoxInfo.setText(s);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e){
        }
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void drawGraphTemperature() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {

                new DataPoint(1, Double.valueOf(readingTemperature[0])),

                new DataPoint(2, Double.valueOf(readingTemperature[1])),

                new DataPoint(3, Double.valueOf(readingTemperature[2])),

                new DataPoint(4, Double.valueOf(readingTemperature[3])),

                new DataPoint(5, Double.valueOf(readingTemperature[4]))

        });
        series.isDrawDataPoints();
        series.setDrawDataPoints(true);
        series.setThickness(3);
        if (temp==0) {
            series.setAnimated(true);
            temp++;
        }else
            series.setAnimated(false);

        graphTemperature.removeAllSeries();
        graphTemperature.addSeries(series);
    }

    private void drawGraphHumidity() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {

                new DataPoint(1, Double.valueOf(readingHumidity[0])),

                new DataPoint(2, Double.valueOf(readingHumidity[1])),

                new DataPoint(3, Double.valueOf(readingHumidity[2])),

                new DataPoint(4, Double.valueOf(readingHumidity[3])),

                new DataPoint(5, Double.valueOf(readingHumidity[4]))

        });
        series.isDrawDataPoints();
        series.setDrawDataPoints(true);
        series.setThickness(3);

        if (humidity==0) {
            series.setAnimated(true);
            humidity++;
        }else
            series.setAnimated(false);

        graphHumidity.removeAllSeries();
        graphHumidity.addSeries(series);
    }

    private void drawGraphPressure() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {

                new DataPoint(1, Double.valueOf(readingPressure[0])),

                new DataPoint(2, Double.valueOf(readingPressure[1])),

                new DataPoint(3, Double.valueOf(readingPressure[2])),

                new DataPoint(4, Double.valueOf(readingPressure[3])),

                new DataPoint(5, Double.valueOf(readingPressure[4]))

        });
        series.isDrawDataPoints();
        series.setDrawDataPoints(true);
        series.setThickness(3);

        if (pressure==0) {
            series.setAnimated(true);
            pressure++;
        }else
            series.setAnimated(false);

        graphPressure.removeAllSeries();
        graphPressure.addSeries(series);
    }

}
