package com.medcords.mhcpanel.activities;

import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medcords.mhcpanel.R;
import com.medcords.mhcpanel.views.RadioGroupGridView;

import java.util.ArrayList;
import java.util.List;

import eu.fiskur.chipcloud.ChipCloud;
import eu.fiskur.chipcloud.ChipListener;

public class ShareRecordsActivity extends AppCompatActivity {

    TextView mPatientId, mPatientName;
    private TextInputLayout inputLayoutDoctor;
    private AutoCompleteTextView mDoctorEditText;
    private RadioGroupGridView dateRangeRadioGroup;
    private ChipCloud mBodyTagLayout, mReportTypeTagLayout;
    private Button mShareButton;
    private LinearLayout mReportTypeLayout;
    private CheckBox mReportCheckBox, mPrescriptionCheckBox, mBillCheckbox;

    List<String> reportTypeTags, selectedReportTypeTags, bodyTags,selectedBodyTags;

    String[] arr = {"Dr. Grant Anderson",
            "Dr. Kenneth Ross",
            "Dr. Kevin Reynolds",
            "Dr. Helen White",
            "Dr. Janice Brown",
            "Dr. Kathleen Jones" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_records);

        mDoctorEditText = (AutoCompleteTextView) findViewById(R.id.input_doctor);
        inputLayoutDoctor = (TextInputLayout) findViewById(R.id.input_layout_doctor);

        mPatientId = (TextView) findViewById(R.id.patient_id);
        mPatientName = (TextView) findViewById(R.id.patient_name);

        mShareButton = (Button) findViewById(R.id.share_button);

        dateRangeRadioGroup = (RadioGroupGridView) findViewById(R.id.date_radio_group);

        mReportTypeLayout = (LinearLayout) findViewById(R.id.report_type_layout);

        mBodyTagLayout = (ChipCloud) findViewById(R.id.body_tags_chip_cloud);
        mReportTypeTagLayout = (ChipCloud) findViewById(R.id.report_type_chip_cloud);

        mReportCheckBox = (CheckBox) findViewById(R.id.report_type);
        mPrescriptionCheckBox = (CheckBox) findViewById(R.id.prescription_type);
        mBillCheckbox = (CheckBox) findViewById(R.id.bill_type);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,R.layout.autocomplete_text_view, arr);

        mDoctorEditText.setThreshold(2);
        mDoctorEditText.setAdapter(adapter);


        mReportCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    mReportTypeLayout.setVisibility(View.VISIBLE);
                }

                else {
                    mReportTypeLayout.setVisibility(View.GONE);
                }

            }
        });

        setTags();
        selectedBodyTags = new ArrayList<>();
        selectedReportTypeTags = new ArrayList<>();

        new ChipCloud.Configure()
                .chipCloud(mBodyTagLayout)
                .selectedColor(getResources().getColor(R.color.colorPrimaryDark))
                .selectedFontColor(getResources().getColor(R.color.white))
                .deselectedColor(Color.parseColor("#e1e1e1"))
                .deselectedFontColor(Color.parseColor("#333333"))
                .selectTransitionMS(100)
                .deselectTransitionMS(100)
                .labels(bodyTags.toArray(new String[bodyTags.size()]))
                .mode(ChipCloud.Mode.MULTI)
                .chipListener(new ChipListener() {
                    @Override
                    public void chipSelected(int index) {
                        //...
                        selectedBodyTags.add(bodyTags.get(index));
                    }
                    @Override
                    public void chipDeselected(int index) {
                        //...
                        selectedBodyTags.remove(bodyTags.get(index));
                    }
                })
                .build();

        new ChipCloud.Configure()
                .chipCloud(mReportTypeTagLayout)
                .selectedColor(getResources().getColor(R.color.colorPrimaryDark))
                .selectedFontColor(getResources().getColor(R.color.white))
                .deselectedColor(Color.parseColor("#e1e1e1"))
                .deselectedFontColor(Color.parseColor("#333333"))
                .selectTransitionMS(100)
                .deselectTransitionMS(100)
                .labels(reportTypeTags.toArray(new String[reportTypeTags.size()]))
                .mode(ChipCloud.Mode.MULTI)
                .chipListener(new ChipListener() {
                    @Override
                    public void chipSelected(int index) {
                        //...
                        selectedReportTypeTags.add(reportTypeTags.get(index));
                    }
                    @Override
                    public void chipDeselected(int index) {
                        //...
                        selectedReportTypeTags.remove(reportTypeTags.get(index));
                    }
                })
                .build();


    }

    public void setTags(){
        bodyTags = new ArrayList<>();
        bodyTags.add("Tag 1");
        bodyTags.add("Tag 2");
        bodyTags.add("Tag 3");
        bodyTags.add("Tag 4");
        bodyTags.add("Tag 5");
        bodyTags.add("Tag 6");
        bodyTags.add("Tag 7");
        bodyTags.add("Tag 8");
        bodyTags.add("Tag 9");
        bodyTags.add("Tag 10");
        reportTypeTags = new ArrayList<>();
        reportTypeTags.addAll(bodyTags);
    }

}
