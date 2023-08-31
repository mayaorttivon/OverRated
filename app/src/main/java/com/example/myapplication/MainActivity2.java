package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    EditText etSongName;
    EditText etArtist;
    EditText etGang;
    Button btnPlus;
    Button btnMinus;
    TextView tvGrade;
    Button btnAddSong;
    ArrayList<OverRatedSong> songs;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        init();
    }

    private void init()
    {
        etSongName = findViewById(R.id.etSongName);
        etArtist = findViewById(R.id.etArtist);
        etGang = findViewById(R.id.etGang);
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        tvGrade = findViewById(R.id.tvGrade);
        btnAddSong = findViewById(R.id.btnAddSong);
        btnAddSong.setOnClickListener(this);
        songs = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View view) {
        int grade = Integer.valueOf(tvGrade.getText().toString());
        if( view == btnPlus )
        {
            grade++;
        }
        else if( view == btnMinus )
        {
            grade--;
        }
        tvGrade.setText(Integer.toString(grade));

        //must be after handling the grade
        if( view == btnAddSong )
        {
            OverRatedSong song = new OverRatedSong(etSongName.getText().toString(), etArtist.getText().toString(), grade, etGang.getText().toString());
            //start a service that notifies about adding a new rating for the same gang.
            //for this example it would notify only if there are already rated songs for this gang
            Intent intent = new Intent(this, SameGangSongAddedService.class);
            intent.putExtra("gang", song.getGang() );
            startService(intent);
            //add to the songs collection so we can use it afterwards in case we would like to display all songs.
            //maybe this is redundant and is enough at this point just to add it to the db and pull to ArraList by need.
            songs.add(song);
            db.collection("songs")
                    .add(song)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
    }
}