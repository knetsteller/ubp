package demo.ufgbipperv2.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

public class DBHandler {

    public static final String SENDER = "sender";
    public static final String DATE = "date";
    public static final String MESSAGE = "message";

    public static final String TABLE_NAME = "myTable";
    public static final String DATABASE_NAME = "myDatabaseName";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_CREATE = "create table mytable (sender text not null, date text not null, message text not null)";

    DatabaseHelper dbHelper;
    Context context;
    SQLiteDatabase db;

    public DBHandler(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS myTable");
            onCreate(db);
        }
    }

    public DBHandler open() {
        db = dbHelper.getWritableDatabase();

        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertData(String sender, String date, String message) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SENDER, sender);
        contentValues.put(DATE, date);
        contentValues.put(MESSAGE, message);

        return db.insertOrThrow(TABLE_NAME, null, contentValues);
    }

    public Cursor returnData() {

        return db.query(TABLE_NAME, new String[]{SENDER, DATE, MESSAGE}, null, null, null, null, null);
    }
}
