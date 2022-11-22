package ca.unb.mobiledev.ultimatestattracker.helper;
import android.util.Log;

import com.google.gson.*;
public class JsonUtils {

    public String convertObjectToJson(Object obj){
        try {
            Gson gson = new Gson();
            String str = gson.toJson(obj);
            Log.i("JsonUtils", "== Json String ==\n"+str);
            return str;
        } catch (Exception e){
            System.err.println("Failed to convert Object to JSON");
            return null;
        }
    }

    public Object convertJsonToObject(String str, Class type){
        try{
            Gson gson = new Gson();
            Object obj = gson.fromJson(str, type);
            Log.i("JsonUtils", "== Java Obj ==\n"+obj);
            return obj;
        } catch (Exception e){
            System.err.println("Failed to get Object from JSON");
            return null;
        }
    }


}
