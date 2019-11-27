package com.aslam.co321_project.Driver;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.ArrayAdapter;

import com.aslam.co321_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

/**
 * Created by TutorialsPoint7 on 8/23/2016.
 */

public class NotificationService extends Service {

    private DatabaseReference databaseReference = MainActivity.databaseReference;
    private String driverId = MainActivity.uid;
    private final String CHANNEL_ID = "Vision Pod";
    private final int NOTIFICATION_ID = 001;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        monitorForNewTasks();
        monitorAbnormals();
        return START_STICKY;
    }

    private void monitorAbnormals() {
        databaseReference.child("notifications").child(driverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot myDataSnapshot: dataSnapshot.getChildren()){
                    if((boolean)myDataSnapshot.child("notified").getValue()==false){
                        String problem = myDataSnapshot.child("problem").getValue().toString();
                        String boxName = myDataSnapshot.getKey();
                        displayNotification("check the " + problem + "in box " + boxName);
                        databaseReference.child("notifications").child(driverId).child(boxName).child("notified").setValue(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void monitorForNewTasks() {
        databaseReference.child("driverTask").child(driverId).child("ongoingDeliveries").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot tempSnapShot: dataSnapshot.getChildren()){
                    if((boolean)tempSnapShot.child("notified").getValue()==false){
                        displayNotification("New task added");
                        databaseReference.child("driverTask").child(driverId).child("ongoingDeliveries").child(tempSnapShot.getKey()).child("notified").setValue(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayNotification(String message) {
        Uri notiSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_local_shipping_white_24dp);
        builder.setContentTitle("Vision Pod");
        builder.setContentText(message);
        builder.setSound(notiSound);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }
}
