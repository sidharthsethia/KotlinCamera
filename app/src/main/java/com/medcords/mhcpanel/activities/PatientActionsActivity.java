package com.medcords.mhcpanel.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.medcords.mhcpanel.R;

public class PatientActionsActivity extends AppCompatActivity {

    private ImageButton uploadRecordsButton, shareRecordsButton, editPatientDetailsButton;
    private Boolean isUserNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_actions);

        isUserNew = getIntent().getExtras().getBoolean("new_user");

        editPatientDetailsButton = (ImageButton) findViewById(R.id.edit_patient_button);
        uploadRecordsButton = (ImageButton) findViewById(R.id.upload_records_button);
        shareRecordsButton = (ImageButton) findViewById(R.id.share_records_button);

        editPatientDetailsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                uploadRecordsButton.setImageResource(R.mipmap.upload_grey);
                shareRecordsButton.setImageResource(R.mipmap.file_grey);
                editPatientDetailsButton.setImageResource(R.mipmap.edit_green);
                return false;
            }
        });

        shareRecordsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!isUserNew){
                    uploadRecordsButton.setImageResource(R.mipmap.upload_grey);
                    shareRecordsButton.setImageResource(R.mipmap.file_green);
                    editPatientDetailsButton.setImageResource(R.mipmap.edit_grey);
                }
                return false;
            }
        });

        uploadRecordsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                uploadRecordsButton.setImageResource(R.mipmap.upload_green);
                shareRecordsButton.setImageResource(R.mipmap.file_grey);
                editPatientDetailsButton.setImageResource(R.mipmap.edit_grey);
                return false;
            }
        });

        uploadRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PatientActionsActivity.this, CameraActivity.class);
                startActivity(i);
            }
        });

        shareRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PatientActionsActivity.this, ShareRecordsActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadRecordsButton.setImageResource(R.mipmap.upload_grey);
        shareRecordsButton.setImageResource(R.mipmap.file_grey);
        editPatientDetailsButton.setImageResource(R.mipmap.edit_grey);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_patient, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_end_session:
                endSession();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void endSession() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("lastRecordsUpdatedForId", 0);
        editor.commit();

        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        endSession();
    }
}
