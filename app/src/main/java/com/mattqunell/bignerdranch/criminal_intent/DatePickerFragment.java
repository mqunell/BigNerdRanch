package com.mattqunell.bignerdranch.criminal_intent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.mattqunell.bignerdranch.R;

public class DatePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*
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
