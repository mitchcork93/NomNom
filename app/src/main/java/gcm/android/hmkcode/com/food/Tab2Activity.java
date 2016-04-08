package gcm.android.hmkcode.com.food;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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

        Bundle b = getArguments();
        String ingredientsString = b.getString("ingredients");
        String[] ingredients = ingredientsString.split(",");

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
                    if (isChecked) { // If the checkbox is ticked
                        buttonView.getTag();
                        shoppingList.add(cb.getText().toString()); // add the item to list
                    } else { // Item is unticked
                        shoppingList.remove(cb.getText().toString()); // remove item from list
                    }

                }
            });
            ll.addView(cb);
        }
        Button button = new Button(getActivity());
        button.setText("Add to list");
        button.setPadding(0, 10, 0, 10);
        button.setBackgroundColor(Color.parseColor("#49B99F"));
        button.setTextColor(Color.parseColor("#FFFFFF"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String list = "";

                for (int x = 0; x < shoppingList.size(); x++)
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
