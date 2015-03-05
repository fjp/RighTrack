package at.fjp.rightrack;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private final String LOG_TAG = TodoFragment.class.getSimpleName();
    private ArrayAdapter<String> mForecastAdapter;
    private static Context mContext = null;

    private ListView myList;
    private MyAdapter myAdapter;

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

    public ArrayList myItems = new ArrayList();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_add_todo:
                ListItem listItem = new ListItem();
                listItem.caption = "Added Todo";
                myItems.add(listItem);
                Log.v(LOG_TAG, "Added Todo");
                myAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;


        public MyAdapter() {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            for (int i = 0; i < 3; i++) {
                ListItem listItem = new ListItem();
                listItem.caption = "Caption" + i;
                myItems.add(listItem);
            }

            notifyDataSetChanged();
        }

        public int getCount() {
            return myItems.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_item_todo, null);
                holder.caption = (EditText) convertView
                        .findViewById(R.id.list_item_todo_edit_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //Fill EditText with the value you have in data source
            ListItem item = (ListItem) myItems.get(position);
            holder.caption.setText(item.caption);
            holder.caption.setId(position);

            //we need to update adapter once we finish with editing
            holder.caption.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus){
                        final int position = v.getId();
                        final EditText Caption = (EditText) v;
                        ListItem item = (ListItem) myItems.get(position);
                        item.caption = Caption.getText().toString();
                    }
                }
            });

            return convertView;
        }
    }

    class ViewHolder {
        EditText caption;
    }

    class ListItem {
        String caption;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /* Create some dummy data for the ListView.  Here's a sample weekly forecast
        String[] data = {
                "Stop Thinking Too Much",
                "Get a Job - BMW, Uni, ... Werkstudent oder Praktikum!",
                "Eat an Apple",
                "Komplimente geben/denken",
                "Fri 6/27 - Foggy - 21/10",
                "Kurzum: Wer nie fragt, kann auch nie etwas bekommen. Wer aber wagt, gewinnt gleich dreifach. Mindestens an Erfahrung.\n" +
                        "\n" +
                        "Fragen kostet nichts – außer Überwindung! Geh unter die kalte Dusche!",
                "Lass die Gedanken - ausnahmslos ALLE! - das sein was sie sind! Gedanken. Lass sie kommen und gehen und grübel nicht sinnlos darüber! \n" +
                        "Gib die Analyse, der Meinungen anderer über dich, auf! Haters gonna hate.",
                "Fail. A Lot.\n" +
                        "Be Polarizing (Gegensätztlich) - Be Yourself\n" +
                        "Stop Giving A FUCK!\n" +
                        "Do One Thing That Scares You Everyday!\n" +
                        "GET IN TOUCH WITH PEOPLE\n" +
                        "it developes your brain!\n" +
                        "1) Make peace with your past\n" +
                        "so it won't disturb your present.\n" +
                        "2) What other people think of you\n" +
                        "is none of your business.\n" +
                        "3) Time heals almost everything.\n" +
                        "Give it time.\n" +
                        "4) No one is in charge\n" +
                        "of your happiness. Except you.\n" +
                        "5) Don't compare your life to others\n" +
                        "and don't judge them, you have no idea what their jorney is all about.\n" +
                        "6) Stop thinking too much.\n" +
                        "Its alright not to know the answers.\n" +
                        "They will come to you when you least expect it.\n" +
                        "7) Smile.\n" +
                        "You don't own all the problems in the world.\n" +
                        "---------\n" +
                        "8) Just start something!"

        };
        */

        View rootView = inflater.inflate(R.layout.fragment_todo, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        myList = (ListView) rootView.findViewById(R.id.listview_todo);
        myList.setItemsCanFocus(true);
        myAdapter = new MyAdapter();
        myList.setAdapter(myAdapter);

        return rootView;
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
