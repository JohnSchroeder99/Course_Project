package johnschroederregis.johnschroederassignment1.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;

import johnschroederregis.johnschroederassignment1.ItemServiceImplementation;

import static johnschroederregis.johnschroederassignment1.ItemServiceImplementation.tableName;

public class ContentProvider extends android.content.ContentProvider {
    public ItemServiceImplementation db;

    Context context = null;
    public static final String AUTHORITY_NAME = "johnschroederregis.johnschroederassignment1.database.ContentProvider";

    ArrayList<String> arrayList = null;


    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY_NAME, "GET", 1);



        uriMatcher.addURI(AUTHORITY_NAME, "POST",2);
        uriMatcher.addURI(AUTHORITY_NAME, "PUT", 3);
        uriMatcher.addURI(AUTHORITY_NAME, "DELETE", 4);
    }

    @Override
    public boolean onCreate() {
        context = getContext();
        db = new ItemServiceImplementation(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {




        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
       if(sqLiteDatabase != null){
            sqLiteDatabase.delete("listItems",null, null);
       }


        cursor = sqLiteDatabase.query("listItems", new String[] {"itemName","itemDescription"},
                null, null, null, null, null, null);
        Log.d("ContProv", "retrieve database item after cursor made 2");
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                arrayList.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }catch(Exception e){
            Log.d("ContProv", "nothing in database exception 2");
        }
        return cursor;
    }



    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d("ContProv", "inserting into db through content prov");
            db.addItemDatabase("itemName", values.toString());
        Log.d("ContProv", "inserted into db through content prov");
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
