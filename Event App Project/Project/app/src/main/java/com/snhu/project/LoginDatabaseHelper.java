package com.snhu.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int VERSION = 1;


    public LoginDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);

    }


    private static final class UsersTable {
        private static final String TABLE = "users";
        private static final String COL_ID = "_id";
        private static final String COL_USERNAME = "username";
        private static final String COL_PASSWORD = "password";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + LoginDatabaseHelper.UsersTable.TABLE + " (" +
                LoginDatabaseHelper.UsersTable.COL_ID + " integer primary key autoincrement, " +
                LoginDatabaseHelper.UsersTable.COL_USERNAME + " text, " +
                LoginDatabaseHelper.UsersTable.COL_PASSWORD + " text) ");
    }


    public void addUserToDb(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LoginDatabaseHelper.UsersTable.COL_USERNAME, username);
        values.put(LoginDatabaseHelper.UsersTable.COL_PASSWORD, password);
        db.insert(LoginDatabaseHelper.UsersTable.TABLE, null, values);
    }

    /*public boolean checkRegister(String username) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select * from " + LoginDatabaseHelper.UsersTable.TABLE + " where " + UsersTable.COL_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{username});
        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        int count = cursor.getInt(0);
        return count > 0;

    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + LoginDatabaseHelper.UsersTable.TABLE);
        onCreate(db);
    }
}
