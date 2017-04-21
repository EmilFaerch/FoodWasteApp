package com.example.emilfrch.foodwaste;

import android.widget.ArrayAdapter;

/**
 * Created by Emil Færch on 13-04-2017.
 */

public class Item {
    String category, name;
    String weight, value;

    public Item(String category, String name, String weight, String value ) {
        this.category = category;
        this.name = name;
        this.weight = weight;
        this.value = value;

    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getWeight() { return weight; }

    public String getValue() {
        return value;
    }

}
