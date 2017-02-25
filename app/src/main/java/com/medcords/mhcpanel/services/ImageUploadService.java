package com.medcords.mhcpanel.services;

/**
 * Created by sidharthsethia on 24/02/17.
 */

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.medcords.mhcpanel.database.DatabaseHandler;
import com.medcords.mhcpanel.database.ImageRecord;
import com.medcords.mhcpanel.utilities.Constants;



/**
 * Created by mb-14 on 30/08/16.
 */
public class ImageUploadService extends IntentService {
    private Context context;

    int imageId = 0;

    ImageRecord imageRecord;

    DatabaseHandler db;

    public ImageUploadService(){
        super(ImageUploadService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        context = ImageUploadService.this;
        imageId = intent.getIntExtra("imageId", 0);
        Log.e("Inside upload service", "true");
        db = new DatabaseHandler(ImageUploadService.this);
        imageRecord = db.getImage(imageId);

        uploadImage();

    }

    private void uploadImage() {
        //In the callback for the request call updateRecord().
        //Also call deleteRecord() if required.
        Log.e("Inside upload image fn", "true");
        updateRecord();
    }

    private void updateRecord(){
        imageRecord.setHasBeenUploaded(Constants.FLAG_UPLOAD_TRUE);
        db.updateImageRecord(imageRecord);
    }

    private void deleteRecord(){
        db.deleteImage(imageRecord);
    }


}
