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
    public String ingredients = "";
    public String nutrients = "";
    public String units = "";
    public String amount = "";
    public Recipe selectedRecipe;
    public String api;
    public int numberOfRecipes = 100;
    public JSONArray recipeList = null;
    public JSONArray ingredientsList = null;
    public JSONArray nutrientsList = null;
    public String responseBody;
    public ArrayList<HashMap<String, String>> recipes;
    public ArrayList<String> imageURLS = new ArrayList<String>();
    public String foodType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_recipes);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getString("ingredients") != null)
            {
                String list = extras.getString("ingredients");
                System.out.println(list);
                api = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?ingredients=" + list + "&limitLicense=true";
            }
            else {
                foodType = extras.getString("id");
                api = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search?query="+ foodType + "&number=" + numberOfRecipes + "&limitLicense=true";
            }
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

                    selectedRecipe = new Recipe(ids,title,readyIn,null,imageURLS.get(position));

                    new Ingredients().execute();

                  //  https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/{id}/information

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

                    // looping through All Contacts
                    for (int i = 0; i < recipeList.length(); i++) {
                        JSONObject c = recipeList.getJSONObject(i);
                        try {
                            int t = Integer.parseInt(c.getString("readyInMinutes"));

                             String id = c.getString("id");
                             String name = c.getString("title");
                             String ready = c.getString("readyInMinutes");
                             imageURLS.add(c.getString("image"));

                                // tmp hashmap for single contact
                             HashMap<String, String> contact = new HashMap<String, String>();

                                // adding each child node to HashMap key => value
                             contact.put("id", id);
                             contact.put("title", name);
                             contact.put("readyInMinutes", ready);

                                // adding contact to contact list
                             recipes.add(contact);

                        }catch (Exception e){e.printStackTrace(); System.out.println("ohh dear...");}

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    recipeList = new JSONArray(responseBody);

                    for (int i = 0; i < recipeList.length(); i++) {
                        JSONObject c = recipeList.getJSONObject(i);

                            String id = c.getString("id");
                            String name = c.getString("title");
                            imageURLS.add(c.getString("image"));

                            // tmp hashmap for single contact
                            HashMap<String, String> contact = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            contact.put("id", id);
                            contact.put("title", name);
                            contact.put("readyInMinutes", "10");

                            // adding contact to contact list
                            recipes.add(contact);

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

    private class Instructions extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(GetRecipes.this);
            pDialog.setMessage("Getting instructions....");
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
            System.out.println("PUTTING: " + units);
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
                    System.out.println("Investigate: " + jsonObj.toString());
                  //  nutrientsList = jsonObj.getJSONArray("nutrients");

                    // looping through All Contacts
                    for (int i = 0; i < ingredientsList.length(); i++) {
                        JSONObject c = ingredientsList.getJSONObject(i);
                        try {

                            ingredients += c.getString("name") + ",";

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("ohh dear...");
                        }
                    }

                        JSONObject nut = jsonObj.getJSONObject("nutrition");
                        System.out.println("Nutlist: " + nut.toString());
                        nutrientsList = nut.getJSONArray("nutrients");

                        for(int y=0; y<nutrientsList.length(); y++)
                        {
                            JSONObject o = nutrientsList.getJSONObject(y);
                            try {
                                nutrients += o.getString("title") +",";
                                System.out.println("ADDING: " + o.getString("unit"));
                                String checker = o.getString("unit");
                                if(checker.equalsIgnoreCase("Âµg"))
                                    units += "µg" + ",";
                                else
                                    units += o.getString("unit") + ",";
                                amount += o.getString("amount") + ",";

                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("ohh dear...");
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

            System.out.println("INGREDIENTS: " + ingredients);

            new Instructions().execute();

        }
    }

}
