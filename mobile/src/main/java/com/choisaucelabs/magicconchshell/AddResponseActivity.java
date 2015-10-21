package com.choisaucelabs.magicconchshell;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddResponseActivity extends AppCompatActivity
{
    private EditText responseText, responseFile;
    private Button browse, submit, clear;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_response);

        responseText = (EditText)findViewById(R.id.response_text);
        responseFile = (EditText)findViewById(R.id.response_file_name);
    }

    public void clearFields(View v)
    {
        responseText.setText("");
        responseFile.setText("");
    }

}
