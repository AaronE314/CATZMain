package menu.catz.aaron.catzmain;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileHandler {
    String filename = "";
    Context context;
    public FileHandler(Context c, String name){
        context = c;
        filename = name;
    }

    public void writeToFile(String data) {
        try {
            File file = new File(context.getFilesDir(),filename);
            if (!file.exists()) {
                createFile();
            }
            FileWriter fOut = new FileWriter(file);
            fOut.append(data);
            fOut.close();

        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void createFile(){
        File file = new File(context.getFilesDir(), filename);
        try {
            file.createNewFile();
        }catch(Exception e){
            Log.e("Exception", "File Create Failed: " + e.toString());
        }
    }

    public String readFromFile() {
        String sReturn = "";

        try {
            File file = new File(context.getFilesDir(),filename);
            if (!file.exists()) {
                writeToFile("");
            }
            FileReader fIn = new FileReader(file);
            BufferedReader textReader = new BufferedReader(fIn);
            String sNextLine;
            while((sNextLine = textReader.readLine()) != null){
                sReturn+=sNextLine;
            }
        }
        catch (Exception e) {
            Log.e("Exception", "File Read Failed: " + e.toString());
        }
        return sReturn;
    }
    public Boolean hasText() {
        if (!readFromFile().equals("")) {
            return true;
        }
        return false;
    }
}