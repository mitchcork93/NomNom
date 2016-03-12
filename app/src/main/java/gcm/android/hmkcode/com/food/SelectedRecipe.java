package gcm.android.hmkcode.com.food;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SelectedRecipe extends ActionBarActivity {

    private Recipe recipe;
    String imageLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_recipe);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            Pattern p = Pattern.compile("\"([^\"]*)\"");
            Matcher m = p.matcher(extras.getString("link"));
            while (m.find()) {
                imageLink="https://spoonacular.com/recipeImages/"+ m.group(1);
            }
            recipe = new Recipe(extras.getString("id"),extras.getString("title"),extras.getString("ready"),SelectedRecipe.this,imageLink);
            TextView title =(TextView)findViewById(R.id.title);
            TextView ready =(TextView)findViewById(R.id.time);
            title.setText(recipe.getTitle());
            ready.setText("Ready in: " + recipe.getTime() + " minutes!");
            System.out.println(extras.getString("link"));

            recipe.getInstructions();
            recipe.getImage();
        }
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

    public void findInstructions(View v){
        recipe.getInstructions();
    }

}
