package com.medcords.mhcpanel.utilities

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.hardware.Camera
import android.os.Environment
import android.util.Log
import android.view.Surface
import android.widget.NumberPicker

import com.medcords.mhcpanel.R

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by sidharthsethia on 27/12/16.
 */

object Utility {

    // attempt to get a Camera instance
    // Camera is not available (in use or does not exist)
    // returns null if camera is unavailable
    val cameraInstance: Camera?
        get() {
            var c: Camera? = null
            try {
                c = Camera.open()
            } catch (e: Exception) {
                Log.d("No camera Found", e.message)
            }

            return c
        }

    fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }


    /*
     * returning image / video
     */
    fun getOutputMediaFile(type: Int): File? {

        // External sdcard location
        val mediaStorageDir = File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.IMAGE_DIRECTORY_NAME)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(Constants.IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + Constants.IMAGE_DIRECTORY_NAME + " directory")
                return null
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmssS",
                Locale.getDefault()).format(Date())
        val mediaFile: File
        if (type == Constants.MEDIA_TYPE_IMAGE) {
            mediaFile = File(mediaStorageDir.path + File.separator
                    + "IMG_" + timeStamp + ".jpg")
        } else if (type == Constants.MEDIA_TYPE_CMP_IMAGE) {
            mediaFile = File(mediaStorageDir.path + File.separator
                    + "MED_IMG_" + timeStamp + ".jpg")
        } else {
            return null
        }

        return mediaFile
    }

    private fun setDividerColor(picker: NumberPicker?) {
        if (picker == null)
            return

        val count = picker.childCount
        for (i in 0..count - 1) {
            try {
                val dividerField = picker.javaClass.getDeclaredField("mSelectionDivider")
                dividerField.isAccessible = true
                val colorDrawable = ColorDrawable(picker.resources.getColor(R.color.colorPrimaryDark))
                dividerField.set(picker, colorDrawable)
                picker.invalidate()
            } catch (e: Exception) {
                Log.w("setDividerColor", e)
            }

        }
    }

    fun deleteImage(filePath: String) {
        //String file_dj_path = Environment.getExternalStorageDirectory() + "/ECP_Screenshots/abc.jpg";
        val fdelete = File(filePath)
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.e("-->", "file Deleted :" + filePath)
            } else {
                Log.e("-->", "file not Deleted :" + filePath)
            }
        }
    }

    fun setPhotoOrientation(activity: Activity, cameraId: Int): Int {
        val info = android.hardware.Camera.CameraInfo()
        android.hardware.Camera.getCameraInfo(cameraId, info)
        val rotation = activity.windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }

        var result: Int
        // do something for phones running an SDK before lollipop
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360
            result = (360 - result) % 360 // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360
        }

        return result
    }

    fun setCameraDisplayOrientation(activity: Activity, cameraId: Int, camera: android.hardware.Camera) {
        val info = android.hardware.Camera.CameraInfo()
        android.hardware.Camera.getCameraInfo(cameraId, info)
        val rotation = activity.windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }

        var result: Int
        //int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // do something for phones running an SDK before lollipop
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360
            result = (360 - result) % 360 // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360
        }

        camera.setDisplayOrientation(result)
    }

    fun rotateBitmap(source: Bitmap?, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source!!.width, source.height, matrix, true)
    }


}
