package com.medcords.mhcpanel.views

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

import com.medcords.mhcpanel.utilities.Utility

import java.io.IOException

/**
 * Created by sidharthsethia on 26/01/17.
 */

class CameraPreview(private val mContext: Context, private var mCamera: Camera?) : SurfaceView(mContext), SurfaceHolder.Callback {
    private val mHolder: SurfaceHolder

    var safeToTakePicture = false

    init {
        stopPreviewAndFreeCamera()
        val params = mCamera!!.parameters
        //*EDIT*//params.setFocusMode("continuous-picture");
        //It is better to use defined constraints as opposed to String, thanks to AbdelHady
        params.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
        val flashModes = params.supportedFlashModes
        if (flashModes.contains(android.hardware.Camera.Parameters.FLASH_MODE_AUTO)) {
            params.flashMode = Camera.Parameters.FLASH_MODE_AUTO
        }

        var bestSize: Camera.Size? = null
        val sizeList = (mCamera as Camera).getParameters().supportedPreviewSizes
        bestSize = sizeList[0]
        for (i in 1..sizeList.size - 1) {
            if (sizeList[i].width * sizeList[i].height > bestSize!!.width * bestSize.height) {
                bestSize = sizeList[i]
            }
        }

        val supportedPreviewFormats = params.supportedPreviewFormats
        val supportedPreviewFormatsIterator = supportedPreviewFormats.iterator()
        while (supportedPreviewFormatsIterator.hasNext()) {
            val previewFormat = supportedPreviewFormatsIterator.next()
            if (previewFormat === ImageFormat.YV12) {
                params.previewFormat = previewFormat
            }
        }

        params.setPreviewSize(bestSize!!.width, bestSize.height)

        params.setPictureSize(bestSize.width, bestSize.height)

        mCamera!!.parameters = params

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = holder
        mHolder.addCallback(this)
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera!!.setPreviewDisplay(holder)
            mCamera!!.setDisplayOrientation(90)
            mCamera!!.startPreview()
        } catch (e: IOException) {
            Log.d(TAG, "Error setting camera preview: " + e.message)
        }

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // empty. Take care of releasing the Camera preview in your activity.
        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mCamera!!.stopPreview()
        }
        stopPreviewAndFreeCamera()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.surface == null) {
            // preview surface does not exist
            return
        }

        // stop preview before making changes
        try {
            mCamera!!.stopPreview()
        } catch (e: Exception) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera!!.setPreviewDisplay(mHolder)
            mCamera!!.startPreview()
            safeToTakePicture = true
        } catch (e: Exception) {
            Log.d(TAG, "Error starting camera preview: " + e.message)
        }

    }

    private fun stopPreviewAndFreeCamera() {

        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mCamera!!.stopPreview()

            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
            mCamera!!.release()

            mCamera = null
        }
    }

    fun turnOnFlash() {
        val params = mCamera!!.parameters
        params.flashMode = Camera.Parameters.FLASH_MODE_TORCH
        mCamera!!.parameters = params
    }

    fun turnOFfFlash() {
        val params = mCamera!!.parameters
        params.flashMode = Camera.Parameters.FLASH_MODE_OFF
        mCamera!!.parameters = params
    }

    val flashStatus: Int
        get() {
            val params = mCamera!!.parameters
            if (Camera.Parameters.FLASH_MODE_AUTO == params.flashMode) {
                return FLASH_MODE_AUTO
            } else if (Camera.Parameters.FLASH_MODE_ON == params.flashMode) {
                return FLASH_MODE_ON
            } else if (Camera.Parameters.FLASH_MODE_OFF == params.flashMode) {
                return FLASH_MODE_OFF
            }

            return FLASH_MODE_OFF

        }

    companion object {

        private val TAG = CameraPreview::class.java.name

        private val FLASH_MODE_AUTO = 1
        private val FLASH_MODE_ON = 2
        private val FLASH_MODE_OFF = 3
    }
}