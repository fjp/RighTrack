package at.fjp.rightrack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


/**
 * Todo Dialog
 */
public class TodoDialogAdd extends DialogFragment {
    private static final String LOG_TAG = TodoDialogAdd.class.getSimpleName();

    private static final String ARG_PARAM1 = "recurrence";

    private static String[] mRecurrenceStrings;
    private Spinner mSpinner;

    private Context mContext;

    public static TodoDialogAdd newInstance(String[] recurrence) {
        TodoDialogAdd dialogUpdate = new TodoDialogAdd();
        Bundle args = new Bundle();
        mRecurrenceStrings = recurrence;
        args.putCharSequenceArray(ARG_PARAM1, mRecurrenceStrings);
        dialogUpdate.setArguments(args);
        return dialogUpdate;
    }

    public TodoDialogAdd() {
        // Required empty public constructor
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface TodoDialogClickListener {
        // The dialog fragment receives a reference to this Activity through the
        // Fragment.onAttach() callback, which it uses to call the following methods
        // defined by the NoticeDialogFragment.NoticeDialogListener interface
        void onDialogAddClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    TodoDialogClickListener mListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mListener = (TodoDialogClickListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.dialog_todo_add, null);
        builder.setView(dialogView);

        builder.setMessage(R.string.dialog_add_todo)

                .setPositiveButton(R.string.action_add_todo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Log.v(LOG_TAG, "PositiveButton");
                        mListener.onDialogAddClick(TodoDialogAdd.this);

                    }
                })
                .setNegativeButton(R.string.action_cancel_todo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(TodoDialogAdd.this);
                        Log.v(LOG_TAG, "NegativeButton");
                    }
                });

        mSpinner = (Spinner) dialogView.findViewById(R.id.recurrence_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mRecurrenceStrings);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
