package at.fjp.rightrack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Todo Dialog
 */
public class TodoDialogAdd extends DialogFragment {
    private static final String LOG_TAG = TodoFragment.class.getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.dialog_add_todo)

                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v(LOG_TAG, "PositiveButton");
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Log.v(LOG_TAG, "NegativeButton");
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
