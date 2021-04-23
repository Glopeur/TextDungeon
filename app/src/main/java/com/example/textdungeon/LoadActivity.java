package com.example.textdungeon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LoadActivity extends AppCompatActivity {

    private Button buttonSlot1, buttonSlot2, buttonSlot3;
    private FloatingActionButton buttonBack;

    private DBLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        this.db = new DBLite(this);

        this.setupButton();
        this.getAvailableSave();

        this.buttonSlot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor data = db.getSlotData(0);
                int position = 0;
                byte[] blob = null;
                while (data.moveToNext()) {
                    position = data.getInt(0);
                    blob = data.getBlob(data.getColumnIndex("INV"));
                }
                String json = new String(blob);
                Gson gson = new Gson();
                ArrayList<Items> items = gson.fromJson(json, new TypeToken<ArrayList<Items>>(){}.getType());

                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                intent.putExtra("POSITION", position);
                intent.putExtra("INV", items);
                startActivity(intent);
                Toast.makeText(getBaseContext(), "Save 1 Loaded", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        this.buttonSlot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor data = db.getSlotData(1);
                int position = 0;
                byte[] blob = null;
                while (data.moveToNext()) {
                    position = data.getInt(0);
                    blob = data.getBlob(data.getColumnIndex("INV"));
                }
                String json = new String(blob);
                Gson gson = new Gson();
                ArrayList<Items> items = gson.fromJson(json, new TypeToken<ArrayList<Items>>(){}.getType());

                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                intent.putExtra("POSITION", position);
                intent.putExtra("INV", items);
                startActivity(intent);
                Toast.makeText(getBaseContext(), "Save 2 Loaded", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        this.buttonSlot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor data = db.getSlotData(2);
                int position = 0;
                byte[] blob = null;
                while (data.moveToNext()) {
                    position = data.getInt(0);
                    blob = data.getBlob(data.getColumnIndex("INV"));
                }
                String json = new String(blob);
                Gson gson = new Gson();
                ArrayList<Items> items = gson.fromJson(json, new TypeToken<ArrayList<Items>>(){}.getType());

                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                intent.putExtra("POSITION", position);
                intent.putExtra("INV", items);
                startActivity(intent);
                Toast.makeText(getBaseContext(), "Save 3 Loaded", Toast.LENGTH_LONG).show();
                finish();
            }
        });


        this.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupButton(){
        this.buttonSlot1 = findViewById(R.id.buttonLoadSlot1);
        this.buttonSlot2 = findViewById(R.id.buttonLoadSlot2);
        this.buttonSlot3 = findViewById(R.id.buttonLoadSlot3);
        this.buttonBack = findViewById(R.id.floatingActionButtonLoadBack);

        this.buttonSlot1.setEnabled(false);
        this.buttonSlot2.setEnabled(false);
        this.buttonSlot3.setEnabled(false);
    }

    private void getAvailableSave() {
       Cursor data = this.db.getData();

       if(data.getCount() != 0) {
           ArrayList<Integer> id = new ArrayList<>();
           while(data.moveToNext()) {
               id.add(data.getInt(0));
           }
           System.out.println(id);
           for (int i = 0; i < id.size(); i++) {
               switch (id.get(i)) {
                   case 0:
                       this.buttonSlot1.setEnabled(true);
                       break;
                   case 1:
                       this.buttonSlot2.setEnabled(true);
                       break;
                   case 2:
                       this.buttonSlot3.setEnabled(true);
                       break;
                   default:
                       Toast.makeText(this, "Database Error", Toast.LENGTH_LONG).show();
                       finish();
               }
           }
       }
    }
}
