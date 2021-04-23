package com.example.textdungeon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private ArrayList<Room> room = new ArrayList<>();
    private ArrayList<String> roomList = new ArrayList<>();

    private ArrayList<Items> items = new ArrayList<>();
    private ArrayList<String> itemsList = new ArrayList<>();

    private int startId, demonLocation;
    private Player player;

    private TextView textViewTitle;
    private Button buttonNorth, buttonWest, buttonEast, buttonSouth, buttonPickup, buttonInventory;
    private FloatingActionButton buttonBack, buttonSave;

    private DBLite sqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.initDungeon();
        this.initActivity();

        if (this.getIntent().hasExtra("POSITION")) {
            this.player.setPosition(this.getIntent().getIntExtra("POSITION", 0));
        }
        if (this.getIntent().hasExtra("INV")) {
            this.player.setInventory((ArrayList<Items>) this.getIntent().getExtras().get("INV"));
        }

        this.setRoom();

        //------------------------

        this.buttonNorth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRoom("N");
            }
        });

        this.buttonWest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRoom("W");
            }
        });

        this.buttonEast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRoom("E");
            }
        });

        this.buttonSouth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRoom("S");
            }
        });

        this.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        this.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSavePressed();
            }
        });

        this.buttonPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPickup.setEnabled(false);
                Items item = room.get(player.getPosition()).getItems();
                ArrayList<Items> inv = player.getInventory();
                inv.add(item);
                player.setInventory(inv);
                room.get(player.getPosition()).setItems(null);
                Toast.makeText(getBaseContext(), "Picked up: " + item.getName(), Toast.LENGTH_LONG ).show();
                if (item.getName().equalsIgnoreCase("chest")) endGame("WIN");
            }
        });

        this.buttonInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInventoryPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle(R.string.leaving)
                .setCancelable(true)
                .setPositiveButton(R.string.leaving_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton(R.string.leaving_deny, null).show();
    }

    private void onSavePressed() {
        new AlertDialog.Builder(this).setTitle(R.string.save)
                .setItems(R.array.saves, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (sqlite.insertData(new Save(which, player.getPosition(), player.getInventory()))) {
                            Toast.makeText(getBaseContext(), "Data saved on Slot " + (which + 1), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), "Error: Data Not Saved", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setCancelable(true)
                .setNegativeButton(R.string.leaving_deny, null).show();
    }

    private void onInventoryPressed() {

        ArrayList<String> item = new ArrayList<>();

        for (int i = 0; i < this.player.getInventory().size(); i++) {
            item.add(this.player.getInventory().get(i).getName());
        }

        new AlertDialog.Builder(this).setTitle(R.string.inventory)
                .setItems(item.toArray(new CharSequence[item.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true)
                .setNegativeButton(R.string.exit, null).show();
    }

    private void initDungeon() {

        this.readDungeon();
        this.createRooms();
        this.setDefaultRoom();
        this.createPlayer();
        this.setDemonLocation();

    }

    private void readDungeon() {
        try {
            XmlResourceParser resourceParser = this.getResources().getXml(R.xml.dungeon);
            try {
                resourceParser.next();

                int parser = resourceParser.getEventType();
                int i = 0;
                while(parser != XmlPullParser.END_DOCUMENT) {

                    if(parser == XmlPullParser.TEXT) {
                        if (i == 7) {
                            i = 0;
                        }
                        if (i < 7) {
                            this.roomList.add(resourceParser.getText());
                        }
                        i++;
                    }
                    parser = resourceParser.next();
                }
            } catch (IOException e) {

            }
        } catch (XmlPullParserException e) {

        }
    }

    private void readItems() {
        try {
            XmlResourceParser resourceParser = this.getResources().getXml(R.xml.items);
            try {
                resourceParser.next();

                int parser = resourceParser.getEventType();
                int i = 0;
                while(parser != XmlPullParser.END_DOCUMENT) {

                    if(parser == XmlPullParser.TEXT) {
                        if (i == 3) {
                            i = 0;
                        }
                        if (i < 3) {
                            this.itemsList.add(resourceParser.getText());
                        }
                        i++;
                    }
                    parser = resourceParser.next();
                }
            } catch (IOException e) {

            }
        } catch (XmlPullParserException e) {

        }
    }

    private void setDemonLocation() {
        try {
            XmlResourceParser resourceParser = this.getResources().getXml(R.xml.demon);
            try {
                resourceParser.next();

                int parser = resourceParser.getEventType();
                int i = 0;
                while(parser != XmlPullParser.END_DOCUMENT) {

                    if(parser == XmlPullParser.TEXT) {
                        if (i == 1) {
                            i = 0;
                        }
                        if (i < 1) {
                            this.demonLocation = Integer.valueOf(resourceParser.getText());
                        }
                        i++;
                    }
                    parser = resourceParser.next();
                }
            } catch (IOException e) {

            }
        } catch (XmlPullParserException e) {

        }
    }

    private void createRooms() {
        ArrayList<List> list = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < this.roomList.size(); i++) {
            if (count == 7) {
                list.add(new ArrayList<>(temp));
                temp.clear();
                count = 0;
            }
            if (count < 7) {
                   temp.add(this.roomList.get(i));
            }
            count++;
        }
        list.add(temp);

        this.readItems();
        ArrayList<List> list2 = new ArrayList<>();
        ArrayList<String> temp2 = new ArrayList<>();
        count = 0;
        for (int i = 0; i < itemsList.size(); i++) {
            if (count == 3) {
                list2.add(new ArrayList<>(temp2));
                temp2.clear();
                count = 0;
            }
            if (count < 3) {
                temp2.add(this.itemsList.get(i));
            }
            count++;
        }
        list2.add(temp2);

        Items item = null;

        for (int i = 0; i < list.size(); i++) {

            if (Integer.valueOf(list.get(i).get(6).toString()) != -2) {
                for (int y = 0; y < list2.size(); y++) {
                    if (Integer.valueOf(list.get(i).get(6).toString()) == Integer.valueOf(list2.get(y).get(0).toString())) {
                        item = new Items(Integer.valueOf(list2.get(y).get(0).toString()), list2.get(y).get(1).toString(), list2.get(y).get(2).toString());
                        this.items.add(item);
                    }
                }
            } else {
                item = null;
            }

            this.room.add(new Room(Integer.valueOf(list.get(i).get(0).toString()), Boolean.valueOf(list.get(i).get(1).toString()),
                    Integer.valueOf(list.get(i).get(2).toString()), Integer.valueOf(list.get(i).get(3).toString()),
                    Integer.valueOf(list.get(i).get(4).toString()), Integer.valueOf(list.get(i).get(5).toString()),
                    item));
        }
    }

    private void setDefaultRoom() {
        for (int i = 0; i < room.size(); i++) {
            if (this.room.get(i).isDefault()) {
                this.startId = this.room.get(i).getId();
            }
        }
    }

    private void createPlayer() {
        this.player = new Player(this.startId);
    }

    private void initActivity() {
        this.textViewTitle = findViewById(R.id.textViewGameTitle);
        this.buttonNorth = findViewById(R.id.buttonNorth);
        this.buttonWest = findViewById(R.id.buttonWest);
        this.buttonEast = findViewById(R.id.buttonEast);
        this.buttonSouth = findViewById(R.id.buttonSouth);
        this.buttonBack = findViewById(R.id.floatingActionButtonGameBack);
        this.buttonSave = findViewById(R.id.floatingActionButtonGameSave);
        this.buttonPickup = findViewById(R.id.buttonPickup);
        this.buttonInventory = findViewById(R.id.buttonInventory);

        this.sqlite = new DBLite(this);
    }

    private void setRoom() {

        this.textViewTitle.setText("Room #" + this.player.getPosition());

        this.buttonNorth.setEnabled((this.room.get(this.player.getPosition()).getNorth() == -1) ? false : true);
        this.buttonWest.setEnabled((this.room.get(this.player.getPosition()).getWest() == -1) ? false : true);
        this.buttonEast.setEnabled((this.room.get(this.player.getPosition()).getEast() == -1) ? false : true);
        this.buttonSouth.setEnabled((this.room.get(this.player.getPosition()).getSouth() == -1) ? false : true);

        for (int i = 0; i < this.player.getInventory().size(); i++) {
            if (this.room.get(this.player.getPosition()).getItems() != null) {
                if (this.player.getInventory().get(i).getId() == this.room.get(this.player.getPosition()).getItems().getId()) {
                    this.room.get(this.player.getPosition()).setItems(null);
                }
            }
        }

        this.buttonPickup.setEnabled((this.room.get(this.player.getPosition()).getItems()) != null ? true : false);

        ArrayList<String> emptyRoom = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.room_empty)));
        Random r = new Random();
        if (this.room.get(this.player.getPosition()).getItems() == null && this.room.get(this.player.getPosition()).getId() != this.demonLocation) Toast.makeText(this, emptyRoom.get(r.nextInt(emptyRoom.size())), Toast.LENGTH_SHORT).show();

        if (this.room.get(this.player.getPosition()).getItems() != null && this.room.get(this.player.getPosition()).getId() != this.demonLocation) Toast.makeText(this, R.string.item_found, Toast.LENGTH_SHORT).show();

        if (this.room.get(this.player.getPosition()).getId() == this.demonLocation) {
            if (!this.player.getInventory().isEmpty()) {
                if (this.player.getInventory().size() == 2) {
                    Toast.makeText(this, R.string.demon_killed, Toast.LENGTH_LONG).show();
                } else
                if (this.player.getInventory().size() == 1) {
                    if (this.player.getInventory().get(0).getName().equalsIgnoreCase("Shield")) {
                        Toast.makeText(this, R.string.demon_nosword, Toast.LENGTH_LONG).show();
                        this.endGame("DEATH");
                    }
                    if (this.player.getInventory().get(0).getName().equalsIgnoreCase("Sword")) {
                        Toast.makeText(this, R.string.demon_noshield, Toast.LENGTH_LONG).show();
                        this.endGame("DEATH");
                    }
                }
            } else {
                Toast.makeText(this, R.string.demon_noitems, Toast.LENGTH_LONG).show();
                this.endGame("DEATH");
            }
        }

    }

    private void changeRoom(String cardinal) {
        int current = this.player.getPosition();
        switch (cardinal) {
            case "N":
                this.player.setPosition(this.room.get(current).getNorth());
                break;
            case "W":
                this.player.setPosition(this.room.get(current).getWest());
                break;
            case "E":
                this.player.setPosition(this.room.get(current).getEast());
                break;
            case "S":
                this.player.setPosition(this.room.get(current).getSouth());
                break;
        }
        this.setRoom();
    }

    private void endGame(String condition) {
        if (condition.equalsIgnoreCase("WIN")) {
            new AlertDialog.Builder(this).setTitle(R.string.end_win)
                    .setCancelable(false)
                    .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }
        if (condition.equalsIgnoreCase("DEATH")) {
            new AlertDialog.Builder(this).setTitle(R.string.end_death)
                    .setCancelable(false)
                    .setNegativeButton(R.string.end_deny, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setPositiveButton(R.string.end_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getBaseContext(), LoadActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).show();
        }
    }
}
