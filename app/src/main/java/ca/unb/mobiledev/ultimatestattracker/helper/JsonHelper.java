package ca.unb.mobiledev.ultimatestattracker.helper;
import com.google.gson.*;
public class JsonHelper {

    public String convertObjectToJson(Object obj){
        try {
            Gson gson = new Gson();
            String str = gson.toJson(obj);
            System.out.println("== Json Obj ==\n"+str);
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
            System.out.println("== Java Obj ==\n"+obj);
            return obj;
        } catch (Exception e){
            System.err.println("Failed to get Object from JSON");
            return null;
        }
    }
}
