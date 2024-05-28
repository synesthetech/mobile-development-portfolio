package com.snhu.project;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    LoginDatabaseHelper dbHelper ;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new LoginDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText username = findViewById(R.id.usernameText);
        EditText password = findViewById(R.id.passwordText);
        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();

        Button signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                //startActivity(intent);
               // if (dbHelper.checkRegister(usernameText)) {
                    intent.putExtra("showLogin", true);
                    startActivity(intent);

                /*} else {
                    dbHelper.addUserToDb(usernameText, passwordText);
                    intent.putExtra("showRegister", true);
                    startActivity(intent);
                }*/
            }
        });
    }


}