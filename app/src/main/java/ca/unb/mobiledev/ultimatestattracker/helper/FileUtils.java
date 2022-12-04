package ca.unb.mobiledev.ultimatestattracker.helper;

import static android.content.Context.MODE_PRIVATE;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import ca.unb.mobiledev.ultimatestattracker.model.Game;
import ca.unb.mobiledev.ultimatestattracker.model.Team;

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

    public static File getGameDir(String teamName, Context c){
        File gameDir = new File(getTeamDir(teamName, c), "games");
        if(!gameDir.exists()){
            gameDir.mkdir();
        }
        return gameDir;
    }

    public static ArrayList<Team> getTeamsFromFileSystem(Context c){
        JsonUtils json = new JsonUtils();
        ArrayList<Team> teams = new ArrayList<>();
        File teamsDir = c.getDir("teams", MODE_PRIVATE);
        for(File teamDir : Objects.requireNonNull(teamsDir.listFiles())) {
            Team team = null;
            String teamDirName = teamDir.getName();
            for(File file : Objects.requireNonNull(teamDir.listFiles())) {
                if(file.getName().equals(teamDirName+".json")) {
                    // Read text from file into a string
                    try{
                        team = (Team) json.convertJsonToObject(readFromFile(file), Team.class);
                    } catch (Exception e){
                        System.err.println("Failed to get Object from JSON");
                    }
                    //teams.add(team);
                }
            }
            if(team != null) teams.add(team);
        }
        return teams;
    }

    public static ArrayList<Game> getGamesFromFileSystem(String teamName, Context c){
        JsonUtils json = new JsonUtils();
        ArrayList<Game> games = new ArrayList<>();
        File gamesDir = getGameDir(teamName, c);
        for(File gameFile : Objects.requireNonNull(gamesDir.listFiles())) {
            Game game = null;
            if(gameFile.getName().startsWith("game_")) {
                // Read text from file into a string
                try{
                    game = (Game) json.convertJsonToObject(readFromFile(gameFile), Game.class);
                } catch (Exception e){
                    System.err.println("Failed to get Object from JSON");
                }
            }
            if(game != null) games.add(game);
        }
        return games;
    }

    public static String readFromFile(File file) throws IOException {
        String ret = "";
        FileReader fr = new FileReader(file);
        int i;
        while((i=fr.read())!=-1)
            ret += (char)i;
        fr.close();
        return ret;
    }



    //TODO getTeam(teamName)
}
