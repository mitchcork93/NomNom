package gcm.android.hmkcode.com.food;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;



public class SelectedRecipe extends ActionBarActivity {

    private Recipe recipe;
    public ProgressDialog pDialog;
    public FragmentTabHost mTabHost;
    String imageLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_recipe);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("NomNom");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String link = extras.getString("link");

            if (link.substring(0, 4).equalsIgnoreCase("http"))
                imageLink = link;
            else
                imageLink = "https://spoonacular.com/recipeImages/" + link;

            recipe = new Recipe(extras.getString("id"), extras.getString("title"), extras.getString("ready"), SelectedRecipe.this, imageLink);
            recipe.setInstructions(extras.getString("instructions"));
            recipe.getImage();
        }

        TextView title = (TextView)findViewById(R.id.title);
        title.setText(recipe.getTitle());

        Bundle extraText = new Bundle();
        extraText.putString("instructions",recipe.getInstructions());

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Tab 1", null),Tab1Activity.class, extraText);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Tab 2", null),Tab2Activity.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator("Tab 3", null),Tab3Activity.class, null);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selected_recipe, menu);
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
