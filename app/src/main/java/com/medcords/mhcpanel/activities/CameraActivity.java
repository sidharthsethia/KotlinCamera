package com.medcords.mhcpanel.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medcords.mhcpanel.R;
import com.medcords.mhcpanel.utilities.Constants;
import com.medcords.mhcpanel.utilities.Utility;
import com.medcords.mhcpanel.views.CameraPreview;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
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

    private Boolean afterActivityResult = false;

    private static String TAG = CameraPreview.class.getName();

    public static int CAMERA_PREVIEW_ACTIVITY = 2;

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

            Bitmap picture = null;

            if (data != null) {
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                int screenHeight = getResources().getDisplayMetrics().heightPixels;
                picture = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // Notice that width and height are reversed
                    Bitmap scaled = Bitmap.createScaledBitmap(picture, screenHeight, screenWidth, true);
                    int w = scaled.getWidth();
                    int h = scaled.getHeight();
                    // Setting post rotate to 90
                    Matrix mtx = new Matrix();

                    int CameraEyeValue = Utility.setPhotoOrientation(CameraActivity.this, 0); // CameraID = 1 : front 0:back
                    Boolean cameraFront = false;
                    if(cameraFront) { // As Front camera is Mirrored so Fliping the Orientation
                        if (CameraEyeValue == 270) {
                            mtx.postRotate(90);
                        } else if (CameraEyeValue == 90) {
                            mtx.postRotate(270);
                        }
                    }else{
                        mtx.postRotate(CameraEyeValue); // CameraEyeValue is default to Display Rotation
                    }

                    picture = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
                }else{// LANDSCAPE MODE
                    //No need to reverse width and height
                    Bitmap scaled = Bitmap.createScaledBitmap(picture, screenWidth, screenHeight, true);
                    picture=scaled;
                }
            }
            //Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);
            File mPictureFile = Utility.getOutputMediaFile(Constants.MEDIA_TYPE_IMAGE);
            try {

                FileOutputStream out = new FileOutputStream(mPictureFile);
                picture.compress(Bitmap.CompressFormat.JPEG, 100, out);

                Bitmap compressedImageBitmap = Compressor.getDefault(CameraActivity.this).compressToBitmap(mPictureFile);
                File mCompressedPictureFile = Utility.getOutputMediaFile(Constants.MEDIA_TYPE_CMP_IMAGE);
                FileOutputStream out2 = new FileOutputStream(mCompressedPictureFile);
                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);

                Utility.deleteImage(mPictureFile.getAbsolutePath());

                imagePreview.setImageBitmap(compressedImageBitmap);
                imagePreview.setVisibility(View.VISIBLE);
                imagesAddressList.add(mCompressedPictureFile.getAbsolutePath());

                mPreview.safeToTakePicture = true;

                Intent intent=new Intent(CameraActivity.this,ImagePreviewActivity.class);
                intent.putExtra("FilePath",mCompressedPictureFile.getAbsolutePath());
                startActivityForResult(intent, CAMERA_PREVIEW_ACTIVITY);

            } catch (Exception e) {
                e.printStackTrace();
                mPreview.safeToTakePicture = true;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

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
        afterActivityResult = false;
        Intent intent = new Intent(this, UploadRecordsActivity.class);
        intent.putStringArrayListExtra("images_address_list", imagesAddressList);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!afterActivityResult){

            if(checkCameraHardware(this)){
                releaseCamera();
                mCamera = getCameraInstance();
                Utility.setCameraDisplayOrientation(CameraActivity.this,0,mCamera);
            } else {
                Toast.makeText(this,"No camera Found",Toast.LENGTH_SHORT).show();
            }

            mPreview = new CameraPreview(this, mCamera);
            preview.addView(mPreview);

            imagesAddressList = new ArrayList<>();

            noOfImagesTaken = 0;
            imagePreview.setVisibility(View.GONE);
            noOfImagesTextView.setVisibility(View.GONE);
            doneButton.setVisibility(View.GONE);
            batchModeLayout.setVisibility(View.VISIBLE);
            singleModeLayout.setVisibility(View.VISIBLE);
        } else {
            afterActivityResult = false;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        afterActivityResult = true;
        if(requestCode == CAMERA_PREVIEW_ACTIVITY)
        {
            String action = data.getStringExtra("action");

            if(action.equals("retake")){
                String address = imagesAddressList.get(imagesAddressList.size()-1);
                imagesAddressList.remove(imagesAddressList.size()-1);
                Utility.deleteImage(address);
                noOfImagesTaken--;
            }

            releaseCamera();
            mCamera = getCameraInstance();
            Utility.setCameraDisplayOrientation(CameraActivity.this,0,mCamera);
            mCamera.startPreview();
            mPreview = new CameraPreview(this, mCamera);
            preview.addView(mPreview);


            if(singleModeOn && noOfImagesTaken == 1){
                mCamera.stopPreview();
                openUploadRecordsActivity();
            }
            else {
                if(singleModeOn && noOfImagesTaken == 0){
                    imagePreview.setVisibility(View.GONE);
                    noOfImagesTextView.setVisibility(View.GONE);
                    doneButton.setVisibility(View.GONE);
                    batchModeLayout.setVisibility(View.VISIBLE);
                    singleModeLayout.setVisibility(View.VISIBLE);
                } else {
                    imagePreview.setVisibility(View.VISIBLE);
                    noOfImagesTextView.setVisibility(View.INVISIBLE);
                    doneButton.setVisibility(View.VISIBLE);
                    batchModeLayout.setVisibility(View.GONE);
                    singleModeLayout.setVisibility(View.GONE);

                    if(noOfImagesTaken > 1){
                        noOfImagesTextView.setVisibility(View.VISIBLE);
                        noOfImagesTextView.setText(Integer.toString(noOfImagesTaken));
                    }

                    if(noOfImagesTaken > 0){
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeFile(imagesAddressList.get(noOfImagesTaken-1), options);
                        imagePreview.setImageBitmap(bitmap);
                    }

                }

            }

        }
    }
}
