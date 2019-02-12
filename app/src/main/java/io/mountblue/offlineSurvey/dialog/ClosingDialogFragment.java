package io.mountblue.offlineSurvey.dialog;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import io.mountblue.offlineSurvey.R;

public class ClosingDialogFragment extends DialogFragment {

    private Dialog dialog = null;

    public ClosingDialogFragment() {

    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.alert);
        alertDialog.setIcon(R.drawable.ic_warning_yellow_48dp);
        alertDialog.setMessage(R.string.close_question_screen_massege);
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                getActivity().finish();
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();

            }
        });
        dialog = alertDialog.create();
        return dialog;
    }
}
