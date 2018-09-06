package johnschroederregis.johnschroederassignment1;

import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.JsonReader;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import johnschroederregis.johnschroederassignment1.ItemPackageModel.WebCallService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MasterFragment extends Fragment implements Serializable {
    private static final long serialVersionUID = 1L;
    public List<String> thingsList = new ArrayList<String>();
    private List<String> thingsDescList = new ArrayList<String>();
    private ListView thingsListView = null;
    private Button addButton = null;
    final MyReceiver myReceiver = new MyReceiver();
    private ContentResolver resolver=null;




    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference fbDatabaseref =database.getReference();



    public MasterFragment() {
    }


    public WebCallService webCallService;
    boolean bound = false;
//----------------------------------------------------Receiver and binding connection------------------------------------------
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            WebCallService.MyBinder binder = (WebCallService.MyBinder) service;
            webCallService = binder.getService();
            bound = true;
            Log.d("service", "service bound");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
            Log.d("service", "service disconnected");
        }
    };

    public class MyReceiver extends BroadcastReceiver {
        public MyReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("service", "inside on receive brodcast");

            try {

                List<String> addtothingslist = null;
                List<String> addtothingsdesclist = null;
                addtothingslist = new ArrayList<String>();
                addtothingslist = intent.getStringArrayListExtra("GroupList");
                addtothingsdesclist = intent.getStringArrayListExtra("NameList");

                Uri uri = Uri.parse ("content://johnschroederregis.johnschroederassignment1.database.ContentProvider/GET");
                resolver = getContext().getContentResolver();
                Log.d("ContProv", uri.toString());
                Cursor cursor = resolver.query (uri, null, null, null, null);
                if (cursor != null)
                {

                    if (cursor.moveToFirst())
                    {
                        while (cursor.moveToNext()){
                                thingsList.add (cursor.getColumnName(0));
                        }
                    }
                    cursor.close();
                }else{
                    for (String v : addtothingslist) {
                        thingsList.add(v.toString());
                    }
                    for(String v : addtothingsdesclist){
                        thingsDescList.add(v.toString());
                    }
                }
                Log.d("DataBase1", cursor.getColumnName(0).toString());
                final ArrayAdapter<String> thingsArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, thingsList);
                thingsListView.setAdapter(thingsArrayAdapter);
                thingsArrayAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                thingsList = null;
            }
            Log.d("service", "johnschroederregis.johnschroederassignment1.database.ContentProvider");
        }


//----------------------------------------------------Receiver and binding connection------------------------------------------
    }


    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master, container, false);
        thingsListView = (ListView) rootView.findViewById(R.id.List_of_things);
        thingsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final ArrayAdapter<String> thingsArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, thingsList);
        final ProgressBar spinner = (ProgressBar) rootView.findViewById(R.id.progressBar1);

        /*
        //--------------------------GETTING WEB SERVICE HERE AND SETTING UP RECIEVER------------------------
        Intent intent = new Intent(this.getActivity().getApplicationContext(), WebCallService.class);
        Intent i = new Intent(getContext(), WebCallService.class);
        IntentFilter iFilter = new IntentFilter("android.intent.action.GROUPS_RECEIVED");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(myReceiver, iFilter);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        getActivity().startService(i);
        //--------------------------GETTING WEB SERVICE HERE AND SETTING UP RECIEVER------------------------
        */


        spinner.setVisibility(View.VISIBLE);


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        spinner.setVisibility(View.INVISIBLE);

        Log.d("fireBase", fbDatabaseref.toString());

        try {
            fbDatabaseref.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("fireBase", "inside datasnapshot");
                    thingsList.clear();
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        thingsList.add(d.getKey().toString());
                        thingsDescList.add(d.getValue().toString());
                    }
                    Log.d("fireBase", thingsList.toString());
                    thingsListView.setAdapter(thingsArrayAdapter);
                    thingsArrayAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } catch (Exception e) {
            Log.d("service", "did not get services master fragment");
            thingsListView.setAdapter(null);
            thingsArrayAdapter.notifyDataSetChanged();
        }

        addButton = rootView.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddItemActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                }else{
                    startActivity(intent);
                }
            }
        });

        thingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // forceCrash(view);
                Intent intent = new Intent(view.getContext(), DetailView.class);
                String itemdesc = thingsDescList.get(position);
                intent.putExtra("ItemClicked", itemdesc);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}






