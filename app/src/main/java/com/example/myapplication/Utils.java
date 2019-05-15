package com.example.myapplication;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static void appendLog(Context context, String text) {
        boolean isWrite = true;
        SimpleDateFormat dateFormatterToServer = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if (isWrite) {
            Date currentDate = new Date();
            String fileName = "bat-log-" + dateFormatterToServer.format(currentDate) + ".txt";
            String dir = context.getFilesDir().getAbsolutePath() + File.separator + "BATCADS";
            File folder = new File(dir);
            if (!folder.exists()) {
                folder.mkdir();
            }
            File logFile = new File(folder.getAbsolutePath() + File.separator + fileName);
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                String strLog = dateFormatterToServer.format(currentDate) + ": " + text;
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                buf.append(strLog);
                buf.newLine();
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
