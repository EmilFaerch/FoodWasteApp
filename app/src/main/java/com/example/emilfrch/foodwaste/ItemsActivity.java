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

public class ItemsActivity extends AppCompatActivity {
    File fileItems;
    String category, item, weight, value;
    ListView listView;
    //ArrayAdapter<String> items;
    CustomAdapter<Item> items;
    LayoutInflater inflater;
    View row;

    boolean pickItem = false;
    String itemValue;

    String chosenCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        fileItems = new File(getExternalFilesDir(null)+"/items.txt");

        inflater = LayoutInflater.from(this);
        row = (LinearLayout) inflater.inflate(R.layout.row_item, listView, false);
       // items = new CustomAdapter(getApplicationContext(), 0);
        // items =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        listView = (ListView) findViewById(R.id.listItems);

        // Assign adapter to ListView
        listView.setAdapter(items);

        TextView txtName = (TextView) findViewById(R.id.txtName);
        TextView txtInfo = (TextView) findViewById(R.id.txtInfo);

        listView.addView(txtName);
        listView.addView(txtInfo);

        if (getIntent().getExtras().getString("chosenCategory") != null) {
            chosenCategory = (getIntent().getExtras().getString("chosenCategory"));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(chosenCategory + " products");

        readFile(null);

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

/*        public void onStart(){
            super.onStart();

            readFile();
        } */

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
            /*
            LayoutInflater inflater = LayoutInflater.from(this);
            View row = (LinearLayout) inflater.inflate(R.layout.row_item, listView, false);
            items = new CustomAdapter(getApplicationContext());

            listView = (ListView) findViewById(R.id.listItems);

            // Assign adapter to ListView
            listView.setAdapter(items);

            listView.addView(row);
            //inflater.inflate(R.layout.row_item, listView, true);
           // inflater.inflate(R.layout.row_item, listView); */



            // * Don't know wtf this means
            // Attaching BufferedReader to the FileInputStream by the help of InputStreamReader
            FileInputStream fis = new FileInputStream(fileItems);
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(fis));

            // Reading data line by line and storing it into the stringbuffer
            while ((category = inputReader.readLine()) != null) {
                item = inputReader.readLine();
                weight = inputReader.readLine();
                value = inputReader.readLine();
                int intWeight = Integer.parseInt(weight);
                int intValue = Integer.parseInt(value);

                if (category.equals(chosenCategory)) {
                    // items.add(new Item(category, item, weight, value));
                    Toast.makeText(this, "Found relevant stuff and didnt crash!", Toast.LENGTH_SHORT).show();
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
