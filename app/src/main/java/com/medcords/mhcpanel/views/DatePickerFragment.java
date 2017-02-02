package com.medcords.mhcpanel.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.medcords.mhcpanel.R;
import com.medcords.mhcpanel.utilities.Utility;

import java.util.Calendar;

/**
 * Created by sidharthsethia on 07/01/17.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public DateSetListener onDateSetListener;
    View divider;
    TextView tv;
    Dialog dialog;

    public interface DateSetListener {
        void onDateSet(DatePicker view, int year, int month, int day);
    }

    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        try{
            onDateSetListener = (DateSetListener) activity;
        }catch(ClassCastException e){
            // The hosting activity does not implemented the interface AlertPositiveListener
            throw new ClassCastException(activity.toString() + " must implement DateSetListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        Bundle bundle = getArguments();

        int year = bundle.getInt("year");;
        int month = bundle.getInt("month");;
        int day = bundle.getInt("day");;

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), R.style.CustomDialogTheme ,this, year, month, day);
        dialog.getDatePicker().setSpinnersShown(true);

        Calendar c = Calendar.getInstance();
        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());

        this.dialog = dialog;
        Utility.colorizeDatePicker(dialog.getDatePicker());
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        onDateSetListener.onDateSet(view,year,month,day);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result){
                changeTitleAndDividerColor();
            }

        }.execute();
    }

    private void changeTitleAndDividerColor(){

        Resources system = Resources.getSystem();
        int dividerId = system.getIdentifier("titleDivider", "id", "android");
        int textViewId = system.getIdentifier("title", "id", "android");

        divider = dialog.findViewById(dividerId);
        tv = (TextView) dialog.findViewById(android.R.id.title);



        divider.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        //tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }
}