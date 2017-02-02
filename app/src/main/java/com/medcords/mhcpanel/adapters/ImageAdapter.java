package com.medcords.mhcpanel.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.medcords.mhcpanel.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private ArrayList<String> imagePaths;
    private Context context;

    public ImageAdapter(Context c, ArrayList<String> imagePaths) {
        context = c;
        this.imagePaths = imagePaths;
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_preview_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder viewHolder, int i) {

        viewHolder.numberTextView.setText(Integer.toString(i+1));
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePaths.get(i), options);
        viewHolder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView numberTextView;
        private ImageView imageView;
        public ViewHolder(View view) {
            super(view);

            numberTextView = (TextView)view.findViewById(R.id.photo_number_indicator);
            imageView = (ImageView) view.findViewById(R.id.image);
        }
    }

}
