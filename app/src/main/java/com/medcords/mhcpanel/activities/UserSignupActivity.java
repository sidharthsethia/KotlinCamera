package com.medcords.mhcpanel.activities;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.medcords.mhcpanel.R;
import com.medcords.mhcpanel.utilities.GPSTracker;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class UserSignupActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button mSignupButton, mOTPSubmitButton;
    private EditText mOTPEditText, mPhoneEditText, mNameEditText, mPlaceOfLivingEditText, mDOBEditText, mAadharEditText, mRelationshipEditText, mAgeEditText;
    private TextView mResendOTPTextView, mLoginTextView, mOTPTitleTextView;
    private CircularImageView mProfileImageView;
    private TextInputLayout inputLayoutName, inputLayoutPlace, inputLayoutPhone, inputLayoutOTP, inputLayoutDOB, inputLayoutRelationship, inputLayoutAadhar, inputLayoutAge;
    private double latitude=-1000,longitude=-1000;
    private CheckBox ageUnknownCheckbox;
    private RadioGroup mGenderRadioGroup;

    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private ArrayList<String>  relationNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        mSignupButton = (Button) findViewById(R.id.register_button);
        mOTPSubmitButton = (Button) findViewById(R.id.submit_button);
        mOTPEditText = (EditText) findViewById(R.id.input_otp);
        mPhoneEditText = (EditText) findViewById(R.id.input_mobile);
        mAgeEditText = (EditText) findViewById(R.id.input_age);
        mNameEditText = (EditText) findViewById(R.id.input_name);
        mPlaceOfLivingEditText = (EditText) findViewById(R.id.input_place);
        mAadharEditText = (EditText) findViewById(R.id.input_aadhar);
        mRelationshipEditText = (EditText) findViewById(R.id.input_relationship);
        mDOBEditText = (EditText) findViewById(R.id.input_dob);
        mResendOTPTextView = (TextView) findViewById(R.id.resend_otp_text_view);
        mLoginTextView = (TextView) findViewById(R.id.login_text_view);
        mOTPTitleTextView = (TextView) findViewById(R.id.otp_text_view);
        mProfileImageView = (CircularImageView) findViewById(R.id.input_photo);
        inputLayoutAadhar = (TextInputLayout) findViewById(R.id.input_layout_aadhar);
        inputLayoutDOB = (TextInputLayout) findViewById(R.id.input_layout_dob);
        inputLayoutPlace = (TextInputLayout) findViewById(R.id.input_layout_place);
        inputLayoutRelationship = (TextInputLayout) findViewById(R.id.input_layout_relationship);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_mobile);
        inputLayoutAge = (TextInputLayout) findViewById(R.id.input_layout_age);
        inputLayoutOTP = (TextInputLayout) findViewById(R.id.input_layout_otp);
        ageUnknownCheckbox = (CheckBox) findViewById(R.id.age_unknown_checkbox);
        mGenderRadioGroup = (RadioGroup) findViewById(R.id.input_gender);

        mRelationshipEditText.setFocusable(false);
        mPlaceOfLivingEditText.setFocusable(false);
        mDOBEditText.setFocusable(false);

        ageUnknownCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    inputLayoutDOB.setVisibility(b ? View.GONE : View.VISIBLE);
                    inputLayoutAge.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });
        GPSTracker gps = new GPSTracker(this);
        // check if GPS enabled
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            // \n is for new line
            gps.stopUsingGPS();
        }else{
            gps.showSettingsAlert();
        }

        mDOBEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        UserSignupActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMaxDate(now);
                dpd.setTitle("Select Date");
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        mPlaceOfLivingEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                            .build();
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .setBoundsBias(new LatLngBounds(
                                        new LatLng(latitude, longitude),
                                        new LatLng(latitude, latitude)))
                                    .setFilter(typeFilter)
                                    .build(UserSignupActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        mRelationshipEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setRelationList(mGenderRadioGroup.getCheckedRadioButtonId());
                final Dialog dialog = new Dialog(UserSignupActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_list_view);

                ListView lv = (ListView) dialog.findViewById(R.id.listView1);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(UserSignupActivity.this, android.R.layout.simple_list_item_1, relationNames);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        dialog.dismiss();
                        mRelationshipEditText.setText(relationNames.get(i));
                    }
                });
                dialog.show();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                mPlaceOfLivingEditText.setText(place.getAddress());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("Error", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        mDOBEditText.setText(date);
    }

    private void setRelationList(int id){
        relationNames.clear();

        relationNames.add("Self");

        if (id != R.id.input_male) {
            relationNames.add("Mother");
            relationNames.add("Wife");
            relationNames.add("Sister");
            relationNames.add("Aunt");
            relationNames.add("Daughter");
        }

        if (id != R.id.input_female) {
            relationNames.add("Father");
            relationNames.add("Husband");
            relationNames.add("Brother");
            relationNames.add("Uncle");
            relationNames.add("Son");
        }
        relationNames.add("Cousin");
    }


}
