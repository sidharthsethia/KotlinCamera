package com.medcords.mhcpanel.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.medcords.mhcpanel.R;
import com.medcords.mhcpanel.adapters.PatientListRecyclerViewAdapter;
import com.medcords.mhcpanel.models.Patient;

import java.util.ArrayList;

public class PatientListActivity extends AppCompatActivity {

    private RecyclerView patientRecyclerView;
    private TextView patientTitleTextView;
    private PatientListRecyclerViewAdapter patientListRecyclerViewAdapter;
    private ArrayList<Patient> patients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        patientRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        patientTitleTextView = (TextView) findViewById(R.id.patient_list_title);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        patientRecyclerView.setLayoutManager(layoutManager);

        makePatientList();

        String patient_list_title = String.format(getResources().getString(R.string.patient_list_title), Integer.toString(patients.size()));
        patientTitleTextView.setText(Html.fromHtml(patient_list_title));

        patientListRecyclerViewAdapter = new PatientListRecyclerViewAdapter(this,patients);

        patientRecyclerView.setAdapter(patientListRecyclerViewAdapter);
    }

    private void makePatientList() {
        patients = new ArrayList<>();
        patients.add(new Patient("Sidharth Sethia","1234","Self","7023479993"));
        patients.add(new Patient("Shobha Sethia","1234","Mother","7023479993"));
        patients.add(new Patient("Sushil Sethia","1234","Father","7023479993"));
        patients.add(new Patient("Rishabh Sethia","1234","Brother","7023479993"));
    }
}
