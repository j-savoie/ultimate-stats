package ca.unb.mobiledev.ultimatestattracker.helper;

import static android.content.Context.MODE_PRIVATE;
import android.content.Context;
import java.io.File;

public class FileUtils {
    public static String toFileName(String str){
        return str.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    public static File getTeamDir(String teamName, Context c){
        File teamDir = new File(c.getDir("teams", MODE_PRIVATE), toFileName(teamName));
        if(!teamDir.exists()){
            teamDir.mkdir();
        }
        return teamDir;
    }
}
