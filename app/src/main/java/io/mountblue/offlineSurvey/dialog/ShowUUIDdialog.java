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
import io.mountblue.offlineSurvey.database.DeviceUUIDGenerator;

@SuppressLint("ValidFragment")
public class ShowUUIDdialog extends DialogFragment {

    private Context context;
    private Dialog dialog = null;
    private String UUID = "";

    public ShowUUIDdialog(Context context) {
        this.context = context;
        UUID = DeviceUUIDGenerator.id(context);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.DEVICE_UUID);
        alertDialog.setIcon(R.drawable.ic_alert_48);
        alertDialog.setMessage(UUID);
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
