package com.example.atapp.sqliteAccess;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.atapp.MainActivity;
import com.example.atapp.model.Worker;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Arrays;

import static android.content.ContentValues.TAG;
import static com.example.atapp.model.Worker.ENTER_EXIT;
import static com.example.atapp.model.Worker.NAME;
import static com.example.atapp.model.Worker.TABLE_NAME;
import static com.example.atapp.model.Worker.TIME;

public class GetDatabase {

    private static final int DB_VERSION = 1; //required for the constructor
    private static final String dbName = "workers.db";

    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase db ;

    public GetDatabase(Context context) {
        this.sqLiteOpenHelper = new SQLiteHelper(context, dbName, null, DB_VERSION);
    }

    public GetDatabase(Context context, String newName, int newId) {
        this.sqLiteOpenHelper = new SQLiteHelper(context, newName, newId);
    }

    public void open() {
       db = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        if (sqLiteOpenHelper != null) {
            sqLiteOpenHelper.close();
        }
    }

    public ArrayList<String> getWorkersNames() {

        open();
        // sorting orders
        String sortOrder =
                NAME + " ASC";

        Cursor cursor = db.query(TABLE_NAME, //Table to query
                new String[]{NAME},
                null,
                null,
                null,
                null,
                sortOrder);
        ArrayList<String> namesArray = new ArrayList<>();

        if (cursor.moveToFirst()) {

            while(cursor.moveToNext()){

                String name = cursor.getString(cursor.getColumnIndex(NAME));
                namesArray.add(name);
            }
        }
        cursor.close();
        db.close();

        Log.d("getWorkersNames()", namesArray.toString());

        return namesArray;
    }

    public ArrayList<Worker> getWorkerRecord(String selectedName) {

        open();
        // sorting orders

        String[] columns = {
                ENTER_EXIT,
                TIME,
        };

        String sortOrder =
                Worker.TIME + " ASC";

        Cursor cursor = db.query("attendance", //Table to query
                columns,
                NAME + " = ?",
                new String[]{
                        "" + selectedName
                },
                null,
                null,
                TIME);
        ArrayList<Worker> workerRecord = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do{
                Worker worker = new Worker();
                worker.setEnterExit(cursor.getString(cursor.getColumnIndex(ENTER_EXIT)));
                worker.setDate(cursor.getString(cursor.getColumnIndex(TIME)));
                workerRecord.add(worker);
                Log.d("getWorkersNames()", worker.toString());
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return workerRecord;
    }

    public void removeWorker(String selectedName) {
        open();
        db.delete(TABLE_NAME, "name = ?", new String[]{selectedName});
        close();
    }
}