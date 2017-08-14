package com.mattqunell.bignerdranch.criminal_intent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mattqunell.bignerdranch.R;

import java.util.Collections;
import java.util.List;

/*
 * CrimeListFragment connects the RecyclerView to the ViewHolders and Adapter
 */
public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_crime_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:

                // Make a new Crime, add it to CrimeLab
                Crime crime = new Crime();
                CrimeLab.get().addCrime(crime);

                // Start CrimePagerActivity (and CrimeFragment) at the new Crime
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);

                // Return true, indicating that processing is done
                return true;

            case R.id.sort_crimes:
                Collections.sort(CrimeLab.get().getCrimes());
                updateUI();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Helper method that creates and sets/updates the Adapter
    private void updateUI() {

        // Call the static getter, get the list of Crimes
        CrimeLab crimeLab = CrimeLab.get();
        List<Crime> crimes = crimeLab.getCrimes();

        // Create the adapter, or refresh the existing one
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.notifyDataSetChanged();
        }
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
