package com.mattqunell.bignerdranch.criminal_intent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.mattqunell.bignerdranch.R;

public class ImageFragment extends DialogFragment {

    // Tag for Bundle argument (in)
    private static final String ARG_IMAGE_PATH = "path";

    // UI element
    private ImageView mImageView;

    // newInstance method (replaces the fragment constructor)
    public static ImageFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_PATH, path);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the Uri String from the Arguments
        String path = getArguments().getString(ARG_IMAGE_PATH);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_image, null);

        mImageView = v.findViewById(R.id.dialog_image);
        mImageView.setImageBitmap(BitmapFactory.decodeFile(path));

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }
}
