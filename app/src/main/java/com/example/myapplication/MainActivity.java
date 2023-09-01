package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail;
    EditText etPassword;
    Button btnSignIn;
    Button btnContinue;
    EditText etGang;
    private FirebaseAuth auth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }
    private void init()
    {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
        btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(this);
        etGang = findViewById(R.id.etGang);

    }

    @Override
    public void onClick(View view) {
        FirebaseUser user = auth.getCurrentUser();
        if( view == btnSignIn ) {
            if (user == null) {
                auth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<AuthResult> task) {
                                                       if (task.isSuccessful()) {
                                                           Log.d(TAG, "User Created Successfully");
                                                           Toast.makeText(MainActivity.this, "Successful sign-in", Toast.LENGTH_SHORT).show();
                                                           btnContinue.setEnabled(true);
                                                       } else {
                                                           Log.d(TAG, "User Creation failed");
                                                       }
                                                   }
                                               }

                        );
            }
            else {
                btnContinue.setEnabled(true);
                btnContinue.setBackgroundColor(Color.BLACK);
            }
        }
        else if( view == btnContinue )
        {
            //start a service that notifies about adding a new rating for the same gang.
            //for this example it would notify only if there are already rated songs for this gang
            Intent serviceIntent = new Intent(this, SameGangSongAddedService.class);
            serviceIntent.putExtra("gang", etGang.getText().toString() );
            startService(serviceIntent);

            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("gang", etGang.getText().toString() );
            startActivity(intent);
        }
    }


}