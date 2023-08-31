package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RatingsByGangActivity extends AppCompatActivity {

    TextView tvGang;
    FirebaseFirestore db;
    ArrayList<OverRatedSong> songs;
    OverRatedSongAdapter overRatedSongAdapter;
    ListView overRatedSongsLV;
    private HeadSetReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings_by_gang);

        tvGang = findViewById(R.id.tvGang);
        Intent intent = this.getIntent();
        String gang = intent.getStringExtra("gang");
        tvGang.setText("Rated songs by your gang " + gang + ": ");
        receiver = new HeadSetReceiver();
        displayRatingsByGang(gang);

    }

    protected void displayRatingsByGang(String gang) {
        db = FirebaseFirestore.getInstance();

        db.collection("songs").whereEqualTo("gang", gang).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List lst = task.getResult().toObjects(OverRatedSong.class);
                            OverRatedSong song = null;
                            songs = new ArrayList<OverRatedSong>(lst);
                            buildSongsListView(songs);

                        } else {
                            Log.d(TAG, "onComplete: failed");
                        }
                    }
                });
    }

    protected void buildSongsListView(ArrayList<OverRatedSong> songs)
    {
        overRatedSongAdapter = new OverRatedSongAdapter(this, 0, 0, songs);
        overRatedSongsLV = findViewById(R.id.lvRatedSongsByGang);
        overRatedSongsLV.setAdapter(overRatedSongAdapter);
    }
    @Override
    protected void onResume() {
        IntentFilter headsetFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(receiver, headsetFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }
}