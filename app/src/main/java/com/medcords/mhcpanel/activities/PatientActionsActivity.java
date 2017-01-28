package com.medcords.mhcpanel.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.medcords.mhcpanel.R;

public class PatientActionsActivity extends AppCompatActivity {

    private ImageButton uploadRecordsButton, searchRecordsButton, editPatientDetailsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_actions);

        editPatientDetailsButton = (ImageButton) findViewById(R.id.edit_patient_button);
        uploadRecordsButton = (ImageButton) findViewById(R.id.upload_records_button);
        searchRecordsButton = (ImageButton) findViewById(R.id.search_records_button);

        editPatientDetailsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                uploadRecordsButton.setImageResource(R.mipmap.upload_grey);
                searchRecordsButton.setImageResource(R.mipmap.search_grey);
                editPatientDetailsButton.setImageResource(R.mipmap.edit_green);
                return false;
            }
        });

        searchRecordsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                uploadRecordsButton.setImageResource(R.mipmap.upload_grey);
                searchRecordsButton.setImageResource(R.mipmap.search_green);
                editPatientDetailsButton.setImageResource(R.mipmap.edit_grey);
                return false;
            }
        });

        uploadRecordsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                uploadRecordsButton.setImageResource(R.mipmap.upload_green);
                searchRecordsButton.setImageResource(R.mipmap.search_grey);
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadRecordsButton.setImageResource(R.mipmap.upload_grey);
        searchRecordsButton.setImageResource(R.mipmap.search_grey);
        editPatientDetailsButton.setImageResource(R.mipmap.edit_grey);
    }
}
