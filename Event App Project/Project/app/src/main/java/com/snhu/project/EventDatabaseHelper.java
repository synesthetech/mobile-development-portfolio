package com.snhu.project;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.view.View;
import java.util.List;
import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;

public class EventDatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "events.db";
        private static final int VERSION = 1;


        public EventDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);

        }


    private static final class EventTable {
            private static final String TABLE = "events";
            private static final String COL_ID = "_id";
            private static final String COL_NAME = "name";
            private static final String COL_DATE = "date";
            private static final String COL_INFO = "info";
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + EventTable.TABLE + " (" +
                    EventTable.COL_ID + " integer primary key autoincrement, " +
                    EventTable.COL_NAME + " text, " +
                    EventTable.COL_DATE + " text, " +
                    EventTable.COL_INFO + " text) ");
        }

        public long addEvent(String name, String date, String info) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EventTable.COL_NAME, name);
            values.put(EventTable.COL_DATE, date);
            values.put(EventTable.COL_INFO, info);
            return db.insert(EventTable.TABLE, null, values);
        }

        public void deleteEvent(long id) {
            SQLiteDatabase db = getWritableDatabase();
            db.delete(EventTable.TABLE, EventTable.COL_ID + " = ?",
                    new String[] { Long.toString(id) });
        }


        public long updateEvent(long id, String name, String date, String info) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EventTable.COL_NAME, name);
            values.put(EventTable.COL_DATE, date);
            values.put(EventTable.COL_INFO, info);
            return db.update(EventTable.TABLE, values, "_id = ?",
            new String[] { Float.toString(id) });
}

        public List<List<String>> getAllEvents() {
            List<List<String>> events = new ArrayList<>();
            SQLiteDatabase db = getWritableDatabase();
            String sql = "select * from " + EventTable.TABLE;
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(0);
                    String name = cursor.getString(1);
                    String date = cursor.getString(2);
                    String info = cursor.getString(3);
                    List<String> eventDetails = new ArrayList<>();
                    eventDetails.add(Long.toString(id));
                    eventDetails.add(name);
                    eventDetails.add(date);
                    eventDetails.add(info);
                    events.add(eventDetails);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return events;
        }


        public List<String> getEventDetails(long id) {
            SQLiteDatabase db = getReadableDatabase();
            String sql = "select * from " + EventTable.TABLE + " where _id = ?";
            Cursor cursor = db.rawQuery(sql,  new String[]{String.valueOf(id)});
            List<String> eventDetails = new ArrayList<>();
            if (cursor.moveToFirst()){
                do {
                    id = cursor.getLong(0);
                    String name = cursor.getString(1);
                    String date = cursor.getString(2);
                    String info = cursor.getString(3);
                    eventDetails.add(Long.toString(id));
                    eventDetails.add(name);
                    eventDetails.add(date);
                    eventDetails.add(info);
                } while (cursor.moveToNext());
            }
            return eventDetails;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            db.execSQL("drop table if exists " + EventTable.TABLE);
            onCreate(db);
        }

}
