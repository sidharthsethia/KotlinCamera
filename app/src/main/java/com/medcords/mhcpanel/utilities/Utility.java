package com.medcords.mhcpanel.utilities;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.medcords.mhcpanel.R;

/**
 * Created by sidharthsethia on 27/12/16.
 */

public class Utility {

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void requestFocus(View view, Activity activity) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static boolean validateName(EditText mNameEditText, TextInputLayout inputLayoutName, Context context, Activity activity) {
        if (mNameEditText.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(context.getString(R.string.err_msg_name));
            Utility.requestFocus(mNameEditText, activity);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validateEmail(EditText mEmailEditText, TextInputLayout inputLayoutEmail, Context context, Activity activity) {
        String email = mEmailEditText.getText().toString().trim();

        if (email.isEmpty() || !Utility.isValidEmail(email)) {
            inputLayoutEmail.setError(context.getString(R.string.err_msg_email));
            Utility.requestFocus(mEmailEditText, activity);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validatePhone(EditText mPhoneEditText, TextInputLayout inputLayoutPhone, Context context, Activity activity) {
        if (mPhoneEditText.getText().toString().trim().isEmpty() || mPhoneEditText.getText().toString().trim().length() != 10) {
            inputLayoutPhone.setError(context.getString(R.string.err_msg_phone));
            Utility.requestFocus(mPhoneEditText, activity);
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validateOTP(EditText mPhoneEditText, TextInputLayout inputLayoutPhone, Context context, Activity activity) {
        if (mPhoneEditText.getText().toString().trim().isEmpty() || mPhoneEditText.getText().toString().trim().length() != Constants.OTP_LENGTH) {
            inputLayoutPhone.setError(context.getString(R.string.err_msg_otp));
            Utility.requestFocus(mPhoneEditText, activity);
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }

        return true;
    }

}
