package com.medcords.mhcpanel.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medcords.mhcpanel.R;
import com.medcords.mhcpanel.utilities.Utility;

public class AgentLoginActivity extends AppCompatActivity {

    private RelativeLayout mOTPLayout, mLoginLayout;
    private Button mLoginButton, mOTPSubmitButton;
    private EditText mOTPEditText, mPhoneEditText;
    private ProgressBar mProgressBar;
    private TextView mResendOTPTextView, mSignupTextView, mOTPTitleTextView, mCancelTextView;
    private TextInputLayout inputLayoutPhone, inputLayoutOTP;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_login);

        mOTPLayout = (RelativeLayout) findViewById(R.id.otp_layout);
        mLoginLayout = (RelativeLayout) findViewById(R.id.login_layout);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mOTPSubmitButton = (Button) findViewById(R.id.submit_button);
        mOTPEditText = (EditText) findViewById(R.id.input_otp);
        mPhoneEditText = (EditText) findViewById(R.id.input_mobile);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mOTPTitleTextView = (TextView) findViewById(R.id.otp_text_view);
        mCancelTextView = (TextView) findViewById(R.id.cancel_textview);
        mResendOTPTextView = (TextView) findViewById(R.id.resend_otp_text_view);
        mSignupTextView = (TextView) findViewById(R.id.signup_text_view);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_mobile);
        inputLayoutOTP = (TextInputLayout) findViewById(R.id.input_layout_otp);

        mOTPLayout.setVisibility(View.GONE);
        mLoginLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

        mCancelTextView.setVisibility(View.VISIBLE);

        mOTPEditText.setBackgroundDrawable(getResources().getDrawable(R.drawable.apptheme_edit_text_holo_light));

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Utility.validatePhone(mPhoneEditText,inputLayoutPhone,AgentLoginActivity.this,AgentLoginActivity.this)) {
                    return;
                }

                String otp_title = String.format(getResources().getString(R.string.otp_title), mPhoneEditText.getText().toString().trim());
                mOTPTitleTextView.setText(Html.fromHtml(otp_title));

                mLoginLayout.setVisibility(View.GONE);
                mOTPLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);


            }
        });

        mOTPSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utility.validateOTP(mOTPEditText,inputLayoutOTP,AgentLoginActivity.this,AgentLoginActivity.this)) {
                    return;
                }

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AgentLoginActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("userLoggedIn", true);
                editor.commit();

                Intent i = new Intent(AgentLoginActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                AgentLoginActivity.this.overridePendingTransition(0, 0);
                finish();

            }
        });

        mCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOTPLayout.setVisibility(View.GONE);
                mLoginLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
        });

        mSignupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AgentLoginActivity.this, AgentSignupActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
