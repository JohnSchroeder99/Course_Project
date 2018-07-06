package johnschroederregis.johnschroederassignment1;


import android.content.res.Configuration;
import android.drm.DrmStore;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.Serializable;
import java.util.ArrayList;

public class Master extends AppCompatActivity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String[] drawerListViewItems;
    private ListView drawerListView;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Log.d("A1", "inside onclick");
            Toast.makeText(Master.this, ((TextView) view).getText(), Toast.LENGTH_LONG).show();
            try {
                drawerLayout.closeDrawer(GravityCompat.START);
            }catch (NullPointerException e) {
                Log.d("A1", "no drawer to close");
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerListViewItems = getResources().getStringArray(R.array.items);
        drawerListView = (ListView) findViewById(R.id.left_drawer);

        final ArrayAdapter<String> thingsArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerListViewItems);
        drawerListView.setAdapter(thingsArrayAdapter);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_master, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }
        switch (id) {
            case (R.id.action_settings):
                return true;
            case (R.id.hideoption):
                Log.d("swap2", "hide clicked");
                getSupportActionBar().hide();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
