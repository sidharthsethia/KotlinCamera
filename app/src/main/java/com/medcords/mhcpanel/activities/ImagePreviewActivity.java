package com.medcords.mhcpanel.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.medcords.mhcpanel.R;
import com.medcords.mhcpanel.utilities.Utility;

import java.io.File;
import java.io.FileOutputStream;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class ImagePreviewActivity extends AppCompatActivity {

    private ImageButton retakeButton, rotateButton, doneButton;
    private ImageView imagePreview;

    private String filePath;

    private Bitmap bitmap;

    private Boolean rotated = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        imagePreview = (ImageView) findViewById(R.id.camera_preview);
        retakeButton = (ImageButton) findViewById(R.id.retake_button);
        rotateButton = (ImageButton) findViewById(R.id.rotate_button);
        doneButton = (ImageButton) findViewById(R.id.done);

        filePath = getIntent().getExtras().getString("FilePath");

        BitmapFactory.Options options = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(filePath,options);
        imagePreview.setImageBitmap(bitmap);

        retakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("action","retake");
                setResult(CameraActivity.CAMERA_PREVIEW_ACTIVITY,intent);
                finish();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileOutputStream out = new FileOutputStream(filePath);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent=new Intent();
                intent.putExtra("action","done");
                setResult(CameraActivity.CAMERA_PREVIEW_ACTIVITY,intent);
                finish();
            }
        });

        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotated = true;
                bitmap = Utility.rotateBitmap(bitmap,90);
                imagePreview.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();
        intent.putExtra("action","retake");
        setResult(CameraActivity.CAMERA_PREVIEW_ACTIVITY,intent);
        finish();
    }
}
