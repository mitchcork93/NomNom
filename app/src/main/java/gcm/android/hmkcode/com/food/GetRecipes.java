package gcm.android.hmkcode.com.food;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class GetRecipes extends ListActivity {

    private ProgressDialog pDialog;
    public String api = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search?query=";
    public int numberOfRecipes = 100;
    public JSONArray recipeList = null;
    public ArrayList<HashMap<String, String>> recipes;
    public ArrayList<String> imageURLS = new ArrayList<String>();
    public String foodType;
    public int time = 60;
    public Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicken);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            foodType = extras.getString("id");
            recipes = new ArrayList<HashMap<String, String>>();
            ListView lv = getListView();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // getting values from selected ListItem
                    String ids = ((TextView) view.findViewById(R.id.name)).getText().toString();
                    String title = ((TextView) view.findViewById(R.id.email)).getText().toString();
                    String readyIn = ((TextView) view.findViewById(R.id.mobile)).getText().toString();

                    Intent in = new Intent(getApplicationContext(), SelectedRecipe.class);
                    in.putExtra("id", ids);
                    in.putExtra("title", title);
                    in.putExtra("ready", readyIn);
                    in.putExtra("link", imageURLS.get(position));
                    startActivity(in);

                }
            });

            // Calling async task to get json
            new GetRecipe().execute();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chicken, menu);
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

    private class GetRecipe extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(GetRecipes.this);
            pDialog.setMessage("Getting Recipes! ....");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            JSONObject jsonObj = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(""+api+ "" + foodType + "&number=" + numberOfRecipes + "&limitLicense=true");
            httppost.setHeader("X-Mashape-Authorization", "5VSMYMsFj4msh0QQAjh7CCxfTaQqp1WVtbmjsnGgPs5B2mmY5k");

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                String responseBody = httpclient.execute(httppost, responseHandler);
                jsonObj = new JSONObject(responseBody);

            }catch (Exception e){e.printStackTrace();}

            Log.d("Response: ", "> " + jsonObj);

            if (jsonObj != null) {
                try {


                    // Getting JSON Array node
                    recipeList = jsonObj.getJSONArray("results");

                    // looping through All Contacts
                    for (int i = 0; i < recipeList.length(); i++) {
                        JSONObject c = recipeList.getJSONObject(i);
                        try {
                            int t = Integer.parseInt(c.getString("readyInMinutes"));

                            if(t<=time) {
                                String id = c.getString("id");
                                String name = c.getString("title");
                                String ready = c.getString("readyInMinutes");
                                imageURLS.add(c.getString("imageUrls"));

                                // tmp hashmap for single contact
                                HashMap<String, String> contact = new HashMap<String, String>();

                                // adding each child node to HashMap key => value
                                contact.put("id", id);
                                contact.put("title", name);
                                contact.put("readyInMinutes", ready);

                                // adding contact to contact list
                                recipes.add(contact);
                            }

                        }catch (Exception e){e.printStackTrace(); System.out.println("ohh dear...");}

                    }
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
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    GetRecipes.this, recipes,
                    R.layout.list_item, new String[] { "id", "title",
                    "readyInMinutes" }, new int[] { R.id.name,
                    R.id.email, R.id.mobile });

            setListAdapter(adapter);
        }

    }

}
