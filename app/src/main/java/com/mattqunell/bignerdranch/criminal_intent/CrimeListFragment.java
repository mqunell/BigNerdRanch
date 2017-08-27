package com.mattqunell.bignerdranch.criminal_intent;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mattqunell.bignerdranch.R;

import java.util.Collections;
import java.util.List;

/*
 * CrimeListFragment connects the RecyclerView to the ViewHolders and Adapter
 * Known bug: Shown subtitle does not persist through Up navigation
 */
public class CrimeListFragment extends Fragment {

    // Key for bundle
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    // The "layers" of the View
    private ConstraintLayout mEmptyCrimeView;
    private RecyclerView mCrimeRecyclerView;

    // UI elements
    private Button mNewCrimeButton;

    // Adapter for connecting the RecyclerView and Crimes
    private CrimeAdapter mAdapter;

    // Instance variables
    private boolean mSubtitleVisible = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle inState) {

        // Inflate the layout file
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        // empty_crime_view layer
        mEmptyCrimeView = view.findViewById(R.id.empty_crime_view);

        mNewCrimeButton = view.findViewById(R.id.new_crime_button);
        mNewCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newCrime();
            }
        });

        // crime_recycler_view layer
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Set mSubtitleVisible if possible (used for device rotation)
        if (inState != null) {
            mSubtitleVisible = inState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUi();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        // Set show_subtitle's text to "Show Subtitle" or "Hide Subtitle"
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        subtitleItem.setTitle(mSubtitleVisible ? R.string.hide_subtitle : R.string.show_subtitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
         * true has to be returned in each individual case to indicate the processing is done, so
         * that the following cases don't run as well. For example, without returning true in
         * "case R.id.show_subtitle", the code in "case R.id.sort_crimes" is also ran when the
         * show_subtitle MenuItem is selected.
         */

        switch (item.getItemId()) {
            // "New Crime" selected
            case R.id.new_crime:
                newCrime();

                return true;

            // "Show Subtitle" selected
            case R.id.show_subtitle:

                // Alternate "Show" and "Hide", and update the menu
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();

                updateUi();
                updateSubtitle();

                return true;

            // "Sort Crimes" selected
            case R.id.sort_crimes:
                Collections.sort(CrimeLab.get(getActivity()).getCrimes());
                updateUi();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Helper method for "New Crime" functionality
    private void newCrime() {

        // Make a new Crime, add it to CrimeLab
        Crime crime = new Crime();
        CrimeLab.get(getActivity()).addCrime(crime);

        // Start CrimePagerActivity (and CrimeFragment) at the new Crime
        Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
        startActivity(intent);
    }

    // Helper method that creates and sets/updates the Adapter
    private void updateUi() {

        // Get the list of Crimes
        List<Crime> crimes = CrimeLab.get(getActivity()).getCrimes();

        // Create the adapter, or refresh the existing one
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.notifyDataSetChanged();
        }

        // Show the empty_crime_view layer when there are 0 Crimes, or hide when >= 1 Crimes
        mEmptyCrimeView.setVisibility(crimes.size() == 0 ? View.VISIBLE : View.INVISIBLE);

        updateSubtitle();
    }

    // Helper method for setting/removing the subtitle
    private void updateSubtitle() {
        String subtitle = null;

        // If the subtitle is visible, get the number of Crimes and set a formatted String
        if (mSubtitleVisible) {
            int crimeCount = CrimeLab.get(getActivity()).getCrimes().size();

            // Get the correctly formatted String
            subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, crimeCount,
                    crimeCount);
        }

        // Set the subtitle
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    /*
     * CrimeHolder: The ViewHolder
     * Inflates and owns each individual layout (list_item_crime) within the RecyclerView.
     * The bind(Crime) method is called each time a new Crime should be displayed.
     */
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Crime mCrime;
        private TextView mTitleTextview;
        private TextView mDateTextview;
        private ImageView mSolvedImageview;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);

            // UI elements
            mTitleTextview = itemView.findViewById(R.id.crime_title);
            mDateTextview = itemView.findViewById(R.id.crime_date);
            mSolvedImageview = itemView.findViewById(R.id.crime_solved);
        }

        // Set a specific layout's TextViews
        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextview.setText(mCrime.getTitle());

            // Format example: "Wednesday, Aug 2, 2017"
            mDateTextview.setText(DateFormat.format("EEEE, MMM d, yyyy", mCrime.getDate()));

            /*
             * Ternary operator: boolean statement ? true result : false result
             * crime.isSolved() == true   ->  View.VISIBLE
             * crime.isSolved() == false  ->  View.GONE
             */
            mSolvedImageview.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View view) {
            startActivity(CrimePagerActivity.newIntent(getActivity(), mCrime.getId()));
        }
    }

    /*
     * CrimeAdapter: The Adapter
     * Connects the ViewHolder and Crimes by knowing how Crime and CrimeLab are implemented.
     * The overridden methods are all required and called by the RecyclerView itself.
     */
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
