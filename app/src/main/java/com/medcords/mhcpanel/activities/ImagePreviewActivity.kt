package com.medcords.mhcpanel.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView

import com.medcords.mhcpanel.R
import com.medcords.mhcpanel.utilities.Utility

import java.io.FileOutputStream

class ImagePreviewActivity : AppCompatActivity() {

    private var retakeButton: ImageButton? = null
    private var rotateButton: ImageButton? = null
    private var doneButton: ImageButton? = null
    private var imagePreview: ImageView? = null

    private var filePath: String? = null

    private var bitmap: Bitmap? = null

    private var rotated: Boolean? = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        imagePreview = findViewById(R.id.camera_preview) as ImageView
        retakeButton = findViewById(R.id.retake_button) as ImageButton
        rotateButton = findViewById(R.id.rotate_button) as ImageButton
        doneButton = findViewById(R.id.done) as ImageButton

        filePath = intent.extras.getString("FilePath")

        val options = BitmapFactory.Options()
        bitmap = BitmapFactory.decodeFile(filePath, options)
        imagePreview!!.setImageBitmap(bitmap)

        retakeButton!!.setOnClickListener {
            val intent = Intent()
            intent.putExtra("action", "retake")
            setResult(CameraActivity.CAMERA_PREVIEW_ACTIVITY, intent)
            finish()
        }

        (doneButton as ImageButton).setOnClickListener {
            try {
                val out = FileOutputStream(filePath!!)
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val intent = Intent()
            intent.putExtra("action", "done")
            setResult(CameraActivity.CAMERA_PREVIEW_ACTIVITY, intent)
            finish()
        }

        (rotateButton as ImageButton).setOnClickListener {
            rotated = true
            bitmap = Utility.rotateBitmap(bitmap, 90f)
            imagePreview!!.setImageBitmap(bitmap)
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("action", "retake")
        setResult(CameraActivity.CAMERA_PREVIEW_ACTIVITY, intent)
        super.onBackPressed()
    }
}
