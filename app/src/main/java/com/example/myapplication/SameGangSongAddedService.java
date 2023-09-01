package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class SameGangSongAddedService extends Service {
    //public static int NOTIFICATION_ID = 1;
   public static boolean firstTime = true;
    public SameGangSongAddedService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String gang = intent.getStringExtra("gang");

        db.collection("songs").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange change:value.getDocumentChanges()) {
                    if(change.getType() == DocumentChange.Type.MODIFIED &&
                            change.getDocument().get("gang", String.class).equals(gang))
                    {
                        //send a notification
                        sendNotification(intent);
                    }
                }
            }

        });

        db.collection("songs").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!firstTime) {
                    for (DocumentChange change : value.getDocumentChanges()) {
                        if (change.getType() == DocumentChange.Type.ADDED &&
                                change.getDocument().get("gang", String.class).equals(gang)) {
                            //send a notification
                            sendNotification(intent);
                        }
                    }
                }
                else
                    firstTime = false;
            }

        });//



        return super.onStartCommand(intent, flags, startId);

    }

    public void sendNotification(Intent intent)
    {

       NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "OveRated";
        int NOTIFICATION_ID = 1;
        String gang = intent.getStringExtra("gang");
        if(gang==null)
            gang="";
       /* NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "CHANNEL_ID + Channel", NotificationManager.IMPORTANCE_HIGH);

        Notification notification = new NotificationCompat.Builder(this, channel.getId())
                .setContentTitle("OverRated")
                .setContentText("New rate by your gang: " + gang)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0))
                .setSmallIcon(R.drawable.ic_stat_name)
                .build();
        notificationManager.notify(NOTIFICATION_ID, notification);
        NOTIFICATION_ID++;*/

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "OverRated";
            String Description = "OverRated channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        //String gang = intent.getStringExtra("gang");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("OverRated")
                .setContentText("New rate by your gang: " + gang);

        Intent resultIntent = new Intent(getApplicationContext(), RatingsByGangActivity.class);
        resultIntent.putExtra("gang", gang );
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NOTIFICATION_ID++;
        notificationManager.notify(NOTIFICATION_ID, builder.build());


    }
}