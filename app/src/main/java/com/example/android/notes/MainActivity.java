package com.example.android.notes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notesArrayList = new ArrayList<String>();
    static ArrayAdapter arrayAdapter;

    static Set<String> stringSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        stringSet = sharedPreferences.getStringSet("notes", null);

        if (stringSet != null) {
            notesArrayList.clear();
            notesArrayList.addAll(stringSet);
        } else {
            notesArrayList.add("Example Note");

            stringSet = new HashSet<String>();
            stringSet.addAll(notesArrayList);
            sharedPreferences.edit().putStringSet("notes", stringSet).apply();
        }

        final ListView notesListView = (ListView) findViewById(R.id.notesListView);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, notesArrayList);

        notesListView.setAdapter(arrayAdapter);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), EditNote.class);
                i.putExtra("noteId", position);

                startActivity(i);
            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure")
                        .setMessage("do you want to delete")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notesArrayList.remove(position);

                                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

                                if (stringSet == null) {
                                    stringSet = new HashSet<String>();
                                } else {
                                    stringSet.clear();
                                }
                                stringSet.addAll(notesArrayList);
                                sharedPreferences.edit().remove("notes").apply();
                                sharedPreferences.edit().putStringSet("notes", stringSet).apply();

                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            notesArrayList.add("");

            SharedPreferences sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

            if (stringSet == null) {
                stringSet = new HashSet<String>();
            } else {
                stringSet.clear();
            }
            stringSet.addAll(notesArrayList);
            sharedPreferences.edit().remove("notes").apply();

            sharedPreferences.edit().putStringSet("notes", stringSet).apply();
            arrayAdapter.notifyDataSetChanged();

            Intent i = new Intent(getApplicationContext(), EditNote.class);
            i.putExtra("noteId", notesArrayList.size() - 1);

            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

