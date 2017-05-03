package com.example.emilfrch.foodwaste;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.Output;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    java.util.Calendar cal = java.util.Calendar.getInstance(); // For whatever the fuck reason: it wants to add "java.util." - I don't know why, this is the only place it wants to add it
    int viewWeek = cal.get(java.util.Calendar.WEEK_OF_YEAR); // viewWeek is the week we would like to see (By default we wanna see the current week)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        // Setting the path of the file (this wouldn't be here in the actual app, as we don't want a default database)
        File fileItems = new File(getExternalFilesDir(null)+ "/items.txt"); // we get the external files directory of the phone and create a text file called "items" in there.
        File fileData = new File(getExternalFilesDir(null) + "/data.txt");  // and the same for the waste database.

        // Quickly clearing databases for testing purposes only
        // fileItems.delete();
        // fileData.delete();
            /*
            // for hard-coding databases -----------------------------
           if (fileItems.exists() == false){ // if there's no default database ...
                try { // ... we try and create one
                    FileOutputStream fos = new FileOutputStream(fileItems, true); // Need to read up on this part. Basically we say we want access to this File (true means it adds text instead of overwriting the file)
                    OutputStreamWriter writer = new OutputStreamWriter(fos); // Here we make a "typewriter" I suppose, that we can use to add text to our opened File.
                    // The database is not formatted as a "comma-seperator", but simply a line seperator. Each line consists of data, formatted this way:
                    writer.write("Fruit\n");    // category
                    writer.write("Apple\n");    // item
                    writer.write("50\n");       // weight (gram)
                    writer.write("3\n");        // value/price
                    writer.write("Fruit\n");    // new item - same format - keep in mind this is only the "items"-database ...
                    writer.write("Orange\n");   // ... so we're only saving and reading item-related stuff (waste data like 'reason' goes in "data.txt" (fileData))
                    writer.write("75\n");
                    writer.write("4\n");
                    writer.write("Fruit\n");
                    writer.write("Banana\n");
                    writer.write("50\n");
                    writer.write("20\n");
                    writer.write("Meat\n");
                    writer.write("Minced Prok\n");
                    writer.write("500\n");
                    writer.write("40\n");
                    writer.write("Bakery\n");
                    writer.write("Ryebread\n");
                    writer.write("1000\n");
                    writer.write("10\n");
                    writer.close();
                    fos.close(); // Always remember to close up Streams
                    Toast.makeText(this, "Created default basic database", Toast.LENGTH_SHORT).show();
                } catch (Exception e) { // If something went wrong
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show(); // Display the specific error
                }
           } else Toast.makeText(this, "Found items.txt", Toast.LENGTH_SHORT).show(); // Let us know if the file exists

        if (fileData.exists() == false) {
            try {
                FileOutputStream fos = new FileOutputStream(fileData, true);
                OutputStreamWriter writer = new OutputStreamWriter(fos);
                writer.write(viewWeek + "\n"); // week number
                writer.write("1\n");                         // day number
                writer.write("Fruit\n");                    // category
                writer.write("Apple\n");                    // item
                writer.write("50\n");                       // weight (gram)
                writer.write("10\n");                        // value/price
                writer.write("50\n");                         // percent
                writer.write("Gone Bad\n");                         // reason
                writer.write(" \n");                         // comment
                writer.write(viewWeek + "\n"); // week number
                writer.write("2\n");                         // day number
                writer.write("Fruit\n");                    // category
                writer.write("Insane Apple\n");                    // item
                writer.write("50\n");                       // weight (gram)
                writer.write("10\n");                        // value/price
                writer.write("50\n");                         // percent
                writer.write("Gone Bad\n");                         // reason
                writer.write(" \n");                         // comment
                writer.write(viewWeek + "\n"); // week number
                writer.write("3\n");                         // day number
                writer.write("Fruit\n");                    // category
                writer.write("Apple\n");                    // item
                writer.write("50\n");                       // weight (gram)
                writer.write("10\n");                        // value/price
                writer.write("50\n");                         // percent
                writer.write("Gone Bad\n");                         // reason
                writer.write(" \n");                         // comment
                writer.write(viewWeek + "\n"); // week number
                writer.write("2\n");                         // day number
                writer.write("Fruit\n");                    // category
                writer.write("Apple\n");                    // item
                writer.write("50\n");                       // weight (gram)
                writer.write("3\n");                        // value/price
                writer.write("50\n");                         // percent
                writer.write("Gone Bad\n");                         // reason
                writer.write(" \n");                         // comment
                writer.write(viewWeek + "\n"); // week number
                writer.write("5\n");                         // day number
                writer.write("Fruit\n");                    // category
                writer.write("Apple\n");                    // item
                writer.write("50\n");                       // weight (gram)
                writer.write("20\n");                        // value/price
                writer.write("50\n");                         // percent
                writer.write("Gone Bad\n");                         // reason
                writer.write(" \n");                         // comment
                writer.write(viewWeek + "\n"); // week number
                writer.write("6\n");                         // day number
                writer.write("Fruit\n");                    // category
                writer.write("Apple\n");                    // item
                writer.write("50\n");                       // weight (gram)
                writer.write("10\n");                        // value/price
                writer.write("50\n");                         // percent
                writer.write("Gone Bad\n");                         // reason
                writer.write(" \n");                         // comment
                writer.write(viewWeek + "\n"); // week number
                writer.write("7\n");                         // day number
                writer.write("Fruit\n");                    // category
                writer.write("Apple\n");                    // item
                writer.write("50\n");                       // weight (gram)
                writer.write("10\n");                        // value/price
                writer.write("50\n");                         // percent
                writer.write("Gone Bad\n");                         // reason
                writer.write(" \n");                         // comment
                writer.close();
                fos.close();
            } catch (Exception e) {
                Toast.makeText(this, "Encountered an error writing to file!\nEntry has not been logged.", Toast.LENGTH_SHORT).show(); // Let them know there was an error
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show(); // Display that error, mostly for support purposes.
            }
        }
       */
    }

    public void showCategories(View v) { // the Log Waste button to open the Categories-activity
        Intent i = new Intent(MainMenu.this, CategoriesActivity.class); // We want to go from MainMenu to CategoriesActivities
        startActivity(i);
    }

    public void showData(View v) { // You guessed it
        Intent i = new Intent(MainMenu.this, DataActivity.class);
        startActivity(i);
    }
}