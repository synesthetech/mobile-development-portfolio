package com.snhu.project;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import java.util.List;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class EditEvent extends AppCompatActivity {

    EventDatabaseHelper dbHelper ;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_event);
        dbHelper = new EventDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText nameEdit = findViewById(R.id.editName);
        EditText dateEdit = findViewById(R.id.editDate);
        EditText infoEdit = findViewById(R.id.editInfo);
        Button saveButton = findViewById(R.id.saveButton);
        Button cancelButton = findViewById(R.id.cancelButton);
        long id = getIntent().getLongExtra("eventId",-1);
        Log.e("EditEvent", "eventId is " + id);
        // Pass the id from the edit launcher to here
        List<String> eventDetails = dbHelper.getEventDetails(id);
        String name = eventDetails.get(1);
        String date = eventDetails.get(2);
        String info = eventDetails.get(3);
        nameEdit.setText(name);
        dateEdit.setText(date);
        infoEdit.setText(info);
        Log.e("EditEvent", "name " + name + " date " + date + " info " + info);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdit.getText().toString();
                String date = dateEdit.getText().toString();
                String info = infoEdit.getText().toString();
                Intent intent = getIntent();
                try (EventDatabaseHelper dbHelper = new EventDatabaseHelper(EditEvent.this)) {
                    long eventId = dbHelper.updateEvent(id, name, date, info);
                    //intent.putExtra("eventId", eventId);
                    intent.putExtra("eventName", name);
                    intent.putExtra("eventDate", date);
                    intent.putExtra("eventInfo", info);
                    Log.e("EditEventIsDone", "name " + name + " date " + date + " info " + info);
                    setResult(Activity.RESULT_OK, intent);

                } catch (Exception ex) {
                    Log.e("EditEvent", "Edit event error occurred", ex);
                }
                finish();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });

    }


}