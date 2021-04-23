package com.example.textdungeon;

import java.util.List;

public class Save {

    private int slot, position;
    private List<Items> inventory;

    public Save (int slot, int position, List<Items> inventory) {
        this.slot = slot;
        this.position = position;
        this.inventory = inventory;
    }

    public int getSlot() {
        return this.slot;
    }

    public int getPosition() {
        return this.position;
    }

    public List<Items> getInventory() {
        return this.inventory;
    }
}
