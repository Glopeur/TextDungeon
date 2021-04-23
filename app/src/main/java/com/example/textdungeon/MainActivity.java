package com.example.textdungeon;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonNewGame, buttonLoadGame, buttonHelp, buttonAbout, buttonExit;
    private DBLite dbLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setupDb();
        this.setupButtons();

        this.buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                startActivity(intent);
            }
        });

        this.buttonLoadGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoadActivity.class);
                startActivity(intent);
            }
        });

        this.buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), HelpActivity.class);
                startActivity(intent);
            }
        });

        this.buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AboutActivity.class);
                startActivity(intent);
            }
        });

        this.buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setupDb() {
        this.dbLite = new DBLite(this);
    }

    private void setupButtons() {
        this.buttonNewGame = findViewById(R.id.buttonNew);
        this.buttonLoadGame = findViewById(R.id.buttonLoad);
        this.buttonHelp = findViewById(R.id.buttonHelp);
        this.buttonAbout = findViewById(R.id.buttonAbout);
        this.buttonExit = findViewById(R.id.buttonExit);
    }
}
