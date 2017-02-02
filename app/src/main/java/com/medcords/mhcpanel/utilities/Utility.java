package com.medcords.mhcpanel.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.medcords.mhcpanel.R;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

/**
 * Created by sidharthsethia on 27/12/16.
 */

public class Utility {

    private static final String IMAGE_DIRECTORY_NAME = "MedCords";

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

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.d("No camera Found", e.getMessage());
        }
        return c; // returns null if camera is unavailable
    }

    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    public static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static void colorizeDatePicker(DatePicker datePicker) {


        Resources system = Resources.getSystem();
        int dayId = system.getIdentifier("day", "id", "android");
        int monthId = system.getIdentifier("month", "id", "android");
        int yearId = system.getIdentifier("year", "id", "android");

        NumberPicker dayPicker = (NumberPicker) datePicker.findViewById(dayId);
        NumberPicker monthPicker = (NumberPicker) datePicker.findViewById(monthId);
        NumberPicker yearPicker = (NumberPicker) datePicker.findViewById(yearId);



        setDividerColor(dayPicker);
        setDividerColor(monthPicker);
        setDividerColor(yearPicker);
    }

    private static void setDividerColor(NumberPicker picker) {
        if (picker == null)
            return;

        final int count = picker.getChildCount();
        for (int i = 0; i < count; i++) {
            try {
                Field dividerField = picker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(picker.getResources().getColor(R.color.colorPrimaryDark));
                dividerField.set(picker, colorDrawable);
                picker.invalidate();
            } catch (Exception e) {
                Log.w("setDividerColor", e);
            }
        }
    }


}
