package gcm.android.hmkcode.com.food;

/**
 * Created by mitch on 25/03/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by anthony on 25/03/2016.
 */
public class DBhelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Ingredients_DB";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME_ITEM = "ITEM";
    public static final String TABLE_NAME_LIST = "LIST";

    public static final String ITEM_COLUMN_ID = "ID";
    public static final String ITEM_COLUMN_NAME = "NAME";
    public static final String ITEM_COLUMN_lISTID = "LISTID";
    public static final String LIST_COLUMN_LIST = "ID";
    public static final String LIST_COLUMN_NAME = "NAME";
    public static final String LIST_COLUMN_DATE = "DATE";

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_ITEM +"(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,LISTID INTEGER,FOREIGN KEY(LISTID) REFERENCES TABLE_NAME_LIST(ID));");
        db.execSQL("CREATE TABLE " + TABLE_NAME_LIST + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,DATE DATETIME);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME_ITEM);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME_LIST);
        onCreate(db);
    }

    public boolean insertDataItem(String NAME, int LISTID){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL(
                    "INSERT INTO ITEM(NAME,LISTID) VALUES ('" + NAME + "','" + LISTID + "')"
            );
        }catch(Exception e){
            Log.e("log_tag", "Error inserting record " + e.toString());

        }
        return true;
    }

    public long insertDataList(String NAME,String DATE){
        long id = -1;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("NAME", NAME);
            values.put("DATE", DATE);

            id = db.insert(TABLE_NAME_LIST,null,values);

        }catch(Exception e){
            Log.e("log_tag", "Error inserting record " + e.toString());

        }
        return id;
    }

    public List getList(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME_LIST, new String[] { LIST_COLUMN_LIST,
                        LIST_COLUMN_NAME, LIST_COLUMN_DATE }, LIST_COLUMN_LIST + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        List list = new List(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
         return list;
    }

    public ArrayList<List> getAllLists(){
        ArrayList<List> lists = new ArrayList<List>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                List list = new List(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2));
                lists.add(list);
            } while (cursor.moveToNext());
        }
        return lists;
    }

    public ArrayList<Item> getAllItems(int id){
        ArrayList<Item> items = new ArrayList<Item>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME_ITEM + " WHERE LISTID=" + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(Integer.parseInt(cursor.getString(0)),cursor.getString(1),Integer.parseInt(cursor.getString(2)));
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items;
    }

}

