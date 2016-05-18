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
    FileHandler(Context c, String name){
        context = c;
        filename = name;
    }
    public void incrementLevel()  {
        writeToFile(String.valueOf(getLevel() + 1));
    }
    public int getLevel(){
        return Integer.valueOf(readFromFile().get(0));
    }

    private void writeToFile(String data) {
        try {
            File file = new File(context.getFilesDir(),filename);
            if (!file.exists()) {
                createFile();
            }
            FileWriter fOut = new FileWriter(file);
            fOut.write(data);
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

    private ArrayList<String> readFromFile() {
        ArrayList<String> sReturn = new ArrayList<>();

        try {
            File file = new File(context.getFilesDir(),filename);
            if (!file.exists()) {
                writeToFile("0");
            }
            FileReader fIn = new FileReader(file);
            BufferedReader textReader = new BufferedReader(fIn);
            String sNextLine;
            while((sNextLine = textReader.readLine()) != null){
                sReturn.add(sNextLine);
            }
        }
        catch (Exception e) {
            Log.e("Exception", "File Read Failed: " + e.toString());
        }
        return sReturn;
    }
}