package com.medcords.mhcpanel.activities

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.medcords.mhcpanel.R
import com.medcords.mhcpanel.utilities.Constants
import com.medcords.mhcpanel.utilities.Utility
import com.medcords.mhcpanel.utilities.Utility.cameraInstance
import com.medcords.mhcpanel.views.CameraPreview

import java.io.FileOutputStream
import java.util.ArrayList

import id.zelory.compressor.Compressor

import com.medcords.mhcpanel.utilities.Utility.checkCameraHardware

class CameraActivity : AppCompatActivity() {

    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null
    private var captureButton: ImageButton? = null
    private var flashButton: ImageButton? = null
    private var singleModeButton: ImageButton? = null
    private var batchModeButton: ImageButton? = null
    private var doneButton: ImageButton? = null
    private var singleModeDot: View? = null
    private var batchModeDot: View? = null
    private var imagePreview: ImageView? = null
    private var noOfImagesTextView: TextView? = null

    private var singleModeLayout: LinearLayout? = null
    private var batchModeLayout: LinearLayout? = null
    private var preview: FrameLayout? = null

    private var flashOn: Boolean? = false
    private var singleModeOn: Boolean? = false

    private var noOfImagesTaken = 0
    private var imagesAddressList: ArrayList<String>? = null

    private var afterActivityResult: Boolean? = false

    private val mPicture = Camera.PictureCallback { data, camera ->
        noOfImagesTaken++

        if ((!singleModeOn!!)!! && noOfImagesTaken == 1) {
            batchModeLayout!!.visibility = View.GONE
            singleModeLayout!!.visibility = View.GONE
            doneButton!!.visibility = View.VISIBLE
        }

        if (noOfImagesTaken > 1) {
            noOfImagesTextView!!.visibility = View.VISIBLE
            noOfImagesTextView!!.text = Integer.toString(noOfImagesTaken)
        }

        var picture: Bitmap? = null

        if (data != null) {
            val screenWidth = resources.displayMetrics.widthPixels
            val screenHeight = resources.displayMetrics.heightPixels
            picture = BitmapFactory.decodeByteArray(data, 0, data?.size ?: 0)

            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                // Notice that width and height are reversed
                val scaled = Bitmap.createScaledBitmap(picture, screenHeight, screenWidth, true)
                val w = scaled.width
                val h = scaled.height
                // Setting post rotate to 90
                val mtx = Matrix()

                val CameraEyeValue = Utility.setPhotoOrientation(this@CameraActivity, 0) // CameraID = 1 : front 0:back
                val cameraFront = false
                if (cameraFront) { // As Front camera is Mirrored so Fliping the Orientation
                    if (CameraEyeValue == 270) {
                        mtx.postRotate(90f)
                    } else if (CameraEyeValue == 90) {
                        mtx.postRotate(270f)
                    }
                } else {
                    mtx.postRotate(CameraEyeValue.toFloat()) // CameraEyeValue is default to Display Rotation
                }

                picture = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true)
            } else {// LANDSCAPE MODE
                //No need to reverse width and height
                val scaled = Bitmap.createScaledBitmap(picture, screenWidth, screenHeight, true)
                picture = scaled
            }
        }
        //Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);
        val mPictureFile = Utility.getOutputMediaFile(Constants.MEDIA_TYPE_IMAGE)
        try {

            val out = FileOutputStream(mPictureFile!!)
            picture!!.compress(Bitmap.CompressFormat.JPEG, 100, out)

            val compressedImageBitmap = Compressor.getDefault(this@CameraActivity).compressToBitmap(mPictureFile)
            val mCompressedPictureFile = Utility.getOutputMediaFile(Constants.MEDIA_TYPE_CMP_IMAGE)
            val out2 = FileOutputStream(mCompressedPictureFile!!)
            compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2)

            Utility.deleteImage(mPictureFile.absolutePath)

            imagePreview!!.setImageBitmap(compressedImageBitmap)
            imagePreview!!.visibility = View.VISIBLE
            imagesAddressList!!.add(mCompressedPictureFile.absolutePath)

            mPreview!!.safeToTakePicture = true

            val intent = Intent(this@CameraActivity, ImagePreviewActivity::class.java)
            intent.putExtra("FilePath", mCompressedPictureFile.absolutePath)
            startActivityForResult(intent, CAMERA_PREVIEW_ACTIVITY)

        } catch (e: Exception) {
            e.printStackTrace()
            mPreview!!.safeToTakePicture = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        preview = findViewById(R.id.camera_preview) as FrameLayout
        captureButton = findViewById(R.id.button_capture) as ImageButton
        flashButton = findViewById(R.id.flash_button) as ImageButton
        singleModeButton = findViewById(R.id.single_mode) as ImageButton
        batchModeButton = findViewById(R.id.batch_mode) as ImageButton
        doneButton = findViewById(R.id.done) as ImageButton

        imagePreview = findViewById(R.id.preview) as ImageView
        imagePreview!!.visibility = View.GONE

        doneButton!!.visibility = View.GONE

        singleModeDot = findViewById(R.id.single_mode_dot) as View
        batchModeDot = findViewById(R.id.batch_mode_dot) as View

        singleModeLayout = findViewById(R.id.single_mode_layout) as LinearLayout
        batchModeLayout = findViewById(R.id.batch_mode_layout) as LinearLayout

        noOfImagesTextView = findViewById(R.id.photo_number_indicator) as TextView
        noOfImagesTextView!!.visibility = View.GONE

        doneButton!!.setOnClickListener { openUploadRecordsActivity() }

        singleModeDot!!.visibility = View.INVISIBLE
        batchModeDot!!.visibility = View.VISIBLE

        singleModeButton!!.setOnClickListener {
            singleModeDot!!.visibility = View.VISIBLE
            batchModeDot!!.visibility = View.INVISIBLE
            singleModeOn = true

            val toast = Toast.makeText(this@CameraActivity, "Single Mode On", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }

        batchModeButton!!.setOnClickListener {
            singleModeDot!!.visibility = View.INVISIBLE
            batchModeDot!!.visibility = View.VISIBLE
            singleModeOn = false

            val toast = Toast.makeText(this@CameraActivity, "Batch Mode On", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }

        flashButton!!.setOnClickListener {
            if ((!flashOn!!)!!) {
                flashOn = true
                flashButton!!.setImageResource(R.mipmap.flash_on_filled)
                mPreview!!.turnOnFlash()
            } else {
                flashOn = false
                flashButton!!.setImageResource(R.mipmap.flash_off_filled)
                mPreview!!.turnOFfFlash()
            }
        }

        captureButton!!.setOnClickListener {
            // get an image from the camera
            if (mPreview!!.safeToTakePicture && mCamera != null) {
                mCamera!!.takePicture(null, null, mPicture)
                mPreview!!.safeToTakePicture = false
            }
        }

    }

    private fun releaseCamera() {

        if (mCamera != null) {
            mPreview!!.holder.removeCallback(mPreview)
            mCamera!!.release()        // release the camera for other applications
            mCamera = null
        }
    }

    override fun onPause() {
        super.onPause()
        releaseCamera()
    }

    fun openUploadRecordsActivity() {
        finish()
    }

    override fun onResume() {
        super.onResume()

        if ((!afterActivityResult!!)!!) {

            if (checkCameraHardware(this)) {
                releaseCamera()
                mCamera = cameraInstance
                Utility.setCameraDisplayOrientation(this@CameraActivity, 0, mCamera as Camera)
            } else {
                Toast.makeText(this, "No camera Found", Toast.LENGTH_SHORT).show()
            }

            mPreview = CameraPreview(this, mCamera)
            preview!!.addView(mPreview)

            imagesAddressList = ArrayList<String>()

            noOfImagesTaken = 0
            imagePreview!!.visibility = View.GONE
            noOfImagesTextView!!.visibility = View.GONE
            doneButton!!.visibility = View.GONE
            batchModeLayout!!.visibility = View.VISIBLE
            singleModeLayout!!.visibility = View.VISIBLE
        } else {
            afterActivityResult = false
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        // check if the request code is same as what is passed  here it is 2
        afterActivityResult = true
        if (requestCode == CAMERA_PREVIEW_ACTIVITY) {
            val action = data.getStringExtra("action")

            if (action == "retake") {
                val address = imagesAddressList!![imagesAddressList!!.size - 1]
                imagesAddressList!!.removeAt(imagesAddressList!!.size - 1)
                Utility.deleteImage(address)
                noOfImagesTaken--
            }

            releaseCamera()
            mCamera = cameraInstance
            Utility.setCameraDisplayOrientation(this@CameraActivity, 0, mCamera as Camera)
            mCamera!!.startPreview()
            mPreview = CameraPreview(this, mCamera)
            preview!!.addView(mPreview)


            if (singleModeOn!! && noOfImagesTaken == 1) {
                mCamera!!.stopPreview()
                openUploadRecordsActivity()
            } else {
                if (singleModeOn!! && noOfImagesTaken == 0) {
                    imagePreview!!.visibility = View.GONE
                    noOfImagesTextView!!.visibility = View.GONE
                    doneButton!!.visibility = View.GONE
                    batchModeLayout!!.visibility = View.VISIBLE
                    singleModeLayout!!.visibility = View.VISIBLE
                } else {
                    imagePreview!!.visibility = View.VISIBLE
                    noOfImagesTextView!!.visibility = View.INVISIBLE
                    doneButton!!.visibility = View.VISIBLE
                    batchModeLayout!!.visibility = View.GONE
                    singleModeLayout!!.visibility = View.GONE

                    if (noOfImagesTaken > 1) {
                        noOfImagesTextView!!.visibility = View.VISIBLE
                        noOfImagesTextView!!.text = Integer.toString(noOfImagesTaken)
                    }

                    if (noOfImagesTaken > 0) {
                        val options = BitmapFactory.Options()
                        val bitmap = BitmapFactory.decodeFile(imagesAddressList!![noOfImagesTaken - 1], options)
                        imagePreview!!.setImageBitmap(bitmap)
                    }

                }

            }

        }
    }

    companion object {

        private val TAG = CameraPreview::class.java.name

        var CAMERA_PREVIEW_ACTIVITY = 2
    }
}
