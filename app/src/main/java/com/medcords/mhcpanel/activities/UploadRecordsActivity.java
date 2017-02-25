package com.medcords.mhcpanel.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.medcords.mhcpanel.database.DatabaseHandler;
import com.medcords.mhcpanel.database.ImageRecord;
import com.medcords.mhcpanel.services.ImageUploadService;
import com.medcords.mhcpanel.utilities.Constants;
import com.medcords.mhcpanel.utilities.Utility;
import com.medcords.mhcpanel.views.DatePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    private long recordsDate;

    //Presently hardcoded. To be fetched from shared preferences or via intent.
    private int medCordsId = 1234;

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

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(UploadRecordsActivity.this);

                if (sharedPreferences.contains("lastRecordsUpdatedForId") && sharedPreferences.getInt("lastRecordsUpdatedForId",0) == medCordsId){
                    inputLayoutOTP.setVisibility(View.GONE);
                    mResendOTPTextView.setVisibility(View.GONE);
                    mOTPTitleTextView.setVisibility(View.GONE);
                }



                mOTPDialogNameTextView.setText(mDoctorEditText.getText().toString());
                mOTPSubscriptionTextView.setText("No of Images: " + Integer.toString(imagesAddressList.size()));

                mOTPSubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        DatabaseHandler db = new DatabaseHandler(UploadRecordsActivity.this);

                        String batchId = UUID.randomUUID().toString();

                        for (String path : imagesAddressList){
                            ImageRecord imageRecord = new ImageRecord();

                            imageRecord.setPath(path);
                            imageRecord.setBatchId(batchId);
                            imageRecord.setDoctorName(mDoctorEditText.getText().toString().trim());
                            imageRecord.setPatientId(Integer.toString(medCordsId));
                            imageRecord.setDocType(getDocType());
                            imageRecord.setReportType(mReportTypeText.getText().toString());
                            imageRecord.setDate(Long.toString(recordsDate));
                            imageRecord.setTags(Utility.convertArrayToString(selectedTags.toArray(new String[selectedTags.size()])));
                            imageRecord.setHasBeenUploaded(Constants.FLAG_UPLOAD_FALSE);
                            db.addImage(imageRecord);
                            imageRecord.setId(db.getImageId(path));

                            Intent uploadImageIntent = new Intent(UploadRecordsActivity.this, ImageUploadService.class);
                            uploadImageIntent.putExtra("imageId", imageRecord.getId());
                            startService(uploadImageIntent);
                        }

                        /*List<ImageRecord> imageRecords = db.getAllImages();
                        db.deleteImage(imageRecords.get(3));
                        for (ImageRecord imageRecord : imageRecords){
                            Log.e("Id", "" + imageRecord.getId());
                        }*/

                        //Log.e("Db size", "" + db.getImageCount());

                        Toast toast = Toast.makeText(UploadRecordsActivity.this,"Records uploaded successfully.", Toast.LENGTH_LONG);
                        toast.show();

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(UploadRecordsActivity.this);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("lastRecordsUpdatedForId", medCordsId);
                        editor.commit();

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
        recordsDate = c.getTimeInMillis();
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

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,day);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.YEAR,year);
        recordsDate = calendar.getTimeInMillis();

        String date = day+"/"+(month+1)+"/"+year;
        mDateEditText.setText(date);
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

        finish();
    }

    private String getDocType(){
        switch (mDocTypeRadioGroup.getCheckedRadioButtonId()){
            case R.id.input_report :
                return "Report";
            case R.id.input_prescription :
                return "Prescription";
            case R.id.input_bill :
                return "Bill";
            default:
                return "Prescription";
        }
    }
}
