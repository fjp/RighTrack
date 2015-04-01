package at.fjp.rightrack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


/**
 * Todo Dialog
 */
public class TodoDialogUpdate extends DialogFragment {
    private static final String LOG_TAG = TodoFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";

    private static String mCurrentTodo;


    public static TodoDialogUpdate newInstance(String currentTodo) {
        TodoDialogUpdate dialogUpdate = new TodoDialogUpdate();
        Bundle args = new Bundle();
        mCurrentTodo = currentTodo;
        args.putString(ARG_PARAM1, mCurrentTodo);
        dialogUpdate.setArguments(args);
        return dialogUpdate;
    }

    public TodoDialogUpdate() {
        // Required empty public constructor
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface TodoDialogClickListener {
        // The dialog fragment receives a reference to this Activity through the
        // Fragment.onAttach() callback, which it uses to call the following methods
        // defined by the NoticeDialogFragment.NoticeDialogListener interface
        void onDialogUpdateClick(DialogFragment dialog);

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

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.dialog_todo_add, null);
        builder.setView(dialogView);

        EditText editText = (EditText) dialogView.findViewById(R.id.todo);
        editText.setText(mCurrentTodo);

        builder.setMessage(R.string.dialog_update_todo)

                .setPositiveButton(R.string.action_update_todo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Log.v(LOG_TAG, "PositiveButton");
                        mListener.onDialogUpdateClick(TodoDialogUpdate.this);

                    }
                })
                .setNegativeButton(R.string.action_cancel_todo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(TodoDialogUpdate.this);
                        Log.v(LOG_TAG, "NegativeButton");
                    }
                });



        // Create the AlertDialog object and return it
        return builder.create();
    }

//    @Override
//    public void onStart() {
//        EditText editText = (EditText) getDialog().findViewById(R.id.todo);
//        editText.setText(mCurrentTodo);
//    }
}
