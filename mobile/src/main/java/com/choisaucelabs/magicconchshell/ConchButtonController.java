package com.choisaucelabs.magicconchshell;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class ConchButtonController implements OnClickListener
{
    private final String TAG = "conch_shell";
    private final String fileName = "responses.json";

    private MainActivity mainActivity;
    private ArrayList<Response> responses;

    public ConchButtonController(MainActivity main)
    {
        mainActivity = main;

        if(FileIO.internalFileExists(main, fileName))
            responses = FileIO.readInternalFile(main, fileName);
        else
            parseResponses();
    }

    private void parseResponses()
    {
        ArrayList<String> responsesList = new ArrayList<>();
        ArrayList<Uri> responseFilesList = new ArrayList<>();

        XmlPullParser parser = mainActivity.getResources().getXml(R.xml.conch_responses);

        try
        {
            while(parser.next() != XmlPullParser.END_DOCUMENT)
            {
                if(XmlPullParser.START_TAG == parser.getEventType())
                    if("response".equals(parser.getName()))
                    {
                        String fileName = parser.getAttributeValue(null, "file");
                        Uri path = Uri.parse("android.resource://com.choisaucelabs.magicconchshell/raw/" + fileName);
                        responseFilesList.add(path);
                    }
                if(XmlPullParser.TEXT == parser.getEventType())
                    responsesList.add(parser.getText());
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }

        responses = new ArrayList<>();
        for(int i = 0; i<responsesList.size(); i++)
        {
            responses.add(new Response(responsesList.get(i), responseFilesList.get(i)));
        }
    }

    public void addResponse(Response r)
    {
        if(responses.contains(r))
        {
            responses.remove(r);
        }
        responses.add(r);
    }

    public void addResponse(String response)
    {
        addResponse(new Response(response));
    }

    public void removeResponse(Response r)
    {
        responses.remove(r);
    }

    public void removeResponse(String response)
    {
        removeResponse(new Response(response));
    }

    public ArrayList<Response> getResponses(){return responses;}

    @Override
    public void onClick(View view)
    {
        int randIndex = (int)(Math.random()* responses.size());
        Response randResponse = responses.get(randIndex);

        mainActivity.showResponse(randResponse.getResponse(), randResponse.getRecordingLocation());
    }

    public void finish()
    {
        FileIO.writeInternalFile(mainActivity, fileName, responses);
    }
}
