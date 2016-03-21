package gcm.android.hmkcode.com.food;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mitch on 11/03/2016.
 */
public class Recipe {

    private String id;
    private String title;
    private String time;
    private String instructions;
    private String imageLink;
    public Activity source;
    public ProgressDialog pDialog;
    public Bitmap bitmap;
   // private ArrayList<String> ingredients;

    Recipe(){}

    public Recipe(String id, String title, String time, Activity source,String link)
    {
        setId(id);
        setTitle(title);
        setTime(time);
        setSource(source);
        setImageLink(link);
    }

    public void setId(String id){
        this.id=id;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public void setTime(String time){
        this.time=time;
    }

    public void setSource(Activity source){
        this.source=source;
    }

    public void setImageLink(String link){
        this.imageLink=link;
    }

    public String getTitle(){
        return title;
    }

    public String getTime(){
        return time;
    }

    public String getId(){
        return id;
    }

    public String getImageLink(){
        return imageLink;
    }

    public void getImage(){
        new GetImage().execute(getImageLink());
    }

    public void getInstructions(){
        new Instructions().execute();
    }

    private class Instructions extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(source);
            pDialog.setMessage("Getting instructions....");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            JSONObject jsonStr = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" + getId() + "/information");
            httppost.setHeader("X-Mashape-Authorization", "5VSMYMsFj4msh0QQAjh7CCxfTaQqp1WVtbmjsnGgPs5B2mmY5k");

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                String responseBody = httpclient.execute(httppost, responseHandler);
                jsonStr = new JSONObject(responseBody);

            }catch (Exception e){e.printStackTrace();}

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    instructions = jsonStr.getString("instructions");

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

            TextView instructionText = (TextView)source.findViewById(R.id.instructions);
            instructionText.setText(Html.fromHtml(instructions));
        }

    }

    private class GetImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            if (image != null) {
                ImageView im = (ImageView)source.findViewById(R.id.imageView);
                im.setImageBitmap(image);
            } else {

            }
        }
    }
}
