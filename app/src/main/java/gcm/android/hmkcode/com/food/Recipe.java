package gcm.android.hmkcode.com.food;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TabHost;
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
public class Recipe{

    private String id;
    private String title;
    private String time;
    private String instructions;
    private String imageLink;
    public Activity source;
    public ProgressDialog pDialog;
    public Bitmap bitmap;
   // private ArrayList<String> ingredients;

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

    public void setInstructions(String instructions){
        this.instructions=instructions;
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

    public String getInstructions(){ return instructions; }

    public void getImage(){
        new GetImage().execute(getImageLink());
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
