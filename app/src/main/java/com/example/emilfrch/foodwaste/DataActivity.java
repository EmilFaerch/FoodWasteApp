package com.example.emilfrch.foodwaste;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DataActivity extends AppCompatActivity {

    File fileData;
    String week, day, category, item, weight, value, percent, reason, comment; // Variables that will temporarily hold the info that we are reading from "data.txt"
    int d1Money, d1Weight, d2Money, d2Weight, d3Money, d3Weight, d4Money, d4Weight, d5Money, d5Weight, d6Money, d6Weight, d7Money, d7Weight = 0; // Dont worry, not complex - just annoying ...
    // ... could probably have been done in a different way, but couldn't really apply it to the different barcharts then - wouldn't save me any time to do it other way anyway

    Calendar cal = Calendar.getInstance();          // Need to getInstance, or else else it will return Week as "3" for some reason
    int viewWeek = cal.get(Calendar.WEEK_OF_YEAR);  // viewWeek is the week we would like to see (By default we wanna see the current week)
    
    TextView weekNumber; // Text header

    // Color codes for the BarCharts
    String col1 = "#4cff00";
    String col2 = "#00ff8c";
    String col3 = "#00ffbb";
    String col4 = "#00ffff";
    String col5 = "#00d4ff";
    String col6 = "#00aeff";
    String col7 = "#0084ff";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Your Data");

        weekNumber = (TextView) findViewById(R.id.txtWeekNumber); // Text Header

        fileData = new File(getExternalFilesDir(null) + "/data.txt");

        readData(null);
    }

    // The reason why this is a method is because we would call this whenever we clicked a "Next" or "Previous Week" button - we would go through the database again ...
    public void readData(View view){
        // ... which is why we set the Text Header here, so we can ...
        weekNumber.setText("Week #" + String.valueOf(viewWeek)); // ... update the textview in case we clicked to see another week ^

        if (fileData.exists() == false) {
            Toast.makeText(this, "Nothing to display yet", Toast.LENGTH_SHORT).show();
        }
        else {
            try { // always try, because Streams can cause trouble.

                // Attaching BufferedReader to the FileInputStream by the help of InputStreamReader - don't know wtf this means
                FileInputStream fis = new FileInputStream(fileData); // Again, we wanna access this file
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(fis)); // Using a BufferedReader to read from the file.

                // read a line and check if it's not empty; we continue to read from the code while it's not empty
                while ((week = inputReader.readLine()) != null) { // Because of the way we formatted the database, if there's 1 line, we know that there are at least 5 more to read from (6 for each item)
                    day = inputReader.readLine();
                    category = inputReader.readLine();
                    item = inputReader.readLine();
                    weight = inputReader.readLine();
                    value = inputReader.readLine();
                    percent = inputReader.readLine();
                    reason = inputReader.readLine();
                    comment = inputReader.readLine();

                    // Okay, so we know 6 things; now we want to see which day of the week it was thrown out so we can add it to that day
                    if (week.equals(String.valueOf(viewWeek))) { // Check to see if the week is the week we want (right now current - might be updated (if it doesn't say WEEK_OF_YEAR I forgot to edit comment))
                        switch (day) {  // Check the value of day
                            case "1":   // if it's 1 (monday) we add it to our corresponding variables
                                d1Weight += Integer.parseInt(weight);
                                d1Money += Integer.parseInt(value);
                                break; // important to 'break:' the cases, it will just continue running the code until it finds one. Could potentially run all 8 cases if only 'default' has 'break'
                            case "2":   // and so on ...
                                d2Weight += Integer.parseInt(weight);
                                d2Money += Integer.parseInt(value);
                                break;
                            case "3":
                                d3Weight += Integer.parseInt(weight);
                                d3Money += Integer.parseInt(value);
                                break;
                            case "4":
                                d4Weight += Integer.parseInt(weight);
                                d4Money += Integer.parseInt(value);
                                break;
                            case "5":
                                d5Weight += Integer.parseInt(weight);
                                d5Money += Integer.parseInt(value);
                                break;
                            case "6":
                                d6Weight += Integer.parseInt(weight);
                                d6Money += Integer.parseInt(value);
                                break;
                            case "7":
                                d7Weight += Integer.parseInt(weight);
                                d7Money += Integer.parseInt(value);
                                break;

                            // the 'else' of "switch case"-statements
                            default: // if it's not any of the 'cases' it goes to the 'default case' (it really shouldn't in this case, so we dont have any code here)
                                break;
                        } // close the switch
                    }
                } // ... there are no more lines in our database, then ...
                inputReader.close(); // ... close that shit up ...
                fis.close(); // ... entirely
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show(); // Catch and display any error
            }

            // Now for the BarCharts/BarGraphs!

            // This is for the money bar chart
            ArrayList<Bar> money = new ArrayList<Bar>(); // Make an ArrayList aka an Adapter (just like the "items" one we use to add items to our ListView)
            // Create 7 bars, one for each day - pretty self explanatory (functions are from a custom library "HoloGraphLibrary" that I added to the project)
            Bar d1 = new Bar();
            d1.setColor(Color.parseColor(col1));
            d1.setName("Monday");
            d1.setValue(d1Money); // So this what we use all the variables for - could have made an array of weight[] and money[], but we would still have to ...
            // create all these bars manually, so doesn't really matter (to me anyway ... screw optimizing! :D )

            Bar d2 = new Bar();
            d2.setColor(Color.parseColor(col2));
            d2.setName("Tuesday");
            d2.setValue(d2Money);

            Bar d3 = new Bar();
            d3.setColor(Color.parseColor(col3));
            d3.setName("Wednesday");
            d3.setValue(d3Money);

            Bar d4 = new Bar();
            d4.setColor(Color.parseColor(col4));
            d4.setName("Thursday");
            d4.setValue(d4Money);

            Bar d5 = new Bar();
            d5.setColor(Color.parseColor(col5));
            d5.setName("Friday");
            d5.setValue(d5Money);

            Bar d6 = new Bar();
            d6.setColor(Color.parseColor(col6));
            d6.setName("Saturday");
            d6.setValue(d6Money);

            Bar d7 = new Bar();
            d7.setColor(Color.parseColor(col7));
            d7.setName("Sunday");
            d7.setValue(d7Money);

            // Add all the bar items to the Adapter
            money.add(d1); money.add(d2);
            money.add(d3); money.add(d4); money.add(d5);
            money.add(d6); money.add(d7);

            // Instantiate the chart for money
            BarGraph graphMoney = (BarGraph) findViewById(R.id.barMoney);
            graphMoney.setBars(money);      // Apply the adapter to the BarGraph
            graphMoney.setUnit("kr");       // Prefix of the value
            graphMoney.appendUnit(true);    // true/false - doesn't seem to do much difference

            // aand same procedure for the weight barchart - nothing new beyond this point
            ArrayList<Bar> weight = new ArrayList<Bar>(); // Make an ArrayList aka an Adapter (just like the "items" one we use to add items to our ListView)
            // Create 7 bars, one for each day
            Bar w1 = new Bar();
            w1.setColor(Color.parseColor(col1));
            w1.setName("Monday");
            w1.setValue(d1Weight);

            Bar w2 = new Bar();
            w2.setColor(Color.parseColor(col2));
            w2.setName("Tuesday");
            w2.setValue(d2Weight);

            Bar w3 = new Bar();
            w3.setColor(Color.parseColor(col3));
            w3.setName("Wednesday");
            w3.setValue(d3Weight);

            Bar w4 = new Bar();
            w4.setColor(Color.parseColor(col4));
            w4.setName("Thursday");
            w4.setValue(d4Weight);

            Bar w5 = new Bar();
            w5.setColor(Color.parseColor(col5));
            w5.setName("Friday");
            w5.setValue(d5Weight);

            Bar w6 = new Bar();
            w6.setColor(Color.parseColor(col6));
            w6.setName("Saturday");
            w6.setValue(d6Weight);

            Bar w7 = new Bar();
            w7.setColor(Color.parseColor(col7));
            w7.setName("Sunday");
            w7.setValue(d7Weight);

            weight.add(w1); weight.add(w2);
            weight.add(w3); weight.add(w4); weight.add(w5);
            weight.add(w6); weight.add(w7);

            BarGraph graphWeight = (BarGraph) findViewById(R.id.barWeight);
            graphWeight.setBars(weight);  // Apply the adapter to the BarGraph
            graphWeight.setUnit("g");   // prefix of the value
            graphWeight.appendUnit(true);
        }
    }
}
