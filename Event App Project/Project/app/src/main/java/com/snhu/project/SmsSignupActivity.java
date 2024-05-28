package com.snhu.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;

public class SmsSignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sms_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.smsLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button noButton = findViewById(R.id.noButton);
        Button yesButton = findViewById(R.id.yesButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toHome();

            }
        });
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toHomeWithMsg();

            }
        });
    }

    private void toHome() {
        Intent intent = new Intent(SmsSignupActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void toHomeWithMsg() {
        Intent intent = new Intent(SmsSignupActivity.this, MainActivity.class);
        intent.putExtra("showConfirmation", true);
        startActivity(intent);

    }



}
