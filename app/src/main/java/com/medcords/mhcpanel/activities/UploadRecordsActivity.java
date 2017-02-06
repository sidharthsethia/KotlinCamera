package com.medcords.mhcpanel.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.medcords.mhcpanel.R;
import com.medcords.mhcpanel.adapters.ImageAdapter;
import com.medcords.mhcpanel.views.DatePickerFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import eu.fiskur.chipcloud.ChipCloud;
import eu.fiskur.chipcloud.ChipListener;

public class UploadRecordsActivity extends AppCompatActivity implements DatePickerFragment.DateSetListener{


    RecyclerView mGridView;
    List<String> tags, selectedTags;
    private TextView noTagsAddedTextView;
    private TextInputLayout inputLayoutDoctor, inputLayoutReportType, inputLayoutDate;
    private EditText mDoctorEditText, mReportTypeText, mDateEditText;
    private RadioGroup mDocTypeRadioGroup;
    private Button mUploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_records);

        final ArrayList<String> imagesAddressList = getIntent().getStringArrayListExtra("images_address_list");
        mGridView = (RecyclerView) findViewById(R.id.recycler_view);
        mGridView.setLayoutManager(new GridLayoutManager(UploadRecordsActivity.this,3));
        mGridView.setAdapter(new ImageAdapter(this,imagesAddressList));

        noTagsAddedTextView = (TextView) findViewById(R.id.no_tags_added);
        noTagsAddedTextView.setVisibility(View.VISIBLE);

        inputLayoutDoctor = (TextInputLayout) findViewById(R.id.input_layout_doctor);
        inputLayoutDate = (TextInputLayout) findViewById(R.id.input_layout_date);
        inputLayoutReportType = (TextInputLayout) findViewById(R.id.input_layout_type_of_record);

        mDateEditText = (EditText) findViewById(R.id.input_date);
        mDoctorEditText = (EditText) findViewById(R.id.input_doctor);
        mReportTypeText = (EditText) findViewById(R.id.input_type_of_record);

        mUploadButton = (Button) findViewById(R.id.upload_button);

        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(UploadRecordsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_new_user_otp);

                Button mOTPSubmitButton = (Button) dialog.findViewById(R.id.submit_button);
                EditText mOTPEditText = (EditText) dialog.findViewById(R.id.input_otp);
                TextView mResendOTPTextView = (TextView) dialog.findViewById(R.id.resend_otp_text_view);
                TextView mOTPTitleTextView = (TextView) dialog.findViewById(R.id.otp_text_view);
                TextInputLayout inputLayoutOTP = (TextInputLayout) dialog.findViewById(R.id.input_layout_otp);
                TextView mOTPDialogNameTextView = (TextView) dialog.findViewById(R.id.name_text_view);
                TextView mOTPSubscriptionTextView = (TextView) dialog.findViewById(R.id.subscription_text_view);
                TextView mOTPCancelTextView = (TextView) dialog.findViewById(R.id.cancel_text_view);

                String otp_title = String.format(getResources().getString(R.string.otp_title), "7023479993");
                mOTPTitleTextView.setText(Html.fromHtml(otp_title));


                mOTPDialogNameTextView.setText(mDoctorEditText.getText().toString());
                mOTPSubscriptionTextView.setText("No of Images: " + Integer.toString(imagesAddressList.size()));

                mOTPSubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Toast toast = Toast.makeText(UploadRecordsActivity.this,"Records uploaded successfully.", Toast.LENGTH_LONG);
                        toast.show();

                        Intent i = new Intent(UploadRecordsActivity.this, PatientActionsActivity.class);
                        i.putExtra("new_user",false);
                        startActivity(i);
                    }
                });

                mOTPCancelTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        mDoctorEditText.setFocusable(false);
        mDateEditText.setFocusable(false);
        mReportTypeText.setFocusable(false);

        inputLayoutReportType.setVisibility(View.GONE);

        mDocTypeRadioGroup = (RadioGroup) findViewById(R.id.input_doc_type);

        mDocTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if( i == R.id.input_report)
                    inputLayoutReportType.setVisibility(View.VISIBLE);
                else
                    inputLayoutReportType.setVisibility(View.GONE);
            }
        });

        mDoctorEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(UploadRecordsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_list_view);

                TextView title = (TextView) dialog.findViewById(R.id.title);
                title.setText("Select Doctor");
                ListView lv = (ListView) dialog.findViewById(R.id.listView1);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(UploadRecordsActivity.this, android.R.layout.simple_list_item_1, tags);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        dialog.dismiss();
                        mDoctorEditText.setText(tags.get(i));
                    }
                });
                dialog.show();
            }
        });

        mReportTypeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(UploadRecordsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_list_view);

                TextView title = (TextView) dialog.findViewById(R.id.title);
                title.setText("Select Report Type");

                ListView lv = (ListView) dialog.findViewById(R.id.listView1);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(UploadRecordsActivity.this, android.R.layout.simple_list_item_1, tags);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        dialog.dismiss();
                        mReportTypeText.setText(tags.get(i));
                    }
                });
                dialog.show();
            }
        });

        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DialogFragment datePickerFragment = new DatePickerFragment();

                Bundle b = new Bundle();

                /** Storing the selected item's index in the bundle object */
                b.putInt("year", year);
                b.putInt("month", month);
                b.putInt("day", day);

                /** Setting the bundle object to the dialog fragment object */
                datePickerFragment.setArguments(b);


                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String date = day+"/"+(month+1)+"/"+year;
        mDateEditText.setText(date);


        setTags();
        selectedTags = new ArrayList<>();

        ChipCloud chipCloud = (ChipCloud) findViewById(R.id.chip_cloud);

        new ChipCloud.Configure()
                .chipCloud(chipCloud)
                .selectedColor(getResources().getColor(R.color.colorPrimaryDark))
                .selectedFontColor(getResources().getColor(R.color.white))
                .deselectedColor(Color.parseColor("#e1e1e1"))
                .deselectedFontColor(Color.parseColor("#333333"))
                .selectTransitionMS(100)
                .deselectTransitionMS(100)
                .labels(tags.toArray(new String[tags.size()]))
                .mode(ChipCloud.Mode.MULTI)
                .chipListener(new ChipListener() {
                    @Override
                    public void chipSelected(int index) {
                        //...
                        selectedTags.add(tags.get(index));
                        if(selectedTags.size()>0)
                            noTagsAddedTextView.setVisibility(View.GONE);
                    }
                    @Override
                    public void chipDeselected(int index) {
                        //...
                        selectedTags.remove(tags.get(index));
                        if(selectedTags.size() == 0)
                            noTagsAddedTextView.setVisibility(View.GONE);
                    }
                })
                .build();
    }

    public void setTags(){
        tags = new ArrayList<>();
        tags.add("Tag 1");
        tags.add("Tag 2");
        tags.add("Tag 3");
        tags.add("Tag 4");
        tags.add("Tag 5");
        tags.add("Tag 6");
        tags.add("Tag 7");
        tags.add("Tag 8");
        tags.add("Tag 9");
        tags.add("Tag 10");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String date = day+"/"+(month+1)+"/"+year;
        mDateEditText.setText(date);
    }
}
