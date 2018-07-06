package johnschroederregis.johnschroederassignment1;

import android.content.ClipData;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import java.io.Serializable;
import java.util.ArrayList;

import johnschroederregis.johnschroederassignment1.ItemPackageModel.ItemSrvc;

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



    public AddItemActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);

        itemName = rootView.findViewById(R.id.itemName);
        itemDesc = rootView.findViewById(R.id.itemDescr);

        addItem = rootView.findViewById(R.id.button);
        Log.d("A1", "creating service");

        final ItemServiceImplementation itemSrvc = new ItemServiceImplementation(rootView.getContext());

        Log.d("A1", "created service");

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String item = itemName.getText().toString();
                String itemDes = itemDesc.getText().toString();
                Intent intent = new Intent(v.getContext(), Master.class);




              //  itemSrvc.addItemsList1(item, itemDes);

                itemSrvc.addItemDatabase(item, itemDes);



                /*
                itemDescArraylist.add(itemDes);
                itemNameArraylist.add(item);

                intent.putStringArrayListExtra("itemDesc", itemDescArraylist);
                intent.putStringArrayListExtra("itemArray", itemNameArraylist);
                 */


                startActivity(intent);
            }
        });

        return rootView;

    }


}
