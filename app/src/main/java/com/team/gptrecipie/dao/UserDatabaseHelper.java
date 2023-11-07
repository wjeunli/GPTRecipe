package com.team.gptrecipie.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.team.gptrecipie.model.User;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "userDb.db";
    private SQLiteDatabase db;

    public UserDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + UsersTableSchema.NAME + "(" +
                UsersTableSchema.Columns.ID + " integer primary key autoincrement, " +
                UsersTableSchema.Columns.EMAIL + " text unique, " +
                UsersTableSchema.Columns.PASSWORD + " text)"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UsersTableSchema.NAME);
        onCreate(db);
    }

    public void openDB(){
        db = this.getWritableDatabase();
    }

    public boolean createUser(User user) {
        ContentValues cv = new ContentValues();
        cv.put(UsersTableSchema.Columns.EMAIL, user.getEmail());
        cv.put(UsersTableSchema.Columns.PASSWORD, user.getPassword());
        long results = db.insert(UsersTableSchema.NAME, null, cv);
        return results != -1;
    }

    public boolean authenticateUser(String email, String password) {
        String query = "SELECT * FROM Users WHERE " + UsersTableSchema.Columns.EMAIL +
                 " = ? AND " + UsersTableSchema.Columns.PASSWORD + " = ?";
        String[] args = {email, password};
        Cursor c = db.rawQuery(query, args);
        int count = c.getCount();
        c.close();
        return count == 1;
    }
}
