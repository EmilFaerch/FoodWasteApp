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

import static java.lang.Integer.valueOf;

public class LogActivity extends AppCompatActivity {

    File fileItems, fileData;
    String loggedItem = "", chosenCategory, clickedItem;
    boolean newItem = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        fileItems = new File(getExternalFilesDir(null)+"/items.txt");
        fileData = new File(getExternalFilesDir(null)+"/data.txt");
        chosenCategory = getIntent().getExtras().getString("chosenCategory");

        toolbar.setTitle("Submitting new " + chosenCategory + " item");

        if (getIntent().getExtras().getString("clickedItem") != null) {
            // Set Item based on What you clicked in Fruits-activity
            EditText itemName = (EditText) findViewById(R.id.boxItem);
            clickedItem = (getIntent().getExtras().getString("clickedItem"));
            toolbar.setTitle("Logging " + clickedItem);
            itemName.setText(clickedItem);
            newItem = false;
         }

        // REASONS
        Spinner reasonSpinner = (Spinner) findViewById(R.id.spinReason);

        ArrayAdapter<CharSequence> adapterReason = ArrayAdapter.createFromResource(this, R.array.waste_reasons, android.R.layout.simple_spinner_item);

        adapterReason.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

         reasonSpinner.setAdapter(adapterReason);

        // Waste Percentage Text
        final TextView percent = (TextView) findViewById(R.id.txtPercent);
        SeekBar waste = (SeekBar) findViewById(R.id.seekWaste);
        waste.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                percent.setText(progress + "%");
            }
        });
    }

    public void newItem(String category, String item, String weight, String value){ // We're adding a new item
        String mCategory = category;
        String mItem = item;
        String mWeight = weight;
        String mValue = value;
        String intValue = mValue;

        try { // Check to see if the item already exists in the "database" by reading the "items"
            FileInputStream fis = new FileInputStream(fileItems);
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(fis));

            // Reading data line by line and storing it into the stringbuffer
            while ((category = inputReader.readLine()) != null) {
                loggedItem = inputReader.readLine();
                value = inputReader.readLine();

                if (mCategory.equals(category) && mItem.equals(loggedItem) && mValue.equals(value)) {
                        inputReader.close();
                        fis.close();
                        return; //
                    } else {
                    Toast.makeText(getCurrentFocus().getContext(), "This item has already been submitted.\nPlease select it on the list: '" + chosenCategory + " products'", Toast.LENGTH_SHORT).show();
                    return; // Stop the function if we find a matching item - we don't want to add it again.
                    }
            }
            inputReader.close();
            fis.close();
        } catch (Exception e) {
            Toast.makeText(this, "Encountered an error reading from file!\nEntry has not been logged.", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        try {
            FileOutputStream fos = new FileOutputStream(fileItems, true);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write(category + "\n");
            writer.write(item + "\n");
            writer.write(weight + "\n");
            writer.write(intValue + "\n");
            writer.close();
            fos.close();
        } catch (Exception e) {
            Toast.makeText(this, "Encountered an error writing to file!\nItem has not been saved.", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void saveData(View view) {
        EditText textWeight = (EditText) findViewById(R.id.boxWeight) ;
        String weight = String.valueOf(textWeight.getText());

        EditText textItem = (EditText) findViewById(R.id.boxItem);
        String item = String.valueOf(textItem.getText());

        EditText textValue = (EditText) findViewById(R.id.boxValue);
        String value = String.valueOf(textValue.getText());
        int intValue = Integer.parseInt(value);

        SeekBar textPercent = (SeekBar) findViewById(R.id.seekWaste);
        String percent = String.valueOf(textPercent.getProgress());

        Spinner textReason = (Spinner) findViewById(R.id.spinReason);
        String reason = String.valueOf(textReason.getSelectedItem());

        EditText textReflection = (EditText) findViewById(R.id.boxReflection);
        String reflection = String.valueOf(textReflection.getText());

        long waste = Math.round(Double.parseDouble(value) * (Double.parseDouble(percent) / 100));

        String log = MessageFormat.format("You threw out {0}% of {1} ({2})\nWasted {3} kr", percent, item, weight, waste);

            try {
                FileOutputStream fos = new FileOutputStream(fileData, true);
                OutputStreamWriter writer = new OutputStreamWriter(fos);
                writer.write(chosenCategory + "\n");
                writer.write(item + "\n");
                writer.write(weight + "\n");
                writer.write(intValue + "\n");
                writer.write(percent + "\n");
                writer.write(reason + "\n");
                writer.write(reflection + "\n");
                writer.close();
                fos.close();
                Toast.makeText(this, log, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Encountered an error writing to file!\nEntry has not been logged.", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }

        if (newItem){
            newItem(chosenCategory, item, weight, value);
        }
    }
}