package com.snhu.project;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.snhu.project.EventDatabaseHelper;
import android.widget.Button;
import android.content.Intent;
import android.view.ViewGroup;
import java.util.List;
import java.util.ArrayList;
import android.content.Context;
import android.widget.GridLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.util.Log;
import android.text.TextUtils;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> addEventLauncher;
    private ActivityResultLauncher<Intent> editEventLauncher;
    EventDatabaseHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getBooleanExtra("showLogin", false)) {
            showLoginSuccessful();
        }

        if (getIntent().getBooleanExtra("showRegister", false)) {
            showRegisterSuccessful();
        }


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homeGridLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new EventDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        GridLayout homeGridLayout = findViewById(R.id.homeGridLayout);
        TextView nameTV = new TextView(this);

        nameTV.setText("Event");
        TextView dateTV = new TextView(this);
        dateTV.setText("Date");
        TextView infoTV = new TextView(this);
        infoTV.setText("Info");

        GridLayout.LayoutParams nameParams = new GridLayout.LayoutParams();
        GridLayout.LayoutParams dateParams = new GridLayout.LayoutParams();
        GridLayout.LayoutParams infoParams = new GridLayout.LayoutParams();
        GridLayout.LayoutParams deleteParams = new GridLayout.LayoutParams();
        GridLayout.LayoutParams editParams = new GridLayout.LayoutParams();
        int rowIndex = homeGridLayout.getChildCount() / homeGridLayout.getColumnCount();
        nameParams.columnSpec = GridLayout.spec(0);
        nameParams.rowSpec = GridLayout.spec(rowIndex);
        dateParams.columnSpec = GridLayout.spec(1);
        dateParams.rowSpec = GridLayout.spec(rowIndex);
        infoParams.columnSpec = GridLayout.spec(2);
        infoParams.rowSpec = GridLayout.spec(rowIndex);
        homeGridLayout.addView(nameTV, nameParams);
        homeGridLayout.addView(dateTV, dateParams);
        homeGridLayout.addView(infoTV, infoParams);

        if (getIntent().getBooleanExtra("showConfirmation", false)) {
            showConfirmation();
        }
        addEventLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    long eventId = data.getLongExtra("eventId", -1);
                    Log.e("mainActivity after AddEvent intent", "eventId is " + eventId);
                    String eventName = data.getStringExtra("eventName");
                    String eventDate = data.getStringExtra("eventDate");
                    String eventInfo = data.getStringExtra("eventInfo");
                    addEventToGrid(eventId, eventName, eventDate, eventInfo);

                }
            }
        });
        editEventLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    long eventId = data.getLongExtra("eventId", -1);
                    String eventName = data.getStringExtra("eventName");
                    String eventDate = data.getStringExtra("eventDate");
                    String eventInfo = data.getStringExtra("eventInfo");
                    Log.e("EditEventLauncherDone", "newName " + eventName + " newDate " + eventDate + " newInfo " + eventInfo);
                    updateEventOnGrid(eventId, eventName, eventDate, eventInfo);

                }
            }
        });


        Button smsButton = findViewById(R.id.smsButton);

        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SmsSignupActivity.class);
                startActivity(intent);

            }
        });

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch add event activity
                Intent addIntent = new Intent(MainActivity.this, AddEvent.class);
                addEventLauncher.launch(addIntent);
            }
        });
    }

    private void showConfirmation() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("SMS notifications enabled!")
                .setPositiveButton("OK", (dialog, id) -> {

                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmation(long eventId) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this event?")
                .setPositiveButton("Confirm", (dialog, id) -> {
                    dbHelper.deleteEvent(eventId);
                    deleteEventFromGrid(eventId);
                })
                .setNegativeButton("Cancel", (dialog, id) -> {

                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void addEventToGrid(long id, String name, String date, String info) {
        GridLayout homeGridLayout = findViewById(R.id.homeGridLayout);
        TextView nameTV = new TextView(this);
        nameTV.setText(name);
        nameTV.setSingleLine(false);
        nameTV.setMaxLines(5);
        nameTV.setEllipsize(TextUtils.TruncateAt.END);
        nameTV.setWidth(200);
        TextView dateTV = new TextView(this);
        dateTV.setText(date);
        dateTV.setMaxLines(5);
        dateTV.setEllipsize(TextUtils.TruncateAt.END);
        dateTV.setWidth(200);
        TextView infoTV = new TextView(this);
        infoTV.setText(info);
        infoTV.setSingleLine(false);
        infoTV.setMaxLines(5);
        infoTV.setEllipsize(TextUtils.TruncateAt.END);
        infoTV.setWidth(200);

        Button deleteButton = new Button(this);
        deleteButton.setText("Delete");
        deleteButton.setOnClickListener(v -> {
            showDeleteConfirmation(id);
        });

        Button editButton = new Button(this);
        editButton.setText("Edit");
        editButton.setOnClickListener(v -> {
           // Send to edit event activity and pass the id
            Intent editIntent = new Intent(MainActivity.this, EditEvent.class);
            editIntent.putExtra("eventId", id);
            editEventLauncher.launch(editIntent);
        });

        nameTV.setTag("name" + id);
        dateTV.setTag("date" + id);
        infoTV.setTag("info" + id);
        deleteButton.setTag("delete" + id);
        editButton.setTag("edit" + id);

        //setButtonListeners(deleteButton, editButton);
        int rowIndex = homeGridLayout.getChildCount() / homeGridLayout.getColumnCount();
        GridLayout.LayoutParams nameParams = new GridLayout.LayoutParams();
        GridLayout.LayoutParams dateParams = new GridLayout.LayoutParams();
        GridLayout.LayoutParams infoParams = new GridLayout.LayoutParams();

        GridLayout.LayoutParams deleteParams = new GridLayout.LayoutParams();
        GridLayout.LayoutParams editParams = new GridLayout.LayoutParams();
        nameParams.columnSpec = GridLayout.spec(0);
        nameParams.rowSpec = GridLayout.spec(rowIndex);
        dateParams.columnSpec = GridLayout.spec(1);
        dateParams.rowSpec = GridLayout.spec(rowIndex);
        infoParams.columnSpec = GridLayout.spec(2);
        infoParams.rowSpec = GridLayout.spec(rowIndex);
        deleteParams.columnSpec = GridLayout.spec(3);
        deleteParams.rowSpec = GridLayout.spec(rowIndex);
        editParams.columnSpec = GridLayout.spec(4);
        editParams.rowSpec = GridLayout.spec(rowIndex);
        rowIndex++;
        homeGridLayout.addView(nameTV, nameParams);
        homeGridLayout.addView(dateTV, dateParams);
        homeGridLayout.addView(infoTV, infoParams);
        homeGridLayout.addView(deleteButton, deleteParams);
        homeGridLayout.addView(editButton, editParams);
    }

    private void updateEventOnGrid(long id, String name, String date, String info) {
        GridLayout homeGridLayout = findViewById(R.id.homeGridLayout);
        TextView nameTV = homeGridLayout.findViewWithTag("name" + id);
        TextView dateTV = homeGridLayout.findViewWithTag("date" + id);
        TextView infoTV = homeGridLayout.findViewWithTag("info" + id);
        Log.e("updateEvent", "name text view" + nameTV);
        nameTV.setText(name);
        dateTV.setText(date);
        infoTV.setText(info);
    }

    private void deleteEventFromGrid(long id) {
        GridLayout homeGridLayout = findViewById(R.id.homeGridLayout);
        TextView nameTV = homeGridLayout.findViewWithTag("name" + id);
        TextView dateTV = homeGridLayout.findViewWithTag("date" + id);
        TextView infoTV = homeGridLayout.findViewWithTag("info" + id);
        Button deleteButton = homeGridLayout.findViewWithTag("delete" + id);
        Button editButton = homeGridLayout.findViewWithTag("edit" + id);
        homeGridLayout.removeView(nameTV);
        homeGridLayout.removeView(dateTV);
        homeGridLayout.removeView(infoTV);
        homeGridLayout.removeView(deleteButton);
        homeGridLayout.removeView(editButton);


    }

    private void showLoginSuccessful() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("Successfully logged in")
                .setPositiveButton("OK", (dialog, id) -> {

                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showRegisterSuccessful() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("New user registered for account")
                .setPositiveButton("OK", (dialog, id) -> {

                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}