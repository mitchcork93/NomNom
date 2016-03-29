package gcm.android.hmkcode.com.food;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class ShoppingLists extends ListActivity {

    public ArrayList<HashMap<String, String>> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_lists);

        DBhelper db = new DBhelper(this);
        ArrayList<List> allLists = db.getAllLists();
        lists = new ArrayList<HashMap<String, String>>();

        System.out.println("SIZE OF LIST: " + allLists.size());

        for(int i=0; i<allLists.size(); i++) {
            HashMap<String, String> listMap = new HashMap<String, String>();
            listMap.put("name",allLists.get(i).getName());
            listMap.put("date","Date Created: " + allLists.get(i).getDate());
            listMap.put("id","List ID: " + allLists.get(i).getId());
           // listMap.put("readyInMinutes", ready);
            // adding contact to contact list
            lists.add(listMap);
        }

        ListAdapter adapter = new SimpleAdapter(
                ShoppingLists.this, lists,
                R.layout.list_shopping, new String[] { "name", "date", "id"}, new int[] { R.id.name,
                R.id.date,R.id.id});
        setListAdapter(adapter);

        ListView lv = getListView();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // getting values from selected ListItem
                String ids = ((TextView) view.findViewById(R.id.id)).getText().toString();
                String listName = ((TextView) view.findViewById(R.id.name)).getText().toString();
                Intent in = new Intent(getApplicationContext(), SelectedList.class);
                in.putExtra("id", ids);
                in.putExtra("name",listName);
                startActivity(in);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_lists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
