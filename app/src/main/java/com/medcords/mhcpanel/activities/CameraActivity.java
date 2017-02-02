package com.medcords.mhcpanel.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medcords.mhcpanel.R;
import com.medcords.mhcpanel.utilities.Utility;
import com.medcords.mhcpanel.views.CameraPreview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.medcords.mhcpanel.utilities.Utility.checkCameraHardware;
import static com.medcords.mhcpanel.utilities.Utility.getCameraInstance;

public class CameraActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private ImageButton captureButton, flashButton, singleModeButton, batchModeButton,doneButton;
    private View singleModeDot, batchModeDot;
    private ImageView imagePreview;
    private TextView noOfImagesTextView;

    private LinearLayout singleModeLayout,batchModeLayout;
    private FrameLayout preview;

    private Boolean flashOn = false;
    private Boolean singleModeOn = true;

    private int noOfImagesTaken = 0;
    private ArrayList<String> imagesAddressList;


    private static String TAG = CameraPreview.class.getName();

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            noOfImagesTaken++;

            if(!singleModeOn && noOfImagesTaken == 1){
                batchModeLayout.setVisibility(View.GONE);
                singleModeLayout.setVisibility(View.GONE);
                doneButton.setVisibility(View.VISIBLE);
            }

            if(noOfImagesTaken > 1){
                noOfImagesTextView.setVisibility(View.VISIBLE);
                noOfImagesTextView.setText(Integer.toString(noOfImagesTaken));
            }

            Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);
            File mPictureFile = Utility.getOutputMediaFile(MEDIA_TYPE_IMAGE);
            try {
                FileOutputStream out = new FileOutputStream(mPictureFile);
                picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                imagePreview.setImageBitmap(picture);
                imagePreview.setVisibility(View.VISIBLE);
                imagesAddressList.add(mPictureFile.getAbsolutePath());

                if(singleModeOn){
                    mCamera.stopPreview();
                    openUploadRecordsActivity();
                }
                else
                    mCamera.startPreview();

                mPreview.safeToTakePicture = true;

            } catch (Exception e) {
                e.printStackTrace();
                mPreview.safeToTakePicture = true;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_preview);

       /* if(checkCameraHardware(this)){
            releaseCamera();
            mCamera = getCameraInstance();
        } else {
            Toast.makeText(this,"No camera Found",Toast.LENGTH_SHORT).show();
        }

        imagesAddressList = new ArrayList<>();


        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
*/
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        captureButton = (ImageButton) findViewById(R.id.button_capture);
        flashButton = (ImageButton) findViewById(R.id.flash_button);
        singleModeButton = (ImageButton) findViewById(R.id.single_mode);
        batchModeButton = (ImageButton) findViewById(R.id.batch_mode);
        doneButton = (ImageButton) findViewById(R.id.done);

        imagePreview = (ImageView) findViewById(R.id.preview);
        imagePreview.setVisibility(View.GONE);

        doneButton.setVisibility(View.GONE);

        singleModeDot = (View) findViewById(R.id.single_mode_dot);
        batchModeDot = (View) findViewById(R.id.batch_mode_dot);

        singleModeLayout = (LinearLayout) findViewById(R.id.single_mode_layout);
        batchModeLayout = (LinearLayout) findViewById(R.id.batch_mode_layout);

        noOfImagesTextView = (TextView) findViewById(R.id.photo_number_indicator);
        noOfImagesTextView.setVisibility(View.GONE);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUploadRecordsActivity();
            }
        });

        singleModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleModeDot.setVisibility(View.VISIBLE);
                batchModeDot.setVisibility(View.INVISIBLE);
                singleModeOn = true;

                Toast toast = Toast.makeText(CameraActivity.this,"Single Mode On", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        batchModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleModeDot.setVisibility(View.INVISIBLE);
                batchModeDot.setVisibility(View.VISIBLE);
                singleModeOn = false;

                Toast toast = Toast.makeText(CameraActivity.this,"Batch Mode On", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flashOn){
                    flashOn = true;
                    flashButton.setImageResource(R.mipmap.flash_on_filled);
                    mPreview.turnOnFlash();
                } else {
                    flashOn = false;
                    flashButton.setImageResource(R.mipmap.flash_off_filled);
                    mPreview.turnOFfFlash();
                }
            }
        });

        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        if(mPreview.safeToTakePicture && mCamera != null){
                            mCamera.takePicture(null, null, mPicture);
                            mPreview.safeToTakePicture = false;
                        }

                    }
                }
        );

    }

    private void releaseCamera(){

        if (mCamera != null){
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    public void openUploadRecordsActivity(){
        Intent intent = new Intent(this, UploadRecordsActivity.class);
        intent.putStringArrayListExtra("images_address_list", imagesAddressList);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkCameraHardware(this)){
            releaseCamera();
            mCamera = getCameraInstance();
        } else {
            Toast.makeText(this,"No camera Found",Toast.LENGTH_SHORT).show();
        }

        imagesAddressList = new ArrayList<>();


        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        preview.addView(mPreview);

        noOfImagesTaken = 0;
        imagePreview.setVisibility(View.GONE);
        noOfImagesTextView.setVisibility(View.GONE);
        doneButton.setVisibility(View.GONE);
        batchModeLayout.setVisibility(View.VISIBLE);
        singleModeLayout.setVisibility(View.VISIBLE);
    }
}
