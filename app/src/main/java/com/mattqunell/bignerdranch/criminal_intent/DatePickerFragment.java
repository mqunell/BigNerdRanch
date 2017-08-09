package com.mattqunell.bignerdranch.criminal_intent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.mattqunell.bignerdranch.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    // Tags for Bundle argument (in) and Intent extra (out)
    private static final String ARG_DATE = "date";
    public static final String EXTRA_DATE = "com.mattqunell.bignerdranch.criminalintent.date";

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

    // Encapsulates the implementation details of DatePickerFragment's returned Intent
    public static Date getSelectedDate(Intent result) {
        return ((Date) result.getSerializableExtra(EXTRA_DATE));
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
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = mDatePicker.getYear();
                                int month = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();
                                Date date = new GregorianCalendar(year, month, day).getTime();
                                sendResult(Activity.RESULT_OK, date);
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() != null) {

            Intent intent = new Intent();
            intent.putExtra(EXTRA_DATE, date);

            /*
            * When dealing with Activities, Activity.onActivityResult(...) is called automatically when
            * after the child Activity dies. Since these are two Fragments being hosted by the same
            * Activity, it needs to be explicitly called.
            */
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        }
    }
}
