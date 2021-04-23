package com.example.textdungeon;

import java.io.Serializable;

public class Items implements Serializable {

    private String name, description;
    private int id;

    public Items(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}
