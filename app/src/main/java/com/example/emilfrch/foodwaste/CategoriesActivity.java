package com.example.emilfrch.foodwaste;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class CategoriesActivity extends AppCompatActivity {

    String cat = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Food Categories");
    }

    public void categoryBakery(View v) {
        cat = "Bakery";
        openCategory(null);
    }

    public void categoryCorn(View v) {
        cat = "Corn";
        openCategory(null);
    }

    public void categoryDairy(View v) {
        cat = "Dairy";
        openCategory(null);
    }

    public void categoryDrinks(View v) {
        cat = "Drinks";
        openCategory(null);
    }

    public void categoryFruits(View v) {
        cat = "Fruit";
        openCategory(null);
    }

    public void categoryLeftovers(View v) {
        cat = "Leftovers";
        openCategory(null);
    }

    public void categoryMeat(View v) {
        cat = "Meat";
        openCategory(null);
    }

    public void categoryRice(View v) {
        cat = "Rice and Pasta";
        openCategory(null);
    }

    public void categorySeafood(View v) {
        cat = "Seafood";
        openCategory(null);
    }

    public void categorySweets(View v) {
        cat = "Sweets";
        openCategory(null);
    }

    public void categoryVegetables(View v) {
        cat = "Vegetables";
        openCategory(null);
    }

    public void categoryOther(View v) {
        cat = "Other";
        openCategory(null);
    }

    public void openCategory(View v) {
            Intent i = new Intent(CategoriesActivity.this, ItemsActivity.class);
            i.putExtra("chosenCategory", cat);
            startActivity(i);
    }

}