package at.fjp.rightrack;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import at.fjp.rightrack.Database.RighTrackContract;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */



public class TodoFragment extends Fragment implements TodoDialogAdd.TodoDialogClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String LOG_TAG = TodoFragment.class.getSimpleName();
    private static Context mContext = null;

    private static TodoData mTodoData;
    private TodoDialogAdd mTodoDialogAdd;

    // For the SimpleCursorAdapter to match the UserDictionary columns to layout items.
    private static final String[] COLUMNS_TO_BE_BOUND  = new String[] {
            RighTrackContract.TodoEntry.COLUMN_TODO,
            RighTrackContract.TodoEntry.COLUMN_REC_KEY
    };

    private static final int[] LAYOUT_ITEMS_TO_FILL = new int[] {
            android.R.id.text1,
            android.R.id.text2
    };


    private ListView mListView;
    private SimpleCursorAdapter mAdapter;


    // TODO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TodoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodoFragment newInstance(int sectionNumber, Context context) {
        TodoFragment fragment = new TodoFragment();
        mContext = context;
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, sectionNumber);
        fragment.setArguments(args);
        mTodoData = new TodoData(mContext);
        Log.v(LOG_TAG, "newInstance TodoFragment");
        return fragment;
    }

    public TodoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

        setHasOptionsMenu(true);
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

                showTodoDialog();
                Log.v(LOG_TAG, "Added Todo");
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showTodoDialog() {
        mTodoDialogAdd = new TodoDialogAdd();
        mTodoDialogAdd.setTargetFragment(this, 0);
        mTodoDialogAdd.show(getFragmentManager(), "addtodo");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_todo, container, false);

        // Get the TextView which will be populated with the Dictionary ContentProvider data.
        mListView = (ListView) rootView.findViewById(R.id.listview_todo);

        // Get a Cursor containing all of the rows in the Todo table.
        Cursor cursor = mTodoData.getCursor();

        // Set the Adapter to fill the standard two_line_list_item layout with data from the Cursor.
        mAdapter = new SimpleCursorAdapter(mContext,
                android.R.layout.two_line_list_item,
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
    public void onDialogPositiveClick(DialogFragment dialog) {

        // User touched the dialog's positive button
        EditText editTextTodo = (EditText) dialog.getDialog().findViewById(R.id.todo);
        String todo = editTextTodo.getText().toString();

        Log.v(LOG_TAG, "onDialogPositiveClick " + todo);

        mTodoData.addTodo(todo,1,1,1,1);


        mAdapter.changeCursor(mTodoData.getCursor());
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

}
