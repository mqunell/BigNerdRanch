package com.mattqunell.bignerdranch.criminal_intent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.mattqunell.bignerdranch.R;

import java.util.Date;
import java.util.UUID;

import static android.widget.CompoundButton.*;

/*
 * CrimeFragment is the controller between Crime (model) and fragment_crime (view).
 */
public class CrimeFragment extends Fragment {

    // Tags for Bundle argument and DialogFragment
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

    // Request code for targeted Fragment
    private static final int REQUEST_DATE = 0;

    // UI elements
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;
    private Button mReportButton;

    // Encapsulates the implementation details of new instances of CrimeFragment
    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    // Inflate the layout and handle updating the Crime
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int after) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Intentionally left blank
            }
        });

        mDateButton = v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());

                // Set this Fragment as the the DatePickerFragment's target
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mSolvedCheckbox = v.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setChecked(mCrime.isSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        mReportButton = v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the implicit Intent
                Intent i = new Intent(Intent.ACTION_SEND);

                // Set the type to plain text
                i.setType("text/plain");

                // Use constants from the Intent class to add extra information
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());

                // Force the chooser to show each time
                i = Intent.createChooser(i, getString(R.string.send_report));

                // Start the new Intent
                startActivity(i);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Like in geo_quiz/QuizActivity, the outer if statement is not actually necessary
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_DATE && data != null) {
            Date date = DatePickerFragment.getSelectedDate(data);
            mCrime.setDate(date);
            updateDate();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // "Remove Crime" selected
            case R.id.remove_crime:

                // Remove it from CrimeLab
                CrimeLab.get(getActivity()).removeCrime(mCrime);

                // Return to the previous Activity
                getActivity().finish();

                // Return true, indicating that processing is done
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Helper method for updating mDateButton's text
    private void updateDate() {
        //mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setText(DateFormat.format("EEEE, MMM d, yyyy", mCrime.getDate()));
    }

    // Builds a crime report
    private String getCrimeReport() {
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String solvedString = (mCrime.isSolved() ?
                getString(R.string.crime_report_solved) :
                getString(R.string.crime_report_unsolved));

        String suspectString = (mCrime.getSuspect() == null ?
                getString(R.string.crime_report_no_suspect) :
                getString(R.string.crime_report_suspect));

        // Put the date, solved, and suspect Strings into the "crime_report" format String
        return getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspectString);
    }
}
