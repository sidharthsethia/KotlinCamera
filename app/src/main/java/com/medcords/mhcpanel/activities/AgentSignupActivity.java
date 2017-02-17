package com.medcords.mhcpanel.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medcords.mhcpanel.R;
import com.medcords.mhcpanel.utilities.Utility;
import com.mikhaellopez.circularimageview.CircularImageView;

import static com.medcords.mhcpanel.utilities.Constants.REQUEST_CAMERA;
import static com.medcords.mhcpanel.utilities.Constants.SELECT_FILE;
import static com.medcords.mhcpanel.utilities.Constants.SEND_PHOTO_URI_INTENT;
import static com.medcords.mhcpanel.utilities.Constants.VARIABLE_URI;

public class AgentSignupActivity extends AppCompatActivity {

    private RelativeLayout mOTPLayout, mSignupLayout;
    private Button mSignupButton, mOTPSubmitButton;
    private EditText mOTPEditText, mPhoneEditText, mNameEditText, mEmailEditText,mCityEditText, mStateEditText, mPincodeEditText;
    private ProgressBar mProgressBar;
    private TextView mResendOTPTextView, mLoginTextView, mOTPTitleTextView;
    private CircularImageView mProfileImageView;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPhone, inputLayoutState, inputLayoutCity, inputLayoutPincode, inputLayoutOTP;
    private Uri profilePhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_signup);

        mOTPLayout = (RelativeLayout) findViewById(R.id.otp_layout);
        mSignupLayout = (RelativeLayout) findViewById(R.id.signup_layout);
        mSignupButton = (Button) findViewById(R.id.register_button);
        mOTPSubmitButton = (Button) findViewById(R.id.submit_button);
        mOTPEditText = (EditText) findViewById(R.id.input_otp);
        mPhoneEditText = (EditText) findViewById(R.id.input_mobile);
        mNameEditText = (EditText) findViewById(R.id.input_name);
        mEmailEditText = (EditText) findViewById(R.id.input_email);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mResendOTPTextView = (TextView) findViewById(R.id.resend_otp_text_view);
        mLoginTextView = (TextView) findViewById(R.id.login_text_view);
        mOTPTitleTextView = (TextView) findViewById(R.id.otp_text_view);
        mProfileImageView = (CircularImageView) findViewById(R.id.input_photo);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_mobile);
        inputLayoutOTP = (TextInputLayout) findViewById(R.id.input_layout_otp);

        inputLayoutCity = (TextInputLayout) findViewById(R.id.input_layout_city);
        inputLayoutState = (TextInputLayout) findViewById(R.id.input_layout_state);
        inputLayoutPincode = (TextInputLayout) findViewById(R.id.input_layout_pincode);

        mCityEditText = (EditText) findViewById(R.id.input_city);
        mStateEditText = (EditText) findViewById(R.id.input_state);
        mPhoneEditText = (EditText) findViewById(R.id.input_pincode);

        mOTPLayout.setVisibility(View.GONE);
        mSignupLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

        mOTPEditText.setBackgroundDrawable(getResources().getDrawable(R.drawable.apptheme_edit_text_holo_light));

        mNameEditText.addTextChangedListener(new SignupTextWatcher(mNameEditText));
        mEmailEditText.addTextChangedListener(new SignupTextWatcher(mEmailEditText));
        mPhoneEditText.addTextChangedListener(new SignupTextWatcher(mPhoneEditText));


        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });


        mOTPSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utility.validateOTP(mOTPEditText,inputLayoutOTP,AgentSignupActivity.this,AgentSignupActivity.this)) {
                    return;
                }

                Intent i = new Intent(AgentSignupActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        mLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AgentSignupActivity.this, AgentLoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        mProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.addProfilePicture(AgentSignupActivity.this);
            }
        });
    }

    private void submitForm() {
        if (!Utility.validateName(mNameEditText,inputLayoutName,AgentSignupActivity.this,AgentSignupActivity.this)) {
            return;
        }

        if (!Utility.validateEmail(mEmailEditText,inputLayoutEmail,AgentSignupActivity.this,AgentSignupActivity.this)) {
            return;
        }

        if (!Utility.validatePhone(mPhoneEditText,inputLayoutPhone,AgentSignupActivity.this,AgentSignupActivity.this)) {
            return;
        }

        if(mCityEditText.getText().toString().isEmpty()){
            inputLayoutCity.setError(getString(R.string.err_msg_city));
            Utility.requestFocus(mPhoneEditText, AgentSignupActivity.this);
            return;
        }

        if(mStateEditText.getText().toString().isEmpty()){
            inputLayoutState.setError(getString(R.string.err_msg_state));
            Utility.requestFocus(mStateEditText, AgentSignupActivity.this);
            return;
        }

        if(mPincodeEditText.getText().toString().isEmpty() || mPincodeEditText.getText().toString().length()<6){
            inputLayoutPincode.setError(getString(R.string.err_msg_pincode));
            Utility.requestFocus(mPincodeEditText, AgentSignupActivity.this);
            return;
        }

        String otp_title = String.format(getResources().getString(R.string.otp_title), mPhoneEditText.getText().toString().trim());
        mOTPTitleTextView.setText(Html.fromHtml(otp_title));


        mSignupLayout.setVisibility(View.GONE);
        mOTPLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

        //Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }


    private class SignupTextWatcher implements TextWatcher {

        private EditText view;

        private SignupTextWatcher(EditText view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    Utility.validateName(view,inputLayoutName,AgentSignupActivity.this,AgentSignupActivity.this);
                    break;
                case R.id.input_email:
                    Utility.validateEmail(view,inputLayoutEmail,AgentSignupActivity.this,AgentSignupActivity.this);
                    break;
                case R.id.input_mobile:
                    Utility.validatePhone(view,inputLayoutPhone,AgentSignupActivity.this,AgentSignupActivity.this);
                    break;
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Log.d("Inside activity result","true");
        switch(requestCode) {
            case SELECT_FILE:
                if(resultCode == RESULT_OK){
                    profilePhotoUri = imageReturnedIntent.getData();
                    if(profilePhotoUri != null)
                        mProfileImageView.setImageURI(profilePhotoUri);
                }

                break;
            case REQUEST_CAMERA:
                Log.d("Inside request camera","true");
                if(resultCode == RESULT_OK){
                    Log.d("Inside result ok",profilePhotoUri.getEncodedPath());
                    if(profilePhotoUri != null)
                        mProfileImageView.setImageURI(profilePhotoUri);
                }
                break;
        }
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
