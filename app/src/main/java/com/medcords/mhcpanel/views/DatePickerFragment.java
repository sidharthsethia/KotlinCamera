package com.medcords.mhcpanel.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.medcords.mhcpanel.R;
import com.medcords.mhcpanel.utilities.Utility;

import java.util.Calendar;

/**
 * Created by sidharthsethia on 07/01/17.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public DateSetListener onDateSetListener;

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

        Utility.colorizeDatePicker(dialog.getDatePicker());
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        onDateSetListener.onDateSet(view,year,month,day);
    }
}