package com.example.organaizer.ui.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.annotation.NonNull;

public class CustomDialogFragment extends DialogFragment {
    String HEAD;
    String BODY;
    public CustomDialogFragment(String HEAD, String BODY)
    {
        this.HEAD = HEAD;
        this.BODY = BODY;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder
                .setTitle(HEAD)
                .setMessage(BODY)
                .setPositiveButton("OK", null)
                .create();

    }
}