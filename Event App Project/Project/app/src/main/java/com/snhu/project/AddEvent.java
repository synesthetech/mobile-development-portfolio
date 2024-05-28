package com.snhu.project;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.snhu.project.EventDatabaseHelper;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Button;
import android.content.Context;
import android.app.Activity;
import android.util.Log;




public class AddEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Transition from AddButton from Main Activity

        // EditText in layout
        // Create the EditText fields
        EditText nameEdit = findViewById(R.id.addName);
        EditText dateEdit = findViewById(R.id.addDate);
        EditText infoEdit = findViewById(R.id.addInfo);
        Button addButton = findViewById(R.id.addButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        // register and store the text entered into EditText into name, date, desc
        // OnClickListener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdit.getText().toString();
                String date = dateEdit.getText().toString();
                String info = infoEdit.getText().toString();
                try (EventDatabaseHelper dbHelper = new EventDatabaseHelper(AddEvent.this)) {
                    long newEventId = dbHelper.addEvent(name, date, info);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("eventId", newEventId);
                    Log.e("AddEvent", "eventId is " + newEventId);
                    resultIntent.putExtra("eventName", name);
                    resultIntent.putExtra("eventDate", date);
                    resultIntent.putExtra("eventInfo", info);
                    setResult(Activity.RESULT_OK, resultIntent);
                } catch (Exception ex) {
                    Log.e("AddEvent", "Add event error occurred", ex);
                }
                finish();

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();

            }
        });

    }
}