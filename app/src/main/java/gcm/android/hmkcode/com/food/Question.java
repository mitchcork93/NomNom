package gcm.android.hmkcode.com.food;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;


public class Question extends ActionBarActivity {

    String api = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/quickAnswer?";
    private String question;
    private String answer;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion(){
        return question;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
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

    public void getAnswer(View v){

        TextView q = (TextView)findViewById(R.id.typedQuestion);
        String unFilteredQuestion = q.getText().toString();
        question = unFilteredQuestion.replaceAll("\\s+","%20");
        new query().execute();
    }

    private class query extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Question.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            JSONObject jsonStr = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(api+"q=" + question);
            httppost.setHeader("X-Mashape-Authorization", "5VSMYMsFj4msh0QQAjh7CCxfTaQqp1WVtbmjsnGgPs5B2mmY5k");

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                String responseBody = httpclient.execute(httppost, responseHandler);
                jsonStr = new JSONObject(responseBody);

            }catch (Exception e){e.printStackTrace();}

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    answer = jsonStr.getString("answer");

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

            TextView text = (TextView)findViewById(R.id.answer);
            text.setText(answer);
        }

    }
}
