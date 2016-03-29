package gcm.android.hmkcode.com.food;

/**
 * Created by mitch on 29/03/2016.
 */
public class Item {

    private int id;
    private String name;
    private int listId;

    public Item(int id, String name,int listId){
        setId(id);
        setName(name);
        setListId(listId);
    }

    public void setId(int id){
        this.id=id;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setListId(int listId){
        this.listId=listId;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public int getListId(){
        return listId;
    }
}
