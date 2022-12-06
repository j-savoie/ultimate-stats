package ca.unb.mobiledev.ultimatestattracker.helper;
import android.util.Log;

import com.google.gson.*;

import java.util.ArrayList;

public class JsonUtils {

    public String convertObjectToJson(Object obj){
        try {
            Gson gson = new Gson();
            String str = gson.toJson(obj);
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
            return obj;
        } catch (Exception e){
            System.err.println("Failed to get Object from JSON");
            return null;
        }
    }
}
