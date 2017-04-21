package com.example.emilfrch.foodwaste;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ItemsActivity extends AppCompatActivity {
    File fileItems;
    String category, item, weight, value;
    ListView listView;
    ArrayAdapter<String> items;

    TextView tName, tInfo;

    LinearLayout row;

    int position = 0;

    ArrayAdapter adapter;

    boolean pickItem = false;
    String itemValue;

    String chosenCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        fileItems = new File(getExternalFilesDir(null) + "/items.txt");

        listView = (ListView) findViewById(R.id.listItems);

        items = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        if (getIntent().getExtras().getString("chosenCategory") != null) {
            chosenCategory = (getIntent().getExtras().getString("chosenCategory"));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(chosenCategory + " products");

        listView.setAdapter(items);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                itemValue = (String) listView.getItemAtPosition(position);
                pickItem = true;
                itemAdd(null);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        readFile(null);
    }

    public void itemAdd(View v) {
        if (!pickItem) itemValue = null;
        Intent i = new Intent(ItemsActivity.this, LogActivity.class);
        i.putExtra("chosenCategory", chosenCategory);
        i.putExtra("clickedItem", itemValue);
        startActivity(i);
        pickItem = false;
    }

    private void readFile(View v){
        try {
            // * Don't know wtf this means
            // Attaching BufferedReader to the FileInputStream by the help of InputStreamReader
            FileInputStream fis = new FileInputStream(fileItems);
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(fis));

            // Reading data line by line and storing it into the stringbuffer
            while ((category = inputReader.readLine()) != null) {
                item = inputReader.readLine();
                weight = inputReader.readLine();
                value = inputReader.readLine();

                    if (category.equals(chosenCategory)) {
                        items.add(item);
                        Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
                    }
            }
            inputReader.close();
            fis.close();
        }
        catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
