package io.mountblue.offlineSurvey.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import io.mountblue.offlineSurvey.R;

@SuppressLint("ValidFragment")
public class AboutAppDialog extends DialogFragment {

    private Dialog dialog = null;
    private Context context;

    public AboutAppDialog(Context context) {
        this.context = context;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String aboutApp = getString(R.string.ABOUT_APP_MESSAGE);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.ABOUT_APP_TITLE);
        alertDialog.setMessage(aboutApp);
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });

        dialog = alertDialog.create();
        return dialog;
    }
}
