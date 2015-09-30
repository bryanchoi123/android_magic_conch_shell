package com.choisaucelabs.magicconchshell;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class ConchButtonController implements View.OnClickListener
{
    private final String TAG = "magic_conch";

    private String[] responses;
    private Context toastContext;

    public ConchButtonController(Context context)
    {
        //main = mainActivity;
        toastContext = context;
        parseResponses();
    }

    private void parseResponses()
    {
        ArrayList<String> responsesList = new ArrayList<String>();

        XmlPullParser parser = toastContext.getResources().getXml(R.xml.conch_responses);

        try
        {
            while(parser.next() != XmlPullParser.END_DOCUMENT)
            {
                if(XmlPullParser.TEXT == parser.getEventType())
                    responsesList.add(parser.getText());
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }

        responses = new String[responsesList.size()];

        for(int i = 0; i<responsesList.size(); i++)
            responses[i] = responsesList.get(i);
    }

    @Override
    public void onClick(View view)
    {
        int randIndex = (int)(Math.random() * responses.length);
        String response = responses[randIndex];

        Toast message = Toast.makeText(toastContext, response, Toast.LENGTH_SHORT);
        message.show();
    }
}
