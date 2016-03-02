package com.example.android.notes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import java.util.HashSet;

public class EditNote extends AppCompatActivity implements TextWatcher {

    int noteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText editText = (EditText) findViewById(R.id.editText);

        Intent i = getIntent();
        noteID = i.getIntExtra("noteId",-1);

        if(noteID != -1){
            editText.setText(MainActivity.notesArrayList.get(noteID));
        }

        editText.addTextChangedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        MainActivity.notesArrayList.set(noteID, String.valueOf(s));
        MainActivity.arrayAdapter.notifyDataSetChanged();

        SharedPreferences sharedPreferences = this.getSharedPreferences(getPackageName(),Context.MODE_PRIVATE);

        if (MainActivity.stringSet == null){
            MainActivity.stringSet = new HashSet<String>();
        }
        else {
            MainActivity.stringSet.clear();
        }

        MainActivity.stringSet.addAll(MainActivity.notesArrayList);
        sharedPreferences.edit().remove("notes").apply();
        sharedPreferences.edit().putStringSet("notes",MainActivity.stringSet).apply();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
