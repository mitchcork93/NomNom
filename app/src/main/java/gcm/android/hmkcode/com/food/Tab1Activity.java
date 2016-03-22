package gcm.android.hmkcode.com.food;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by mitch on 21/03/2016.
 */
public class Tab1Activity extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_one, container, false);

        Bundle b = getArguments();
        String test = b.getString("instructions");
        TextView tv = (TextView) v.findViewById(R.id.cookingInstructions);
        tv.setText(Html.fromHtml(test));

        return v;
    }
}
