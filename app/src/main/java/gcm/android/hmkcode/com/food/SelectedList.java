package gcm.android.hmkcode.com.food;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class SelectedList extends ActionBarActivity {

    public String listName;
    public int listId;
    public ArrayList<Item> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("NomNom");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            listName = extras.getString("name");
            String filtered = extras.getString("id").replaceAll("[^\\d]", "");
            listId = Integer.parseInt(filtered);

            TextView title = (TextView) findViewById(R.id.name);
            title.setText(listName);

            setListItems();
            generateList();
        }else
            System.out.println("ERROR: NO EXTRAS");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selected_list, menu);
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

    public void setListItems(){
        DBhelper db = new DBhelper(this);
        listItems = db.getAllItems(listId);
    }

    public void generateList(){
        LinearLayout ll = (LinearLayout)findViewById(R.id.expandableLinear);
        for(int i = 0; i <listItems.size(); i++) {
            final CheckBox cb = new CheckBox(this);
            cb.setText(listItems.get(i).getName());
            cb.setTag("checkbox" + i);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        buttonView.getTag();
                        cb.setTextColor(getResources().getColor(R.color.Green));
                    }
                }
            });
            ll.addView(cb);
        }

        Button button = new Button(this);
        button.setText("Delete List");
        button.setPadding(0, 10, 0, 10);
        ll.addView(button);
    }
}
