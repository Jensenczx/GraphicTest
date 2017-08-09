package com.chenjensen.graphictest.java_layer.surfaceview;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by chenjensen on 17/7/20.
 */

public class ResouceReader {
    public static String readTextFromResource(Context context, int resourceId) {
        StringBuilder builder = new StringBuilder();
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                builder.append(nextLine);
                builder.append("\n");
            }
        }catch (IOException e) {

        }catch (Resources.NotFoundException e) {

        }
        return builder.toString();
    }

}
