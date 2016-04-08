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
import java.util.Random;


public class GetRecipes extends ListActivity {

    private ProgressDialog pDialog;
    public String ingredients = "";
    public String nutrients = "";
    public String units = "";
    public String amount = "";
    public Recipe selectedRecipe;
    public String api;
    public int numberOfRecipes = 50;
    public JSONArray recipeList = null;
    public JSONArray ingredientsList = null;
    public JSONArray nutrientsList = null;
    public String responseBody;
    public ArrayList<String> title;
    public ArrayList<String> recipeId;
    public ArrayList<String> cookingTime;
    public ArrayList<String> imageURLS = new ArrayList<String>();
    public String foodType;
    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_recipes);

        listView = (ListView) findViewById(android.R.id.list);

        Random random = new Random();

        // generate a random integer from 0 to 899,
       // int rand = random.nextInt(100);
        int rand = 0;
       // System.out.println("RAND: " + rand);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getString("ingredients") != null) // get the ingredients from the tex tline.
            {
                String list = extras.getString("ingredients");
                api = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?ingredients=" +
                        list + "&limitLicense=true";
            }
            else { // Is when one of DA  buttons are pressed
                foodType = extras.getString("id");
                api = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search?query="+ foodType
                        + "&number=" + numberOfRecipes + "&offset=" + rand + "&limitLicense=true";
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    ListItem newsData = (ListItem) listView.getItemAtPosition(position);

                    String ids = ((TextView)listView.findViewById(R.id.date)).getText().toString();
                    String title = ((TextView)listView.findViewById(R.id.title)).getText().toString();
                    String readyIn = ((TextView) listView.findViewById(R.id.reporter)).getText().toString();
                    String filteredId = ids.substring(11); // Removes label

                    selectedRecipe = new Recipe(filteredId,title,readyIn,null,imageURLS.get(position));

                    new Ingredients().execute();
                }
            });
            // Calling async task to get json
            new GetRecipe().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_recipes, menu);
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
            /* Showing progress dialog*/
            pDialog = new ProgressDialog(GetRecipes.this);
            pDialog.setMessage("Getting Recipes! ....");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            JSONObject jsonObj = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(api);
            httppost.setHeader("X-Mashape-Authorization", "5VSMYMsFj4msh0QQAjh7CCxfTaQqp1WVtbmjsnGgPs5B2mmY5k");

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                responseBody = httpclient.execute(httppost, responseHandler);
                jsonObj = new JSONObject(responseBody);

            }catch (Exception e){e.printStackTrace();}

            Log.d("Response: ", "> " + jsonObj);

            if (jsonObj != null) {
                try {
                    // Getting JSON Array node
                    recipeList = jsonObj.getJSONArray("results");
                    title = new ArrayList<String>();
                    cookingTime = new ArrayList<String>();
                    recipeId = new ArrayList<String>();

                    for (int i = 0; i < recipeList.length(); i++) {
                        JSONObject c = recipeList.getJSONObject(i);
                        try {
                             String id = c.getString("id");
                             String name = c.getString("title");
                             String ready = c.getString("readyInMinutes");
                             imageURLS.add(c.getString("image"));

                             title.add(name);
                            cookingTime.add(ready);
                            recipeId.add(id);

                        }catch (Exception e){e.printStackTrace(); System.out.println("ohh dear...");}

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    recipeList = new JSONArray(responseBody);
                    title = new ArrayList<String>();
                    cookingTime = new ArrayList<String>();
                    recipeId = new ArrayList<String>();

                    for (int i = 0; i < recipeList.length(); i++) {
                        JSONObject c = recipeList.getJSONObject(i);

                            String id = c.getString("id");
                            String name = c.getString("title");

                            imageURLS.add(c.getString("image"));
                            title.add(name);
                            cookingTime.add("20");
                            recipeId.add(id);
                        }

                }catch (Exception e){e.printStackTrace();}
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

            ArrayList<ListItem> listData = getListData();
            listView.setAdapter(new CustomListAdapter(getApplication(),listData));
        }

    }

    private class Instructions extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(GetRecipes.this);
            pDialog.setMessage("Getting Instructions....");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            JSONObject jsonStr = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" + selectedRecipe.getId() + "/information");
            httppost.setHeader("X-Mashape-Authorization", "5VSMYMsFj4msh0QQAjh7CCxfTaQqp1WVtbmjsnGgPs5B2mmY5k");

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                String responseBody = httpclient.execute(httppost, responseHandler);
                jsonStr = new JSONObject(responseBody);

            }catch (Exception e){e.printStackTrace();}

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    selectedRecipe.setInstructions(jsonStr.getString("instructions"));

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

            Intent in = new Intent(getApplicationContext(), SelectedRecipe.class);
            in.putExtra("id", selectedRecipe.getId());
            in.putExtra("title", selectedRecipe.getTitle());
            in.putExtra("ready", selectedRecipe.getTime());
            in.putExtra("link", selectedRecipe.getImageLink());
            in.putExtra("instructions",selectedRecipe.getInstructions());
            in.putExtra("ingredients",ingredients);
            in.putExtra("nutrients",nutrients);
            in.putExtra("units",units);
            in.putExtra("amount",amount);
            startActivity(in);
        }

    }

    private class Ingredients extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(GetRecipes.this);
            pDialog.setMessage("Getting Ingredients....");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            JSONObject jsonObj = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" + selectedRecipe.getId() + "/information?includeNutrition=true");
            httppost.setHeader("X-Mashape-Authorization", "5VSMYMsFj4msh0QQAjh7CCxfTaQqp1WVtbmjsnGgPs5B2mmY5k");

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                responseBody = httpclient.execute(httppost, responseHandler);
                jsonObj = new JSONObject(responseBody);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d("Response: ", "> " + jsonObj);

            if (jsonObj != null) {
                try {
                    // Getting JSON Array node
                    ingredientsList = jsonObj.getJSONArray("extendedIngredients");
                  //  nutrientsList = jsonObj.getJSONArray("nutrients");

                    // looping through All Contacts
                    for (int i = 0; i < ingredientsList.length(); i++) {
                        JSONObject c = ingredientsList.getJSONObject(i);
                        try {

                            ingredients += c.getString("name") + ",";

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                        JSONObject nut = jsonObj.getJSONObject("nutrition");
                        nutrientsList = nut.getJSONArray("nutrients");

                        for(int y=0; y<nutrientsList.length(); y++)
                        {
                            JSONObject o = nutrientsList.getJSONObject(y);
                            try {
                                nutrients += o.getString("title") +",";
                                String checker = o.getString("unit");
                                if(checker.equalsIgnoreCase("Âµg"))
                                    units += "µg" + ",";
                                else
                                    units += o.getString("unit") + ",";
                                amount += o.getString("amount") + ",";

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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

            new Instructions().execute();

        }
    }

    private ArrayList<ListItem> getListData() {
        ArrayList<ListItem> listData = new ArrayList<ListItem>();

        for (int i = 0; i < imageURLS.size(); i++) {
            ListItem newsData = new ListItem();

            String link = imageURLS.get(i);

            if (!link.substring(0, 4).equalsIgnoreCase("http"))
                link = "https://spoonacular.com/recipeImages/" + imageURLS.get(i);

            newsData.setUrl(link);
            newsData.setHeadline(title.get(i));
            newsData.setReporterName(cookingTime.get(i));
            newsData.setDate("Recipe ID: " + recipeId.get(i));
            listData.add(newsData);
        }

        return listData;
    }

}
