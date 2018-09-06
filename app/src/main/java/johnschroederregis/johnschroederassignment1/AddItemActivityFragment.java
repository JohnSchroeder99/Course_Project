package johnschroederregis.johnschroederassignment1;


import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import johnschroederregis.johnschroederassignment1.ItemPackageModel.WebCallService;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddItemActivityFragment extends Fragment implements Serializable {
    private static final long serialVersionUID = 1L;

    private Button addItem = null;
    private EditText itemName = null;
    private EditText itemDesc = null;
    static ArrayList<String> itemNameArraylist = new ArrayList<String>();
    static ArrayList<String> itemDescArraylist = new ArrayList<String>();
    final JSONObject jsonObject = new JSONObject();
    ContentResolver resolver =null;

    //-------------------------------------------------------------------------------

    WebCallService webCallService = new WebCallService();
    boolean bound = false;

    //-------------------------------------------------------------------------------


    public AddItemActivityFragment() {
    }

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


    //----------------------------------SETUP UP POST FROM HERE WITH SERVICE POST CALL---------------------------------------------
    public void broadcastIntent(View view, String a, String b) {
       // webCallService.postInformation(a, b);
        Intent intent = new Intent(view.getContext(), Master.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        }else{
            startActivity(intent);
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);
        addItem = rootView.findViewById(R.id.button);
        itemName = rootView.findViewById(R.id.itemName);
        itemDesc = rootView.findViewById(R.id.itemDescr);



        Intent intent = new Intent(this.getActivity().getApplicationContext(), WebCallService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        final ProgressBar spinner = rootView.findViewById(R.id.progressBar2);

        spinner.setVisibility(View.INVISIBLE);


        addItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String iName = itemName.getText().toString();
                final String iDesc = itemDesc.getText().toString();
                String errorItemName = "Item Name";
                String erroItemDescr = "Item Descr";

              
                if(iName.equals(errorItemName)){
                    itemName.setError("needs to be item name");
                }else if (iDesc.equals(erroItemDescr)){
                    itemDesc.setError("needs to be filled out ");
                }else{
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference ref = database.getReference();
                    Log.d("fbData", iName + iDesc);
                    spinner.setVisibility(View.VISIBLE);
                    ref.push().setValue(iName, iDesc);

                   /* Uri uri = Uri.parse ("content://johnschroederregis.johnschroederassignment1.database.ContentProvider");
                    resolver = getContext().getContentResolver();
                    ContentValues values = new ContentValues();
                    values.put("itemName", iName);
                    Log.d("ContProv", uri.toString());

                    resolver.insert(uri, values);*/

                    broadcastIntent(v, iName, iDesc);
                }
            }
        });

        return rootView;

    }


}
