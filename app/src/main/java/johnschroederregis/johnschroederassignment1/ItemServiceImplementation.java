package johnschroederregis.johnschroederassignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import johnschroederregis.johnschroederassignment1.ItemPackageModel.ItemSrvc;

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemServiceImplementation extends SQLiteOpenHelper implements ItemSrvc {

    private final String fileName = "List1.sio";
    private final String fileName2 = "List2.sio";
    private ArrayList<String> itemArrayList = new ArrayList<String>();
    private List<String> itemArrayList2 = new ArrayList<String>();
    private Context appContext;
    public final static String tableName =  "listItems";

    private static final String DBNAME = "ListItems";
    private static final int DBVERSION = 1;
    private String createListTable = "create table listItems(id integer primary key autoincrement, " + "itemName text, itemDescription text)";


    public ItemServiceImplementation(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("DataBase1", "creating table");
        sqLiteDatabase.execSQL(createListTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d("DataBase1", "on upgrade");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ tableName);
        onCreate(sqLiteDatabase);
    }

    public void addItemDatabase(String a, String b){

        Log.d("ContProv", "adding items to database");
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("itemName", a);
        Log.d("ContProv", "values inserted into db "+values.toString());
        long rowOfInsertId =  sqLiteDatabase.insert(tableName, null, values);
        sqLiteDatabase.close();
        Log.d("ContProv", "adding items to database");
        return;
    }


    public ArrayList<String> retrieveDatabaseItems1(){
        Log.d("DataBase1", "retrieve database item 1");
        ArrayList<String> itemArray1 = new ArrayList<String>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(tableName, new String[] {"itemName","itemDescription"},
                null, null, null, null, null, null);
        Log.d("DataBase1", "retrieve database item after cursor made");
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                itemArray1.add(cursor.getString(0));
                cursor.moveToNext();
                Log.d("DataBase1", "while loop ");
            }

            Log.d("DataBase1", cursor.getColumnName(0).toString());
            cursor.close();
        }catch(Exception e){
            Log.d("DataBase1", "nothin in database exception 1");
        }
        return itemArray1;
    }

    public List<String> retrieveDatabaseItemDesc2(){
        Log.d("DataBase1", "retrieve database item Description");
        List<String> itemArray1 = new ArrayList<String>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(tableName, new String[] {"itemName","itemDescription"},
                null, null, null, null, null, null);
        Log.d("DataBase1", "retrieve database item after cursor made 2");
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Log.d("DataBase1", "while loop 2 ");
                itemArray1.add(cursor.getString(1));
                cursor.moveToNext();
            }
            cursor.close();
        }catch(Exception e){
            Log.d("DataBase1", "nothin in database exception 2");
        }
        return itemArray1;
    }

    public List<String> retrieveDatabaseItems2(){
        return itemArrayList2;
    }


    private void readfile() {
        try {
            Log.d("A1", "readfiles, success");
            ObjectInputStream ois = new ObjectInputStream(appContext.openFileInput(fileName));
            itemArrayList = (ArrayList<String>) ois.readObject();
            ois.close();
            ObjectInputStream ois2 = new ObjectInputStream(appContext.openFileInput(fileName2));
            itemArrayList2 = (ArrayList<String>) ois2.readObject();
            ois2.close();
        } catch (Exception e) {
            Log.d("A1", "read file nothing/fail" + e.toString());
        }
    }

    private void writeFile() {
        try {
            Log.d("A1", "writefiles success");
            ObjectOutputStream oos = new ObjectOutputStream(appContext.openFileOutput(fileName, Context.MODE_PRIVATE));
            oos.writeObject(itemArrayList);
            oos.flush();
            oos.close();
            ObjectOutputStream oos2 = new ObjectOutputStream(appContext.openFileOutput(fileName2, Context.MODE_PRIVATE));
            oos2.writeObject(itemArrayList2);
            oos2.flush();
            oos2.close();
        } catch (Exception e) {
            Log.d("A1", "write file fail" + e.toString());
        }
    }

    public ArrayList<String> retrieveItemsInList() {
        Log.d("A1", "retrieving items in list array1");
        return itemArrayList;
    }

    public List<String> retrieveItemsInList2() {
        Log.d("A1", "retrieving items in list array2");
        return itemArrayList2;
    }

    public String addItemsList1(String string, String string2) {
        itemArrayList.add(string);
        itemArrayList2.add(string2);
        Log.d("A1", "added strings to arraylists");
        writeFile();
        return string;
    }
}
