package johnschroederregis.johnschroederassignment1;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import johnschroederregis.johnschroederassignment1.ItemPackageModel.ItemSrvc;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailViewFragment extends Fragment {

    public DetailViewFragment() {
    }

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference ref = database.getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_view, container, false);

        //when this fragment is loaded create an intent that can tap into global data from masterfragment intents and grab the context of what "item" is intialized too in the masterfragment
       /* ItemServiceImplementation itemsrvc = new ItemServiceImplementation(rootView.getContext());
        ArrayList<String> alist= new ArrayList<String>();
        alist = (ArrayList<String>) itemsrvc.retrieveDatabaseItemDesc2();

        Intent intent = getActivity().getIntent();
        int selected =0;

        String item = alist.get(intent.getIntExtra("index", selected));
        */


        Intent intent = getActivity().getIntent();
        String childClickedFromMasters = intent.getStringExtra("ItemClicked");


        TextView thingwehavedetailed = rootView.findViewById(R.id.detail_text);
        String childClicked = ref.child(childClickedFromMasters).getKey();


        thingwehavedetailed.setText(childClicked);


        return rootView;
    }
}
