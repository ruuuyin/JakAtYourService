package jays.data;

import com.google.gson.Gson;

import java.io.*;

public class JsonWorker {
    private static final Gson GSON = new Gson();
    private static final String JSON_DIR = System.getProperty("user.home")+"\\.jays_metadata\\";

    public static void toJson(Object obj,String jsonFileName){
        File jsonDir = new File(JSON_DIR);
        if (!jsonDir.exists()) jsonDir.mkdir();
        try {
            Writer writer = new FileWriter(new File(jsonDir.getPath()+"\\"+jsonFileName+".json"));
            GSON.toJson(obj,writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object fromJson(String jsonFileName, Class classType){
        try {
            Reader reader = new FileReader(new File(JSON_DIR+"\\"+jsonFileName+".json"));
            Object o = GSON.fromJson(reader,classType);
            reader.close();
            return o;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
