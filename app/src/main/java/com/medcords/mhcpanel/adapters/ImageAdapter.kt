package com.medcords.mhcpanel.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.medcords.mhcpanel.R

import java.util.ArrayList

class ImageAdapter(private val context: Context, private val imagePaths: ArrayList<String>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ImageAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.image_preview_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ImageAdapter.ViewHolder, i: Int) {

        viewHolder.numberTextView.text = Integer.toString(i + 1)
        val options = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeFile(imagePaths[i], options)
        viewHolder.imageView.setImageBitmap(bitmap)
    }

    override fun getItemCount(): Int {
        return imagePaths.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val numberTextView: TextView
        internal val imageView: ImageView

        init {

            numberTextView = view.findViewById(R.id.photo_number_indicator) as TextView
            imageView = view.findViewById(R.id.image) as ImageView
        }
    }

}
