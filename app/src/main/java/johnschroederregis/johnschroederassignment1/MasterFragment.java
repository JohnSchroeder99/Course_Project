package johnschroederregis.johnschroederassignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.EventLog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import johnschroederregis.johnschroederassignment1.ItemPackageModel.ItemSrvc;

/**
 * A placeholder fragment containing a simple view.
 */
public class MasterFragment extends Fragment implements Serializable {
    private static final long serialVersionUID = 1L;

    private  List<String> thingsList =new ArrayList<String>();
    private  List<String> thingsDescList = new ArrayList<String>();
    private ListView thingsListView = null;
    private Button addButton = null;

    

    public MasterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master, container, false);
        //Make the arrayAdapter and set the listview to display the contents of the thingsList stored in strings.xml resoures
        try {
            /*  Intent intent = getActivity().getIntent();
            String extraStr;
            extraStr = getActivity().getIntent().getExtras().getString("item");
            Log.d("swap2", "masterFragment Inside intent put to thingslist");
            thingsList = intent.getStringArrayListExtra("itemArray");
            thingsDescList = intent.getStringArrayListExtra("itemDesc");
            */

            Log.d("A1", "creating item service implementation object - master fragment");
            ItemServiceImplementation itemSrvc = new ItemServiceImplementation(this.getContext());
            Log.d("A1", "implementation object created - master fragment ");

            //thingsList = itemSrvc.retrieveItemsInList();
            //thingsDescList = itemSrvc.retrieveItemsInList2();

            thingsList =itemSrvc.retrieveDatabaseItems1();
            thingsDescList = itemSrvc.retrieveDatabaseItemDesc2();

            Log.d("A1", "thingsList ArrayList attempted to be set to values of output file read file ");

            if(thingsList != null){
                Log.d("A1", "thingList has items");
            }else{
                Log.d("A1", "thingList is null");
            }
        } catch (NullPointerException e) {
            Log.d("A1", "nothing in intent yet");
        }
        thingsListView = (ListView) rootView.findViewById(R.id.List_of_things);
        thingsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final ArrayAdapter<String> thingsArrayAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_multiple_choice, thingsList);
        try{
            thingsListView.setAdapter(thingsArrayAdapter);
            thingsArrayAdapter.notifyDataSetChanged();
        }catch (Exception e){
            thingsListView.setAdapter(null);
            thingsArrayAdapter.notifyDataSetChanged();
            Log.d("A1", "null arrayadapter");
        }




        addButton = rootView.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddItemActivity.class);
                startActivity(intent);
            }
        });

        thingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SparseBooleanArray sp = thingsListView.getCheckedItemPositions();

                //item is a string that is equal to the name of the item at the clicked position
                String itemdesc = thingsDescList.get(position);
                // Toast.makeText(view.getContext(), "Position: "+ position+ " ListItem "+ item, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(view.getContext(), DetailView.class);
                // create an intent that detailView class can access and
                // save the intent to the global intent bucket and call it item  so it can be retrieved from detail
                // fragments and set it to the string called item which is whatever thingy
                // is at the item position
               // intent.putExtra("itemDesc", itemdesc);
                intent.putExtra("index", position);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
