package com.example.emilfrch.foodwaste;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    File fileItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Food Waste v. 0.7 logging (20-4)");

        fileItems = new File(getExternalFilesDir(null)+"/items.txt");

           if (fileItems.exists() == false){
                try {
                    FileOutputStream fos = new FileOutputStream(fileItems, true);
                    OutputStreamWriter writer = new OutputStreamWriter(fos);
                    writer.write("Fruit\n");
                    writer.write("Apple\n");
                    writer.write("50\n");
                    writer.write("3\n");
                    writer.write("Fruit\n");
                    writer.write("Orange\n");
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
                    fos.close();
                    fos.flush();
                    Toast.makeText(this, fileItems.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }
           } else Toast.makeText(this, "Found items.txt", Toast.LENGTH_SHORT).show();
    }

    public void showCategories(View v) {
        Intent i = new Intent(MainMenu.this, CategoriesActivity.class);
        startActivity(i);
    }
}