package gcm.android.hmkcode.com.food;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by mitch on 21/03/2016.
 */
public class Tab3Activity extends Fragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_three, container, false);

        LinearLayout l = (LinearLayout)inflater.inflate(R.layout.tab_three, container, false);
        // RelativeLayout s = (RelativeLayout)inflater.inflate(R.layout.tab_two, container, false);
        // RelativeLayout r = (RelativeLayout)s.findViewById(R.layout.text);

        Bundle b = getArguments();
        String nutrientsString = b.getString("nutrients");
        String amountSting = b.getString("amount");
        String unitString = b.getString("units");

        String[] nutrients = nutrientsString.split(",");
        String[] amount = amountSting.split(",");
        String[] units = unitString.split(",");

        ScrollView sv = new ScrollView(getActivity());
        LinearLayout linear = new LinearLayout(getActivity());
        linear.setOrientation(LinearLayout.VERTICAL);
        sv.addView(linear);

        for(int i = 0; i <nutrients.length; i++) {
            TextView cb = new TextView(getActivity());
            cb.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
            SpannableString content = new SpannableString(amount[i] + units[i] + " of " + nutrients[i]);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            cb.setText(content);
            cb.setPadding(0,10,0,10);
            linear.addView(cb);
        }


        l.addView(sv);
        return l;
    }

}
