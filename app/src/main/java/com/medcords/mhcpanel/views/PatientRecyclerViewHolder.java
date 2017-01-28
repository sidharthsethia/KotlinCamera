package com.medcords.mhcpanel.views;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medcords.mhcpanel.R;
import com.medcords.mhcpanel.models.Patient;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONObject;

import java.util.ArrayList;



public class PatientRecyclerViewHolder extends RecyclerView.ViewHolder{

    public CircularImageView imageView;
    public LinearLayout layout;
    public TextView nameTextView;
    public TextView relationTextView;

    public PatientRecyclerViewHolder(View itemView) {
        super(itemView);
        layout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
        imageView = (CircularImageView) itemView.findViewById(R.id.patient_profile_pic);
        nameTextView = (TextView) itemView.findViewById(R.id.patient_name);
        relationTextView = (TextView) itemView.findViewById(R.id.patient_relation);

        nameTextView.setTextColor(Color.argb(220, 0, 0, 0));
        relationTextView.setTextColor(Color.argb(140, 0, 0, 0));
    }
}
