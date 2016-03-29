package gcm.android.hmkcode.com.food;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.ScrollView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;

/**
 * Created by mitch on 21/03/2016.
 */
public class Tab2Activity extends Fragment {

    public ArrayList<String> shoppingList;
    public ArrayList<CheckBox> checkboxes;
  //  public CheckBox cb;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_two, container, false);

        shoppingList = new ArrayList<String>();
        checkboxes = new ArrayList<CheckBox>();

        LinearLayout l = (LinearLayout)inflater.inflate(R.layout.tab_two, container, false);
       // RelativeLayout s = (RelativeLayout)inflater.inflate(R.layout.tab_two, container, false);
       // RelativeLayout r = (RelativeLayout)s.findViewById(R.layout.text);

        Bundle b = getArguments();
        String ingredientsString = b.getString("ingredients");

        System.out.println("string: " + ingredientsString);
        String[] ingredients = ingredientsString.split(",");

        System.out.println("size: " + ingredients.length);

        ScrollView sv = new ScrollView(getActivity());
        LinearLayout ll = new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        for(int i = 0; i <ingredients.length; i++) {
            final CheckBox cb = new CheckBox(getActivity());
            cb.setText(ingredients[i]);
            cb.setTag("checkbox" + i);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        buttonView.getTag();
                        shoppingList.add(cb.getText().toString());
                    } else {
                        shoppingList.remove(cb.getText().toString());
                    }

                }
            });
            ll.addView(cb);
        }
        Button button = new Button(getActivity());
        button.setText("Add to list");
        button.setPadding(0, 10, 0, 10);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String list = "";

                for (int x=0; x<shoppingList.size(); x++)
                    list += shoppingList.get(x) + "\n";

                final EditText input = new EditText(getActivity());
                input.setHint("Enter List Name");
                new AlertDialog.Builder(getActivity())
                        .setView(input)
                        .setIcon(R.drawable.action_bar_shopping_list)
                        .setTitle("Save List")
                        .setMessage("Adding following items to list\n\n" + list)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (input.getText().toString().equalsIgnoreCase(""))
                                    System.out.println("no input");
                                else {
                                    DBhelper db = new DBhelper(getActivity());

                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                    String date = df.format(c.getTime());

                                    long id = db.insertDataList(input.getText().toString(), date);
                                    int listId = (int) id;

                                    for (int y = 0; y < shoppingList.size(); y++)
                                        db.insertDataItem(shoppingList.get(y), listId);

                                    /* USED FOR TESTING
                                    List test = db.getList(listId);
                                    System.out.println("Latest List Info\nName: " + test.getName() + "\n" + "ID: " + test.getId() + "\n" + "Date: " + test.getDate());
                                    ArrayList<Item> allItems = db.getAllItems(listId);
                                    for(int i=0; i<allItems.size(); i++)
                                        System.out.println("Latest Items Info\nName: " + allItems.get(i).getName() + "\n" + "ID: " + allItems.get(i).getId() + "\n" + "ListID: " + allItems.get(i).getListId());

                                    ArrayList<List> allLists = db.getAllLists();
                                    for(int i=0; i<allLists.size(); i++)
                                        System.out.println("List " + i + "Info\nName: " + allLists.get(i).getName() + "\n" + "ID: " + allLists.get(i).getId() + "\n" + "Date: " + allLists.get(i).getDate());*/
                                    Toast.makeText(getActivity(), "List Created!",
                                            Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                }
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });
        ll.addView(button);
        l.addView(sv);
        return l;

       // return v;
    }
}
