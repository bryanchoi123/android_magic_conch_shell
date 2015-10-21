package com.choisaucelabs.magicconchshell;

import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
{
    private final String TAG = "magic_conch";
    public static final String EXTRA_RESPONSE_TEXT = "responses";
    public static final String EXTRA_RESPONSE_URI = "Uris";

    private AudioController audioController;
    private ConchButtonController buttonController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add click listener for conch shell button
        ImageButton conch = (ImageButton) this.findViewById(R.id.conch);
        buttonController = new ConchButtonController(this);
        conch.setOnClickListener(buttonController);

        // use volume control keys for app's volume
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        audioController = new AudioController(this);
    }

    public void showResponse(String response, Uri responseFile)
    {
        int duration = audioController.prepareAudio(responseFile);
        if(duration > -1)
        {
            audioController.playAudio();

            TextView responseArea = (TextView) findViewById(R.id.response);
            responseArea.setText(response);
        }
    }

    private void editResponses()
    {
        Intent i = new Intent(this, EditResponseActivity.class);
        ArrayList<Response> responses = buttonController.getResponses();

        String[] text = new String[responses.size()];
        String[] path = new String[text.length];

        int index = 0;
        for(Response r: responses)
        {
            text[index] = r.getResponse();
            path[index++] = r.getRecordingLocation().toString();
        }

        i.putExtra(EXTRA_RESPONSE_TEXT, text);
        i.putExtra(EXTRA_RESPONSE_URI, path);

        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id)
        {
            case R.id.edit_responses:
                editResponses();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();

        audioController.destroy();
        buttonController.finish();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();

        audioController = new AudioController(this);
        buttonController = new ConchButtonController(this);
    }
}