package gcm.android.hmkcode.com.food;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by mitch on 11/03/2016.
 */
public class Recipe {

    private String id;
    private String title;
    private String time;
   // private ArrayList<String> ingredients;

    Recipe(){}

    public Recipe(String id, String title, String time)
    {
        setId(id);
        setTitle(title);
        setTime(time);
      //  setIngredients();
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
/*
    public void setIngredients(){
        this.ingredients=ingredients;
    }*/

    public String getTitle(){
        return title;
    }

    public String getTime(){
        return time;
    }
/*
    public ArrayList<String> getIngredients(){
        return ingredients;
    }*/

    public String getId(){
        return id;
    }
}
