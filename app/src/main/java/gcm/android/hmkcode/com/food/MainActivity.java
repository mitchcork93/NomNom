package gcm.android.hmkcode.com.food;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import android.content.Intent;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    public ArrayList<Recipe> stuff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void askQuestion(View v){
        Intent intent = new Intent(getBaseContext(), Question.class);
        startActivity(intent);
    }

    public void getChicken(View v){
        Intent intent = new Intent(getBaseContext(), GetRecipes.class);
        intent.putExtra("id","chicken");
        startActivity(intent);
    }

    public void getSteak(View v){
        Intent intent = new Intent(getBaseContext(), GetRecipes.class);
        intent.putExtra("id","steak");
        startActivity(intent);
    }

    public void getVeg(View v){
        Intent intent = new Intent(getBaseContext(), GetRecipes.class);
        intent.putExtra("id","vegetable");
        startActivity(intent);
    }

    public void findByIngredients(View v){
        Intent intent = new Intent(getBaseContext(), GetRecipes.class);
        EditText input = (EditText)findViewById(R.id.ingredients);
        intent.putExtra("ingredients",input.getText().toString());
        startActivity(intent);
    }

}
