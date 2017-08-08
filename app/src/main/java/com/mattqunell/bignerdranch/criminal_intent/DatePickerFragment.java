package com.mattqunell.bignerdranch.criminal_intent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.mattqunell.bignerdranch.R;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {

    // Tag for Bundle argument
    private static final String ARG_DATE = "date";

    // UI element
    private DatePicker mDatePicker;

    // newInstance method (replaces the fragment constructor)
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the Date from the Arguments
        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        // Use Calendar to parse the year, month, and day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        /*
         * Note:
         * Using a layout makes it easy to modify what the dialog presents. However, the DatePicker
         * could also have been created in code, without needing dialog_date.xml (but still needing
         * a unique ID to save its state across configuration changes):
         *
         *     DatePicker datePicker = new DatePicker(getActivity());
         *     ...
         *     new AlertDialog.Builder(getActivity()).setView(datePicker)
         */

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);

        // Display the Date shown in CrimeFragment
        mDatePicker = v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);

        /*
         * 1. Pass a context into the AlertDialog.Build constructor, which returns an AlertDialog
         *    instance
         * 2. Set the View to dialog_date
         * 3. Call setTitle(int titleId)
         * 4. Call setPositiveButton(int textId, DialogInterface.OnClickListener listener)
         * 5. Call AlertDialog.Builder.create(), which returns the configured AlertDialog instance
         */
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
