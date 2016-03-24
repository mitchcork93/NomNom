package gcm.android.hmkcode.com.food;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import android.widget.ScrollView;


import java.util.ArrayList;
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
                        System.out.println("ADDING: " + cb.getText().toString());
                    } else {
                        shoppingList.remove(cb.getText().toString());
                        System.out.println("REMOVING: " + cb.getText().toString());
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

                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Save List")
                        .setMessage("Add following items to list?\n\n" + list)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                
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
