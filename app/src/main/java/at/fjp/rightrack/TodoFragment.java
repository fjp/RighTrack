package at.fjp.rightrack;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import at.fjp.rightrack.Database.RighTrackContract;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */



public class TodoFragment extends Fragment implements TodoDialogAdd.TodoDialogClickListener,
 TodoDialogUpdate.TodoDialogClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String RECURRENCE_ID_PARAM = "recurrence_id";
    private static final String LOG_TAG = TodoFragment.class.getSimpleName();
    private Context mContext = null;

    private ActionMode mActionMode;

    private TodoData mTodoData;
    private long mRecurrenceId;
    private TodoDialogAdd mTodoDialogAdd;
    private TodoDialogUpdate mTodoDialogUpdate;
    private String mSelectedTodo;

    // For the SimpleCursorAdapter to match the _Todo columns to layout items.
    private static final String[] COLUMNS_TO_BE_BOUND  = new String[] {
            RighTrackContract.TodoEntry.COLUMN_TODO
    };

    private static final int[] LAYOUT_ITEMS_TO_FILL = new int[] {
            android.R.id.text1
    };

    private ListView mListView;
    private SimpleCursorAdapter mAdapter;

    private OnFragmentInteractionListener mListener;


    public interface UpdateListener {
        public void update();
    }

    UpdateListener mUpdateListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TodoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodoFragment newInstance(long recurrenceId) {
        TodoFragment fragment = new TodoFragment();
        Bundle args = new Bundle();
        args.putLong(RECURRENCE_ID_PARAM, recurrenceId);
        fragment.setArguments(args);
        return fragment;
    }

    public TodoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecurrenceId = getArguments().getLong(RECURRENCE_ID_PARAM);
        }

        mContext = getActivity();
        mTodoData = new TodoData(mContext);

        setHasOptionsMenu(true);

        try {
            mUpdateListener = (UpdateListener) getFragmentManager().findFragmentByTag("Fragment");
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }

    }

    /**
     *
     * @param menu
     * @param inflater
     * Displays menu for todo fragment, including add option
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.todo, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_add_todo:

                showTodoDialogAdd();
                Log.v(LOG_TAG, "Added Todo");
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showTodoDialogAdd() {
        mTodoDialogAdd = TodoDialogAdd.newInstance(mTodoData.getRecurrenceArray());
        mTodoDialogAdd.setTargetFragment(this, 0); //todo move to dialog class
        mTodoDialogAdd.show(getFragmentManager(), "addTodo");
    }

    private void showTodoDialogUpdate(String currentTodo) {
        mTodoDialogUpdate = TodoDialogUpdate.newInstance(currentTodo, mTodoData.getRecurrenceArray());
        mTodoDialogUpdate.setTargetFragment(this, 0); //todo move to dialog class
        mTodoDialogUpdate.show(getFragmentManager(), "updateTodo");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_todo, container, false);

        // Get the TextView which will be populated with the Dictionary ContentProvider data.
        mListView = (ListView) rootView.findViewById(R.id.listview_todo);

        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mActionMode != null) {
                    return false;
                }

                // selected item
                mListView.setItemChecked(position, true);

                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = getActivity().startActionMode(mActionModeCallback);
                view.setSelected(true);
                return true;
            }
        });

        // Get a Cursor containing all of the rows in the _Todo table.
        Cursor cursor = mTodoData.getTodoCursor(mRecurrenceId);

        // Set the Adapter to fill the standard two_line_list_item layout with data from the Cursor.
        mAdapter = new SimpleCursorAdapter(mContext,
                android.R.layout.simple_list_item_1,
                cursor,
                COLUMNS_TO_BE_BOUND,
                LAYOUT_ITEMS_TO_FILL,
                0);

        // Attach the adapter to the ListView.
        mListView.setAdapter(mAdapter);

        return rootView;
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogAddClick(DialogFragment dialog) {
        // User touched the dialog's positive button

        // get the new _todo text from EditText
        EditText editTextTodo = (EditText) dialog.getDialog().findViewById(R.id.todo);
        String todo = editTextTodo.getText().toString();

        // get the selected recurrence key inside the spinner
        Spinner spinner = (Spinner) dialog.getDialog().findViewById(R.id.recurrence_spinner);
        long recurrenceId = spinner.getSelectedItemId() + 1;

        Log.v(LOG_TAG, "onDialogPositiveClick " + todo + " recurrenceId " + recurrenceId);

        // save new _todo in database: _todo, priorityKey, recurrenceKey, date, due_date
        mTodoData.addTodo(todo,1,recurrenceId,1,1);

        // Update the DataSet
        updateDataSet(mRecurrenceId);
        Log.v(LOG_TAG, "mRecurrenceId " + mRecurrenceId);
    }

    private void updateDataSet(long recurrenceId) {
        mUpdateListener = (UpdateListener) getFragmentManager().findFragmentByTag("Fragment");
        Log.v(LOG_TAG, "mUpdateListener BUGI " + mUpdateListener);
        mAdapter.changeCursor(mTodoData.getTodoCursor(recurrenceId));
        if (mUpdateListener != null) {
            mUpdateListener.update();
        }
    }

    @Override
    public void onDialogUpdateClick(DialogFragment dialog) {
        // User touched the dialog's update button

        // get the new _todo text from EditText
        EditText editTextTodo = (EditText) dialog.getDialog().findViewById(R.id.todo);
        String newTodo = editTextTodo.getText().toString();

        // get the selected recurrence key inside the spinner
        Spinner spinner = (Spinner) dialog.getDialog().findViewById(R.id.recurrence_spinner);
        long recurrenceId = spinner.getSelectedItemId() + 1;


        Log.v(LOG_TAG, "onDialogUpdateClick " + "oldTodo: " + mSelectedTodo + " newTodo: " + newTodo + " recurrenceId " + recurrenceId);

        // save new _todo in database
        mTodoData.updateTodo(mSelectedTodo, newTodo, recurrenceId);

        // Update the DataSet
        updateDataSet(mRecurrenceId);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.todo_selected, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int pos = mListView.getCheckedItemPosition();

            Cursor cursor = (Cursor) mListView.getItemAtPosition(pos);
            Log.v(LOG_TAG, "onActionItemClicked ID " + cursor);

            // Get _Todo String from _TodoColumn = 5
            mSelectedTodo = cursor.getString(5).toString();

            Log.v(LOG_TAG, "onActionItemClicked " + mSelectedTodo);

            switch (item.getItemId()) {
                case R.id.action_edit_todo:
                    showTodoDialogUpdate(mSelectedTodo);
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.action_delete_todo:
                    mTodoData.deleteTodo(mSelectedTodo);
                    updateDataSet(mRecurrenceId);
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

}
