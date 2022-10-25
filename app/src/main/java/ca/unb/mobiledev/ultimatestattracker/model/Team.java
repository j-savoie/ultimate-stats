package ca.unb.mobiledev.ultimatestattracker.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Team implements Serializable {
    public String teamName;
    public ArrayList<Player> players;

    /* Constructors */
    public Team(String name, ArrayList<Player> players){
        this.teamName = name;
        this.players = players;
    }
    public Team(String name, Player player){
        this.teamName = name;
        ArrayList<Player> list = new ArrayList<Player>();
        list.add(player);
        this.players = list;
    }
    public Team(String name){
        this.teamName = name;
        ArrayList<Player> list = new ArrayList<Player>();
        this.players = list;
    }
}
