package com.medcords.mhcpanel.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medcords.mhcpanel.R;
import com.medcords.mhcpanel.activities.AgentSignupActivity;
import com.medcords.mhcpanel.activities.PatientActionsActivity;
import com.medcords.mhcpanel.activities.SplashActivity;
import com.medcords.mhcpanel.models.Patient;
import com.medcords.mhcpanel.views.PatientRecyclerViewHolder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sidharthsethia on 06/01/17.
 */


public class PatientListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Patient> patients;
    Context context;

    public PatientListRecyclerViewAdapter(Context context, ArrayList<Patient> patients) {
        this.context = context;
        this.patients = patients;
    }


    @Override
    public int getItemViewType(int position) {
        /*switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }*/

        return 0;
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_list_item, parent, false);
        return new PatientRecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final PatientRecyclerViewHolder viewHolder = (PatientRecyclerViewHolder) holder;

        viewHolder.nameTextView.setText(patients.get(position).name);
        viewHolder.relationTextView.setText(patients.get(position).relation);
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PatientActionsActivity.class);
                i.putExtra("new_user",false);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }


}

