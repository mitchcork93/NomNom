package gcm.android.hmkcode.com.food;

/**
 * Created by mitch on 29/03/2016.
 */
public class List {

    private int id;
    private String name;
    private String date;

    public List(int id, String name,String date){
        setId(id);
        setName(name);
        setDate(date);
    }

    public void setId(int id){
        this.id=id;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setDate(String date){
        this.date=date;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDate(){
        return date;
    }
}
