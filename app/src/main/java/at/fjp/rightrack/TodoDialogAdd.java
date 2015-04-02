package at.fjp.rightrack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;





/**
 * Todo Dialog
 */
public class TodoDialogAdd extends DialogFragment {
    private static final String LOG_TAG = TodoDialogAdd.class.getSimpleName();

    public static TodoDialogAdd newInstance() {
        TodoDialogAdd dialogUpdate = new TodoDialogAdd();

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

    /* Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
    */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_todo_add, null));

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
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
