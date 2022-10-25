package ca.unb.mobiledev.ultimatestattracker.model;

import java.io.Serializable;

public class Player implements Serializable {
    public String fName;
    public String lName;
    public int number;
    public GENDER gender;

    public enum GENDER {
        Male,
        Female,
        Other
    }

    public Player(String fName, String lName, int num, GENDER gender){
        this.fName = fName;
        this.lName = lName;
        this.number = num;
        this.gender = gender;
    }
}
