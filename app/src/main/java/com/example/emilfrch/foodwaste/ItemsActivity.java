package com.example.emilfrch.foodwaste;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ItemsActivity extends AppCompatActivity {

    // Initializing all the variables we need up here (globally), in case we need them in another function/method (not a different activity)
    File fileItems; // Items database file
    String category, item, weight, value; // the strings for info - in this case is not really used other than going through the database
    ListView listView; // The ListView that lists the items
    ArrayAdapter<String> items; // The adapter that we put onto the listview to add items
    boolean pickItem = false; // Checking if we picked an existing item
    String itemValue; // Used to save the item name when logging an existing item
    String chosenCategory; // variable to keep track of what category we want to add to
    String week, day; // Checking week- and day number (e.g. week 18, day 2 (tuesday)) - while these are technically numbers, we read strings from the file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        fileItems = new File(getExternalFilesDir(null) + "/items.txt"); // For our actual app we would only want it here and not in MainMenu to create a default data base.

        // this is how we receive the transferred variables
        chosenCategory = (getIntent().getExtras().getString("chosenCategory")); // get the extra-string, from the intent we used to call this activity, that we called "chosenCategory" ...
        // ... and put it into an actual variable called chosenCategory - might as well keep it consistent

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        toolbar.setTitle(chosenCategory + " products"); // This will e.g display ("Fruit products") in the toolbar

        listView = (ListView) findViewById(R.id.listItems); // Initialize (or w/e) our ListView
        items = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1); // initialize our adapter to use a simple layout

        listView.setAdapter(items); // Apply the adapter to our listview

        // ListView Item Click Listener
        // a "Listener" checks for events, in this case it's an On Item Click-listener that checks of click-events.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) { // Clicking an item triggers this
                itemValue = (String) listView.getItemAtPosition(position); // we get and save the value of the item, of the position that we clicked. If we pick the 1st item it would save "Banana"
                pickItem = true; // We picked an existing item
                itemAdd(null); // Call the Log-activity - again, null-parameter because... we don't need a specific view.
            }
        });
    }

    @Override
    protected void onStart() { // Using the onStart to have it update the list when you add an item and go back to this activity
        super.onStart(); // This is required, because... that's how it is when you override stuff
        items.clear(); // Clear the adapter to avoid it adding duplicates
        readFile(null); // Read from the file + fill up the adapter with items again
    }

    // Reading from our database to fill out our listview
    public void readFile(View v){
        try { // always try, because Streams can cause a lot of stuff.

            // Attaching BufferedReader to the FileInputStream by the help of InputStreamReader - don't know wtf this means
            FileInputStream fis = new FileInputStream(fileItems); // Again, we wanna access this file
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(fis)); // But now we're using a BufferedReader to read from the file.

            // read a line and check if it's not empty; we continue to read from the code while it's not empty
            while ((category = inputReader.readLine()) != null) { // Because of the way we formatted the database, if there's 1 line, we know that there are at least 3 more to read from (4 for each item)
                item = inputReader.readLine(); // So if there's a category (e.g. "Fruit"), we also read the next 3 lines, even though we don't use them, other than basically skipping through the database
                weight = inputReader.readLine();
                value = inputReader.readLine();

                    // So, if the category from the file (e.g. "Fruit") matches the category that we've kept track of that we are in (e.g. "Fruit");
                    if (category.equals(chosenCategory)) { // check to see if the string is equal to the other string ( the "==" check doesn't work here for some reason, found that out the hard way)
                        items.add(item); // if the categories match, we want to add the item to the list
                        // NOTICE THAT WE DON'T ADD THE ITEM TO THE LISTVIEW, BUT RATHER THE ADAPTER - you add items to an adapter and add the adapter to the listview (BECAUSE WHY MAKE IT SIMPLE)
                    }
            } // if there are no more lines in our database
            inputReader.close(); // Close that shit up
            fis.close();
        }
        catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show(); // Catch and display any error
        }
    }

    // Accessing the Logging-activity
    // We're using the same method for opening the new activity, so we need to see ...
    public void itemAdd(View v) {
        if (!pickItem) itemValue = null; // if we didn't choose an existing item, we must have pressed the "+" button and we would not want the "Item" field filled out in the next screen

        // ... if we chose an existing item, then pass along all the values of that item, ready for easy logging
        Intent i = new Intent(ItemsActivity.this, LogActivity.class); //
        i.putExtra("chosenCategory", chosenCategory); // Pass the category along again so we know what to save the item as
        i.putExtra("clickedItem", itemValue); // and pass along the name of the chosen item as well (doesn't matter if it's null or not)
        i.putExtra("clickedWeight", weight);
        i.putExtra("clickedValue", value);
        startActivity(i);
        pickItem = false; // reset pickItem
    }

}
