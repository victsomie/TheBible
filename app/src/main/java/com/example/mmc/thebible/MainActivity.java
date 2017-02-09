package com.example.mmc.thebible;

/*
* This is a bible project
*  Author: Victor M.
*
*  Project started 8th Feb 2017
*
*  Contributors:
*  Victor M.
*
* */

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";

    // Main Map to hold jsonised
    //  <Testament, <Book, <chapter, verses>>>
    Map<String, Map<String, Map<Integer, Map<Integer, String>>>> wholeBible;


    // Reference views
    Spinner spinnerTestaments;
    Spinner spinnerBooks;
    Spinner spinnerChapters;

    TextView txtHello;
    TextView txtDisplay;

    // arrays of items
    // === bible books ==
    Set<String> bibleBooksSet;
    String[] bibleBooksArr;

    // === bible book Verses====
    Set<Integer> bibleVersesSet; // remember our verses are integers
    Integer[] bibleVersesArr; // But here we need the array of verses as strings


    // A map of the verses in a specific chapter
    Map<Integer, String> chapterVerses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        method(this);

        Log.e(TAG, "TRY SEE IF YOU CAN ACCESS >> " + wholeBible.keySet() + " Type is " + wholeBible.keySet().getClass().isArray());

        spinnerTestaments = (Spinner) findViewById(R.id.spinnerTestaments);
        spinnerBooks = (Spinner) findViewById(R.id.spinnerBooks);
        spinnerChapters = (Spinner) findViewById(R.id.spinnerVerses);

        txtHello = (TextView) findViewById(R.id.txtHello);
        txtDisplay = (TextView) findViewById(R.id.txtReadVerses);


        //bibleBooksSet = wholeBible.keySet();

        Set<String> testamentsSet = wholeBible.keySet(); // Gives a set of both testaments
        String[] bibleTestamentsArr = testamentsSet.toArray(new String[testamentsSet.size()]); //Convert the Java Set of testaments to an array

        // Setting the tetstaments spinner items
        ArrayAdapter<String> arrayAdapterTestaments = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_layout, bibleTestamentsArr);
        //arrayAdapterTestaments.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerTestaments.setAdapter(arrayAdapterTestaments);


        spinnerTestaments.setEnabled(true);
        spinnerTestaments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String testamentSelected = spinnerTestaments.getSelectedItem().toString();
                Toast.makeText(MainActivity.this, "You selected >> " + testamentSelected, Toast.LENGTH_SHORT).show();

                Log.e(TAG, "OT books >> " + wholeBible.get("OT").keySet());
                Log.e(TAG, "OT books >> " + wholeBible.get(testamentSelected).keySet());
                bibleBooksSet = wholeBible.get(testamentSelected).keySet(); // Gives a set of OT books

                if (testamentSelected == "OT") {
                    bibleBooksSet = wholeBible.get("OT").keySet(); // Gives a set of OT books
                }
                if (testamentSelected == "NT") {
                    bibleBooksSet = wholeBible.get("NT").keySet(); // Gives a set of NT books
                }

                bibleBooksArr = bibleBooksSet.toArray(new String[bibleBooksSet.size()]); //Convert the Java Set of books to an array
                ArrayAdapter<String> arrayAdapterBooks = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_layout, bibleBooksArr);
                //arrayAdapterTestaments.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinnerBooks.setAdapter(arrayAdapterBooks);
                spinnerBooks.setEnabled(true);
                spinnerBooks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        // Get the book selected here
                        final String bookSelected = spinnerBooks.getSelectedItem().toString();
                        bibleVersesSet = wholeBible.get(testamentSelected).get(bookSelected).keySet();


                        // =======   Setting the verses spinner items ===============
                        //bibleVersesArr = bibleVersesSet.toArray(new String[bibleVersesSet.size()]); //Convert the Java Set of Verse to an array

                        //bibleVersesArr = new String[0];
                        //noinspection SuspiciousToArrayCall
                        bibleVersesArr = bibleVersesSet.toArray(new Integer[bibleVersesSet.size()]);
                        String[] a= Arrays.toString(bibleVersesArr).split("[\\[\\]]")[1].split(", ");

                        ArrayAdapter<String> arrayAdapterChapters = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_layout, a);
                        //arrayAdapterTestaments.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinnerChapters.setAdapter(arrayAdapterChapters);
                        spinnerChapters.setEnabled(true);

                        // ===== verses spinner itemSelected =====
                        spinnerChapters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String chapterSelected = spinnerChapters.getSelectedItem().toString();

                                int chapter = Integer.valueOf(chapterSelected);

                                // Get an obect map of the verses under a certain chapter
                                chapterVerses = wholeBible.get(testamentSelected).get(bookSelected).get(chapter);

                                Log.e(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                                Log.e(TAG, "@@@@@@=== " + bookSelected + " CHAPTER " + chapterSelected+" =====@@@@@@@@@@@@");
                                Log.e(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                                txtDisplay.setText("");
                                for (int verse: chapterVerses.keySet()){

                                    String theVerseItself = chapterVerses.get(verse);
                                    Log.e(TAG, "$$$ verse " + verse + ": " + theVerseItself);
                                    txtDisplay.append(verse +". "+ theVerseItself + "\n");

                                    //Display the book and chapter selected
                                    txtHello.setText(bookSelected +" Chapter " + chapterSelected);
                                }
                                Log.e(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                                Log.e(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });




                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); //SpinnerTestaments onItemSelected

        // Setting the books spinner items
        //bibleBooksSet = wholeBible.get("OT").keySet(); // Gives a set of both testaments


    }


    @Override
    protected void onStart() {
        super.onStart();

        //wholeBible = new Gson().fromJson(getString(R.string.BibleJSon), new TypeToken<Map<String, Map<String, Map<Integer, Map<Integer, String>>>>>() {
        //}.getType());
        method(MainActivity.this);

        Log.e(TAG, "========= WELCOME TO VICTORS BIBLE DESIGN ========");
        Log.e(TAG, "__________________________________________________");
        Log.e(TAG, ".");
        Log.e(TAG, ".");
        Log.e(TAG, "ALL TESTAMENTS >> " + wholeBible.keySet());
        Log.e(TAG, "ALL OT books >> " + wholeBible.get("OT").keySet());
        Log.e(TAG, "__________________________________________________");
        Log.e(TAG, "ALL NT books >> " + wholeBible.get("NT").keySet());
        Log.e(TAG, "__________________________________________________");
        Log.e(TAG, "ALL EXODUS chapters >> " + wholeBible.get("OT").get("Exodus").keySet());
        Log.e(TAG, "__________________________________________________");
        Log.e(TAG, "ALL EXODUS chapter 1 Verses >> " + wholeBible.get("OT").get("Exodus").get(1).values());
        Log.e(TAG, "__________________________________________________");
        Log.e(TAG, "ALL GENESIS List of chapters >> " + wholeBible.get("OT").get("Genesis").keySet());
        Log.e(TAG, "__________________________________________________");
        Log.e(TAG, "ALL GENESIS LIST OF chapter 1 verse numbers >> " + wholeBible.get("OT").get("Genesis").get(1).keySet());
        Log.e(TAG, "__________________________________________________");
        Log.e(TAG, "ALL GENESIS LIST OF chapter 1 verse texts >> " + wholeBible.get("OT").get("Genesis").get(1).values());
        Log.e(TAG, "__________________________________________________");


        Log.e(TAG, "========= END OF VICTORS BIBLE DESIGN ========");
    }


    private Map<String, Map<String, Map<Integer, Map<Integer, String>>>> method(Context context) {
        //Map<String, Map<String, Map<Integer, Map<Integer, String>>>> wholeBible = new Gson().fromJson(context.getString(R.string.BibleJSon), new TypeToken<Map<String, Map<String, Map<Integer, Map<Integer, String>>>>>(){}.getType());
        wholeBible = new Gson().fromJson(context.getString(R.string.BibleJSon), new TypeToken<Map<String, Map<String, Map<Integer, Map<Integer, String>>>>>() {
        }.getType());
        return wholeBible;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_items, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelected = item.getItemId();

        if(itemSelected == R.id.menu_info){
            AlertDialog.Builder infoBuilder = new AlertDialog.Builder(this);
            infoBuilder.setView(R.layout.app_custom_info)
                    .setCancelable(true)
                    .setTitle("About this project....")
                    .setIcon(R.drawable.info)
                    .setPositiveButton("Got it. Thanks!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "Don't forget to donate for this project", Toast.LENGTH_LONG).show();
                        }
                    });
            infoBuilder.create().show();
        }

        return super.onOptionsItemSelected(item);
    }
}
