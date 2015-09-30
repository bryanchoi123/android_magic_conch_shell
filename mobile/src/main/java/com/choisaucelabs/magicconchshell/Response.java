package com.choisaucelabs.magicconchshell;

import android.net.Uri;

import java.util.Comparator;

public class Response implements Comparable<Response>
{
    private String text;
    private Uri fileLocation;

    public Response(String responseText, Uri responseRecording)
    {
        text = responseText;
        fileLocation = responseRecording;
    }

    public Response(String responseText)
    {
        text = responseText;
        fileLocation = null;
    }

    @Override
    public int compareTo(Response other)
    {
        return text.compareToIgnoreCase(other.getResponse());
    }

    @Override
    public boolean equals(Object otherResponse)
    {
        if(this == otherResponse)
            return true;

        if(otherResponse != null && otherResponse instanceof Response)
        {
            Response other = (Response)otherResponse;
            return text.equalsIgnoreCase(other.getResponse());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int sum = 0;
        for(int i = 0; i<text.length(); i++)
            sum += text.charAt(i)/i;
        return sum;
    }

    public String getResponse(){return text;}
    public Uri getRecordingLocation(){return fileLocation;}

    public String toString()
    {
        return text;
    }
}
