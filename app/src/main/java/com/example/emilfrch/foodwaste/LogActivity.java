package com.example.emilfrch.foodwaste;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Locale;

import static java.lang.Integer.valueOf;

public class LogActivity extends AppCompatActivity {

    File fileItems, fileData; // Here we add the actual data-database to add the log entries
    String chosenCategory, clickedItem, clickedWeight, clickedValue; // variables we sent from the previous activity
    boolean newItem = true; // start our assuming it's a new item (that we clicked "+")

    Calendar cal = Calendar.getInstance(Locale.getDefault()); // Need to getInstance, or else else it will return Week as "3" for some reason
    int viewDay = cal.get(Calendar.DAY_OF_WEEK); // viewWeek is the week we would like to see (By default we wanna see the current week)
    int viewWeek = cal.get(Calendar.WEEK_OF_YEAR);

    EditText textItem, textWeight, textValue, textReflection; SeekBar textPercent; Spinner textReason;

    String weight, item, value, percent, reason, reflection, log;
    long wasteMoney, wasteWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

         textWeight = (EditText) findViewById(R.id.boxWeight);
         textItem = (EditText) findViewById(R.id.boxItem);
         textValue = (EditText) findViewById(R.id.boxValue);
         textPercent = (SeekBar) findViewById(R.id.seekWaste);
         textReason = (Spinner) findViewById(R.id.spinReason);
         textReflection = (EditText) findViewById(R.id.boxReflection);

        // THIS IS IMPORTANT - used for logging to the right day
        viewDay--; // Day 1 is Sunday ( I found out -_-' ), we obviously want Monday to be 1, so we subtract 1
        if (viewDay == 0) viewDay = 7; // but when it's actually Sunday it'll be 1 - 1 = 0; so we set it to 7 (our Sunday)

        // Initialize both databases
        fileItems = new File(getExternalFilesDir(null) + "/items.txt");
        fileData = new File(getExternalFilesDir(null) + "/data.txt");

        // get chosen category
        chosenCategory = getIntent().getExtras().getString("chosenCategory");
        toolbar.setTitle("Submitting new " + chosenCategory + " item"); // Submitting new Fruit item

        if (getIntent().getExtras().getString("clickedItem") != null) { // If the intent is not null, we've clicked an existing item from the list
            clickedItem = (getIntent().getExtras().getString("clickedItem")); // get the name of the item we clicked, that we passed along from the last activity
            clickedWeight = (getIntent().getExtras().getString("clickedWeight")); // get the weight
            clickedValue = (getIntent().getExtras().getString("clickedValue")); // get value as well
            toolbar.setTitle("Logging " + clickedItem); // Change the title, as we are not submitting a new item, we are logging an existing item
            textItem.setText(clickedItem); // Put in the name of the clicked item
            textWeight.setText(clickedWeight); // and also the weight
            textValue.setText(clickedValue); // aaand also the price

            newItem = false; // not submitting a new item
        }

        // REASONS
        Spinner reasonSpinner = (Spinner) findViewById(R.id.spinReason); // A Spinner is a fucking Dropdown menu because ... logic?

        // Like the listview, we use an adapter to populate the dropdown
        ArrayAdapter<CharSequence> adapterReason = ArrayAdapter.createFromResource(this, R.array.waste_reasons, android.R.layout.simple_spinner_item);
        // R.array.waste_reasons is a String array defined in "res/values/strings.xml" that holds the reasons ^

        adapterReason.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        reasonSpinner.setAdapter(adapterReason); // Apply the adapter to the dropdo... spinner, sorry.

        // Dynamically update Waste Percentage Text
        final TextView percent = (TextView) findViewById(R.id.txtPercent);
        SeekBar waste = (SeekBar) findViewById(R.id.seekWaste); // SeekBar is a Slider .... these fucking names.
        waste.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // Another listener, this time checking for when something is changed

            // Has to be here ...
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            // eventhough we are only ...
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            // ... interested in this!
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { // When something changes we want to update the text
                percent.setText(progress + "%");
            }
        });
    }

    // Here comes the fun part of saving data
    public void saveData(View view) {
        if (weight == "" || item == "" || value == "") {
            Toast.makeText(this, "You didn't fill out all of the fields!", Toast.LENGTH_SHORT).show();
        } else {
        try {
            // Basically initializing the boxes and saving the values of them
             weight = String.valueOf(textWeight.getText());

             item = String.valueOf(textItem.getText());

             value = String.valueOf(textValue.getText());

             percent = String.valueOf(textPercent.getProgress());

             reason = String.valueOf(textReason.getSelectedItem());

             reflection = String.valueOf(textReflection.getText()); if (reflection == null) reflection = " ";

            // We use a "long" for whatever reason (it doesn't crash, yay) to round the "wasted money" up to a whole number, based on the percent wasted.
             wasteMoney = Math.round(Double.parseDouble(value) * (Double.parseDouble(percent) / 100));

            // Calculate the wasted weight based on percent
             wasteWeight = Math.round(Double.parseDouble(weight) * (Double.parseDouble(percent) / 100));

             log = MessageFormat.format("You threw out {0}% of {1} ({2} gram)\nWasted {3} kr", percent, item, wasteWeight, wasteMoney); // Success message
        }
        catch(Exception e){
            Toast.makeText(this, "Something went wrong!\nSee if you filled out the fields correctly!", Toast.LENGTH_LONG).show();
            return;
        }

        try { // try and access the data database
            // This is what we'll be using for displaying data - it's time to figure out how the fuck we do that...
            FileOutputStream fos = new FileOutputStream(fileData, true); // true for adding, false for overwriting
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write(viewWeek + "\n"); // adding the week number first
            writer.write(viewDay + "\n"); // Day of week (1 for monday and so on) - adding "\n" to create a new line ...
            writer.write(chosenCategory + "\n"); // ... otherwise it would write e.g: 192FruitApple20350Too OldVacation, which looks fucking horrible and useless for our formatting.
            writer.write(item + "\n");           // follows the same format of the items list, then because this is the waste data we include percent, reason, and reflection
            writer.write(wasteWeight + "\n");
            writer.write(wasteMoney + "\n");
            writer.write(percent + "\n");
            writer.write(reason + "\n");
            writer.write(reflection + "\n");
            writer.close();
            fos.close();
            Toast.makeText(this, log, Toast.LENGTH_SHORT).show(); // Display success message when we're done saving the item
        } catch (Exception e) {
            Toast.makeText(this, "Encountered an error writing to file!\nEntry has not been logged.", Toast.LENGTH_SHORT).show(); // Let them know there was an error
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show(); // Display that error, mostly for support purposes.
        }

        // So, we've logged the data, but if it's a new item, we also want to add it to the items database
        if (newItem) { // if we clicked the "+" button
            // Make some temporary variables that we use to compare our actual variables
            String compCategory = chosenCategory;
            String compItem = item;
            String compWeight = weight;
            String compValue = value;

            if (fileItems.exists()) {
                try { // Check to see if the item already exists in the items database by reading through the "items"
                    FileInputStream fis = new FileInputStream(fileItems);
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(fis));

                    // Again, reading the file 4 lines at a time as previously
                    while ((compCategory = inputReader.readLine()) != null) {
                        compItem = inputReader.readLine();
                        compWeight = inputReader.readLine();
                        compValue = inputReader.readLine();

                        // I wanted to display weight and value for each item, so you could have "Rye bread 500g 8 kroner" and "Rye bread 1000g 15 kroner", instead of having 1 rye bread for all,
                        // but RelativeLayout was a bitch, so that's not happening. You can actually have different Rye Bread, but you wont be able to see it until you choose it.
                        if (chosenCategory.equals(compCategory) && item.equals(compItem) && weight.equals(compWeight) && value.equals(compValue)) { // Checking for identical item
                            Toast.makeText(this, item + " == " + compItem, Toast.LENGTH_SHORT).show(); //
                            inputReader.close();
                            fis.close();
                            newItem = false; // WE FOUND A MATCH SO IT'S NOT A NEW ITEM == DONT ADD TO fileItems
                            Toast.makeText(getCurrentFocus().getContext(), "This item has already been submitted.\nPlease select it on the list in the future :)", Toast.LENGTH_LONG).show();
                            return; // If we find a match we stop the while-loop
                        }
                    }
                    inputReader.close();
                    fis.close();
                } catch (Exception e) {
                    Toast.makeText(this, "Encountered an error reading from file!\nEntry has not been logged.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

                if (newItem) { // if there was no match in the items database, we're gonna add the item
                    try {
                        FileOutputStream fos = new FileOutputStream(fileItems, true);
                        OutputStreamWriter writer = new OutputStreamWriter(fos);
                        writer.write(chosenCategory + "\n");
                        writer.write(item + "\n");
                        writer.write(weight + "\n");
                        writer.write(value + "\n");
                        writer.close();
                        fos.close();
                        Toast.makeText(this, "New item '" + item + "' added!", Toast.LENGTH_SHORT).show();
                        newItem = false;
                    } catch (Exception e) {
                        Toast.makeText(this, "Encountered an error writing to file!\nItem has not been saved.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}