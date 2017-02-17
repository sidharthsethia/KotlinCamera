package com.medcords.mhcpanel.activities;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.medcords.mhcpanel.utilities.Utility;
import com.medcords.mhcpanel.views.DatePickerFragment;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Calendar;

import static com.medcords.mhcpanel.utilities.Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE;
import static com.medcords.mhcpanel.utilities.Constants.REQUEST_CAMERA;
import static com.medcords.mhcpanel.utilities.Constants.SELECT_FILE;
import static com.medcords.mhcpanel.utilities.Constants.SEND_PHOTO_URI_INTENT;
import static com.medcords.mhcpanel.utilities.Constants.VARIABLE_URI;

public class PatientSignupActivity extends AppCompatActivity implements DatePickerFragment.DateSetListener{

    private Button mSignupButton, mOTPSubmitButton;
    private EditText mOTPEditText, mPhoneEditText, mNameEditText, mPlaceOfLivingEditText, mDOBEditText, mAadharEditText, mRelationshipEditText, mAgeEditText;
    private TextView mResendOTPTextView, mLoginTextView, mOTPTitleTextView, mOTPDialogNameTextView, mOTPSubscriptionTextView, mOTPCancelTextView;
    private CircularImageView mProfileImageView;
    private TextInputLayout inputLayoutName, inputLayoutPlace, inputLayoutPhone, inputLayoutOTP, inputLayoutDOB, inputLayoutRelationship, inputLayoutAadhar, inputLayoutAge;
    private double latitude=-1000,longitude=-1000;
    private CheckBox ageUnknownCheckbox;
    private RadioGroup mGenderRadioGroup, mSubscriptionRadioGroup;

    private Uri profilePhotoUri;

    private ArrayList<String>  relationNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        mSignupButton = (Button) findViewById(R.id.register_button);

        mPhoneEditText = (EditText) findViewById(R.id.input_mobile);
        mAgeEditText = (EditText) findViewById(R.id.input_age);
        mNameEditText = (EditText) findViewById(R.id.input_name);
        mPlaceOfLivingEditText = (EditText) findViewById(R.id.input_place);
        mAadharEditText = (EditText) findViewById(R.id.input_aadhar);
        mRelationshipEditText = (EditText) findViewById(R.id.input_relationship);
        mDOBEditText = (EditText) findViewById(R.id.input_dob);

        mLoginTextView = (TextView) findViewById(R.id.login_text_view);

        mProfileImageView = (CircularImageView) findViewById(R.id.input_photo);
        inputLayoutAadhar = (TextInputLayout) findViewById(R.id.input_layout_aadhar);
        inputLayoutDOB = (TextInputLayout) findViewById(R.id.input_layout_dob);
        inputLayoutPlace = (TextInputLayout) findViewById(R.id.input_layout_place);
        inputLayoutRelationship = (TextInputLayout) findViewById(R.id.input_layout_relationship);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_mobile);
        inputLayoutAge = (TextInputLayout) findViewById(R.id.input_layout_age);

        ageUnknownCheckbox = (CheckBox) findViewById(R.id.age_unknown_checkbox);
        mGenderRadioGroup = (RadioGroup) findViewById(R.id.input_gender);
        mSubscriptionRadioGroup = (RadioGroup) findViewById(R.id.input_subscription);

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
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DialogFragment datePickerFragment = new DatePickerFragment();

                Bundle b = new Bundle();

                /** Storing the selected item's index in the bundle object */
                b.putInt("year", year - 40);
                b.putInt("month", month);
                b.putInt("day", day);

                /** Setting the bundle object to the dialog fragment object */
                datePickerFragment.setArguments(b);


                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
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
                                    .build(PatientSignupActivity.this);
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
                final Dialog dialog = new Dialog(PatientSignupActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_list_view);

                ListView lv = (ListView) dialog.findViewById(R.id.listView1);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(PatientSignupActivity.this, android.R.layout.simple_list_item_1, relationNames);
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

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(PatientSignupActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_new_user_otp);

                mOTPSubmitButton = (Button) dialog.findViewById(R.id.submit_button);
                mOTPEditText = (EditText) dialog.findViewById(R.id.input_otp);
                mResendOTPTextView = (TextView) dialog.findViewById(R.id.resend_otp_text_view);
                mOTPTitleTextView = (TextView) dialog.findViewById(R.id.otp_text_view);
                inputLayoutOTP = (TextInputLayout) dialog.findViewById(R.id.input_layout_otp);
                mOTPDialogNameTextView = (TextView) dialog.findViewById(R.id.name_text_view);
                mOTPSubscriptionTextView = (TextView) dialog.findViewById(R.id.subscription_text_view);
                mOTPCancelTextView = (TextView) dialog.findViewById(R.id.cancel_text_view);

                String otp_title = String.format(getResources().getString(R.string.otp_title), mPhoneEditText.getText().toString().trim());
                mOTPTitleTextView.setText(Html.fromHtml(otp_title));


                mOTPDialogNameTextView.setText(mNameEditText.getText().toString());
                mOTPSubscriptionTextView.setText("Subscription Term: " + (mSubscriptionRadioGroup.getCheckedRadioButtonId() == R.id.input_6_months ? "6 months" : "1 year"));

                mOTPSubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Toast toast = Toast.makeText(PatientSignupActivity.this,"User registered successfully.", Toast.LENGTH_LONG);
                        toast.show();

                        Intent i = new Intent(PatientSignupActivity.this, PatientActionsActivity.class);
                        i.putExtra("new_user",true);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                        finish();
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

        mProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.addProfilePicture(PatientSignupActivity.this);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case SELECT_FILE:
                if(resultCode == RESULT_OK){
                    profilePhotoUri = data.getData();
                    if(profilePhotoUri != null)
                        mProfileImageView.setImageURI(profilePhotoUri);
                }

                break;
            case REQUEST_CAMERA:
                if(resultCode == RESULT_OK){
                    if(profilePhotoUri != null)
                        mProfileImageView.setImageURI(profilePhotoUri);
                }
                break;
            case PLACE_AUTOCOMPLETE_REQUEST_CODE:
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
                break;
        }
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


    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String date = day+"/"+(month+1)+"/"+year;
        mDOBEditText.setText(date);
    }

    private BroadcastReceiver photoUriReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            profilePhotoUri = intent.getParcelableExtra(VARIABLE_URI);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(photoUriReceiver,
                new IntentFilter(SEND_PHOTO_URI_INTENT));
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(photoUriReceiver);
    }

}
