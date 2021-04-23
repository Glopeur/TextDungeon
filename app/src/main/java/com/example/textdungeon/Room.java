package com.example.textdungeon;

public class Room {

    private int id, north, west, east, south;
    private boolean def;
    private Items items;

    public Room(int id, boolean def, int north, int west, int east, int south, Items items) {
        this.id = id;
        this.def = def;
        this.north = north;
        this.west = west;
        this.east = east;
        this.south = south;
        this.items = items;
    }

    public int getId() {
        return this.id;
    }

    public boolean isDefault() {
        return this.def;
    }

    public int getNorth() {
        return this.north;
    }

    public int getWest() {
        return this.west;
    }

    public int getEast() {
        return this.east;
    }

    public int getSouth() {
        return this.south;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public Items getItems() {
        return this.items;
    }
}
