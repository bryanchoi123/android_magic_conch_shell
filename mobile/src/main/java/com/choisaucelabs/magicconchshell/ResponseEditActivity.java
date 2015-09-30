package com.choisaucelabs.magicconchshell;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ResponseEditActivity extends AppCompatActivity
{
    private ArrayList<Response> responses;
    private ArrayAdapter<Response> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responses);

        adapter = new ArrayAdapter<Response>(this, R.layout.list_view_element);
        responses = new ArrayList<>();

       // parse from extras
        Intent i = getIntent();
        String[] text = i.getStringArrayExtra(MainActivity.EXTRA_RESPONSE_TEXT);
        String[] path = i.getStringArrayExtra(MainActivity.EXTRA_RESPONSE_URI);

        for(int index = 0; index<text.length; index++)
        {
            Response r = new Response(text[index], Uri.parse(path[index]));
            insertInOrder(r);
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.responses_menu_main, menu);
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
            case R.id.add_response:
                createNewResponse();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createNewResponse()
    {
        Intent i = new Intent(this, AddResponseActivity.class);
        startActivity(i);
    }

    /**
     * Insert the given response in alphabetical order
     *
     * @param r - of type Response; the response to add in
     */
    private void insertInOrder(Response r)
    {
        int right = responses.size()-1;
        int left = 0;

        while(left <= right)
        {
            int mid = (left + right)/2;
            Response midResponse = responses.get(mid);
            if(r.compareTo(midResponse) <= 0)
                right = mid-1;
            else
                left = mid + 1;
        }
        responses.add(left, r);
        adapter.insert(r, left);
    }
}
