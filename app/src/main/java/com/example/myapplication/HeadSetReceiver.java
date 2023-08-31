package com.example.myapplication;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HeadSetReceiver extends BroadcastReceiver implements View.OnClickListener {
    //this is a test for git changes
    Dialog dialog;
    Button btnYes;
    Button btnNo;
    Button btnCancel;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if( intent.getIntExtra("state", 0) == 1 ) {
            //Toast.makeText(context, "broadcast received", Toast.LENGTH_SHORT).show();
            showDialog(context);
        }
    }

    protected void showDialog(Context context)
    {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setTitle("Headset Unplugged!");
        dialog.setCancelable(true); //closes the dialog when clicking outside
        btnYes = dialog.findViewById(R.id.btnDialogYes);
        btnYes.setOnClickListener(this);
        btnNo = dialog.findViewById(R.id.btnDialogNo);
        btnNo.setOnClickListener(this);
        btnCancel = dialog.findViewById(R.id.btnDialogCancel);
        btnCancel.setOnClickListener(this);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if( view == btnCancel )
        {
            dialog.cancel();
        }
        else if( view == btnNo )
        {
            dialog.dismiss();
        }
        else //if yes
        {
            //not recommended to start an intent from a broadcast receiver better to use the notification mechanism
            Intent intent = new Intent(view.getContext(), MainActivity2.class);
            view.getContext().startActivity(intent);
        }
    }
}