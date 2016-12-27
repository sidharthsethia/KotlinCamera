package com.medcords.mhcpanel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AgentLoginActivity extends AppCompatActivity {

    private RelativeLayout mOTPLayout, mLoginLayout;
    private Button mLoginButton, mOTPSubmitButton;
    private EditText mOTPEditText, mPhoneEditText;
    private ProgressBar mProgressBar;
    private TextView mResendOTPTextView, mSignupTextView;



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
        mResendOTPTextView = (TextView) findViewById(R.id.resend_otp_text_view);
        mSignupTextView = (TextView) findViewById(R.id.signup_text_view);

        mOTPLayout.setVisibility(View.GONE);
        mLoginLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginLayout.setVisibility(View.GONE);
                mOTPLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
        });

        mOTPSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
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
