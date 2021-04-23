package com.example.textdungeon;

import java.util.ArrayList;

public class Player {

    private int position;
    private ArrayList<Items> items = new ArrayList<>();

    public Player(int position) {
        this.position = position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }

    public void setInventory(ArrayList<Items> items) {
        this.items = items;
    }

    public ArrayList<Items> getInventory() {
        return this.items;
    }
}
