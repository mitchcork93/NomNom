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
    private ProgressDialog pDialog;
    String imageLink;
    String inst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_recipe);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recipe = new Recipe(extras.getString("id"),extras.getString("title"),extras.getString("ready"));
            TextView title =(TextView)findViewById(R.id.title);
            TextView ready =(TextView)findViewById(R.id.time);
            title.setText(recipe.getTitle());
            ready.setText("Ready in: " + recipe.getTime() + " minutes!");

            Pattern p = Pattern.compile("\"([^\"]*)\"");
            Matcher m = p.matcher(extras.getString("link"));
            while (m.find()) {
                imageLink="https://spoonacular.com/recipeImages/"+ m.group(1);
            }

            try {
                ImageView image =(ImageView)findViewById(R.id.imageView);
                URL url = new URL(imageLink);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                image.setImageBitmap(bmp);
            }catch (Exception e){}
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
        new getInstruct().execute();
    }

    private class getInstruct extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SelectedRecipe.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            System.out.println("");

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            JSONObject jsonStr = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" + recipe.getId() + "/information");
            httppost.setHeader("X-Mashape-Authorization", "5VSMYMsFj4msh0QQAjh7CCxfTaQqp1WVtbmjsnGgPs5B2mmY5k");

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                String responseBody = httpclient.execute(httppost, responseHandler);
                jsonStr = new JSONObject(responseBody);

            }catch (Exception e){e.printStackTrace();}

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    inst = jsonStr.getString("instructions");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            TextView i =(TextView)findViewById(R.id.instructions);
            i.setText(inst);
        }

    }
}
