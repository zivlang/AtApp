package com.example.atapp.sqliteAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.atapp.model.Worker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.atapp.model.Worker.ENTER_EXIT;
import static com.example.atapp.model.Worker.NAME;
import static com.example.atapp.model.Worker.SQLITE_ID;
import static com.example.atapp.model.Worker.TIME;

public class SQLiteHelper extends SQLiteOpenHelper {

    private String workerName, selectedName, enterOrExit;
    private int workerId;

    private static final String DB_NAME = "workers.db";
    private static final int DB_VERSION = 1; //required for the constructor
    private static final String WORKERS_TABLE = "workers";
    private static final String ATTENDANCE_TABLE = "attendance";

    private Context context;

    SQLiteHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName, null, version);
        SQLiteDatabase db = getWritableDatabase();
        onCreate(db);
    }

    SQLiteHelper(Context context, String workerName, int workerId) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        this.workerName = workerName;
        this.workerId = workerId;
        SQLiteDatabase db = getWritableDatabase();
        insertNewWorkerToSQLIte(db);
        db.close();
    }

    public SQLiteHelper(Context context, String selectedName, String enterOrExit) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        this.selectedName = selectedName;
        this.enterOrExit = enterOrExit;
        SQLiteDatabase db = getWritableDatabase();
        insertEnterExit(db);
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        createWorkersTable(db);
        createAttendanceTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void createWorkersTable(SQLiteDatabase db) {

        //creating workers table
        String CREATE_SQL_TABLE_STRING = "CREATE TABLE IF NOT EXISTS "+ WORKERS_TABLE
                + " ("
                + SQLITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
                + NAME + " TEXT,"
                + Worker.WORKER_ID + " TEXT"
                + ")";

        db.execSQL(CREATE_SQL_TABLE_STRING);
    }

    public void createAttendanceTable(SQLiteDatabase db) {

        String CREATE_SQL_TABLE_STRING = "CREATE TABLE IF NOT EXISTS "+ ATTENDANCE_TABLE
                + " ("
                + SQLITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
                + NAME + " TEXT,"
                + ENTER_EXIT + " TEXT,"
                + TIME + " TEXT"
                + ")";

        db.execSQL(CREATE_SQL_TABLE_STRING);
    }

    private void insertNewWorkerToSQLIte(SQLiteDatabase db)  {

        ContentValues insertValues = new ContentValues();

        insertValues.put(NAME, workerName);
        insertValues.put(Worker.WORKER_ID, workerId);

        long res = db.insert(WORKERS_TABLE, null, insertValues);

        Toast.makeText(context, workerName + " ID: " + workerId + " was added", Toast.LENGTH_LONG).show();
    }

    public String getTableAsString(SQLiteDatabase db, String tableName) {
        StringBuilder tableString = new StringBuilder(String.format("Table %s:\n", tableName));
        Cursor allRows  = db.rawQuery("SELECT * FROM " + "'" + tableName + "'", null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString.append(String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name))));
                }
                tableString.append("\n");

            } while (allRows.moveToNext());
        }
        return tableString.toString();
    }

    private void insertEnterExit(SQLiteDatabase db) {
        Cursor cursorId = db.rawQuery("SELECT * FROM attendance WHERE name = ? ORDER BY id DESC LIMIT 1", new String[]{selectedName});

        String enterOrExitFromDB;

        if(cursorId.moveToFirst() ) {

            enterOrExitFromDB = cursorId.getString(cursorId.getColumnIndex(ENTER_EXIT));

            if(enterOrExit.equals(enterOrExitFromDB)){

                cursorId.close();
                Toast.makeText(context, selectedName + " had already " + enterOrExit + "ed", Toast.LENGTH_SHORT).show();
            }
            else{
                insertAttendance(enterOrExit, selectedName, db);
            }
        }
        else{
            insertAttendance(enterOrExit, selectedName, db);
        }
    }

    private void insertAttendance(String enterOrExit, String selectedName, SQLiteDatabase db) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy  HH:mm");
        simpleDateFormat.getNumberFormat();
        String date1 = simpleDateFormat.format(Calendar.getInstance().getTime());

        ContentValues insertValues = new ContentValues();

        insertValues.put(ENTER_EXIT, enterOrExit);
        insertValues.put(NAME, selectedName);
        insertValues.put(TIME, date1);

        long res = db.insert(ATTENDANCE_TABLE, null, insertValues);

        Toast.makeText(context, selectedName + " " + enterOrExit + "ed work", Toast.LENGTH_SHORT).show();
    }
}