package com.choisaucelabs.magicconchshell;

import android.content.Context;
import android.net.Uri;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class FileIO
{
    private static final String TAG = "magic_conch";

    public static boolean internalFileExists(Context appContext, String fileName)
    {
        File directory = appContext.getFilesDir();
        File check = new File(directory, fileName);

        return check.exists();
    }

    public static ArrayList<Response> readInternalFile(Context appContext, String fileName)
    {
        if(internalFileExists(appContext, fileName))
            return readFile(appContext.getFilesDir() + "/" + fileName);
        return null;
    }

    public static void writeInternalFile(Context appContext, String fileName, ArrayList<Response> responses)
    {
        String filePath = appContext.getFilesDir() + "/" + fileName;
        writeFile(filePath, responses);
    }

    private static void writeFile(String absolutePath, ArrayList<Response> responses)
    {
        try
        {
            JsonWriter writer = new JsonWriter(new PrintWriter(new File(absolutePath)));

            // begin top-level array
            writer.beginArray();

            // write each response
            for(Response r: responses)
            {
                writer.beginObject();
                writer.name("response").value(r.getResponse());

                Uri recordingLocation = r.getRecordingLocation();
                if(recordingLocation == null)
                    writer.name("file_uri").nullValue();
                else
                    writer.name("file_uri").value(r.getRecordingLocation().toString());

                writer.endObject();
            }

            writer.endArray();
            writer.close();
        }
        catch(Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
    }

    private static ArrayList<Response> readFile(String absolutePath)
    {
        ArrayList<Response> allResponses = new ArrayList<>();

        try
        {
            JsonReader reader = new JsonReader(new BufferedReader(new FileReader(absolutePath)));

            // read opening array
            reader.beginArray();
            while(reader.hasNext())
            {
                String response = "";

                // read object
                reader.beginObject();
                while(reader.hasNext())
                {
                    String name = reader.nextName();
                    if(name.equals("response"))
                        response = reader.nextString();
                    else if(name.equals("file_uri"))
                    {
                        Uri fileLocation = null;

                        if (reader.peek() != JsonToken.NULL)
                            fileLocation = Uri.parse(reader.nextString());

                        allResponses.add(new Response(response, fileLocation));
                    }
                    else
                        reader.skipValue();
                }
                reader.endObject();
            }

            reader.endArray();
            reader.close();
        }
        catch(Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }

        return allResponses;
    }
}
