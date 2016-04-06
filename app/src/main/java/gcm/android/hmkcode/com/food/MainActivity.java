package gcm.android.hmkcode.com.food;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("NomNom");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchView trial = (SearchView) menu.findItem(R.id.action_question).getActionView();
        trial.setSubmitButtonEnabled(true);
        trial.setQueryHint("Ask a Question");

        trial.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }


            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() != 0) {
                    Intent intent = new Intent(getBaseContext(), Question.class);
                    intent.putExtra("question",query);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
        }

        return super.onOptionsItemSelected(item);
    }

    public void askQuestion(View v){
        Intent intent = new Intent(getBaseContext(), Question.class);
        startActivity(intent);
    }

    public void getChicken(View v){
        Intent intent = new Intent(getBaseContext(), GetRecipes.class);
        intent.putExtra("id", "chicken");
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

    public void getFish(View v){
        Intent intent = new Intent(getBaseContext(), GetRecipes.class);
        intent.putExtra("id","fish");
        startActivity(intent);
    }

    public void getJunk(View v){
        Intent intent = new Intent(getBaseContext(), GetRecipes.class);
        intent.putExtra("id","burger");
        startActivity(intent);
    }

    public void getGrain(View v){
        Intent intent = new Intent(getBaseContext(), GetRecipes.class);
        intent.putExtra("id","pasta");
        startActivity(intent);
    }

    public void findByIngredients(View v){
        Intent intent = new Intent(getBaseContext(), GetRecipes.class);
        EditText input = (EditText)findViewById(R.id.ingredients);
        intent.putExtra("ingredients",input.getText().toString());
        startActivity(intent);
    }

    public void shoppingList(MenuItem v){
        Intent intent = new Intent(getBaseContext(), ShoppingLists.class);
        startActivity(intent);
    }
}
