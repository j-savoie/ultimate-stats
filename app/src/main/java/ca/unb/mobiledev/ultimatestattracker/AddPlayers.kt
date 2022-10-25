package ca.unb.mobiledev.ultimatestattracker

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ca.unb.mobiledev.ultimatestattracker.model.Team
import ca.unb.mobiledev.ultimatestattracker.model.Player
import kotlin.math.ln

class AddPlayers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_players)
        val fNameField = findViewById<EditText>(R.id.editTextfName)
        val lnameField = findViewById<EditText>(R.id.editTextLname)
        val numberField = findViewById<EditText>(R.id.editTextNumber)
        val genderSpinner = findViewById<Spinner>(R.id.spinner)
        val button : Button = findViewById<Button>(R.id.AddPlayerButton)
        val completeRoster : Button = findViewById<Button>(R.id.completeRoster)
        var team : Team = intent.getSerializableExtra("AddPlayers") as Team
        val genders = arrayOf<String>("Male", "Female", "Other")
        val adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = adapter

        button.setOnClickListener{
            var fname = fNameField.text.toString()
            var lname = lnameField.text.toString()
            var number = numberField.text.toString().toInt()
            var genderString = genderSpinner.selectedItem.toString()
            var gender : Player.GENDER = Player.GENDER.Male
            when(genderString){
                "Male" -> gender = Player.GENDER.Male
                "Female" -> gender = Player.GENDER.Female
                "Other" -> gender = Player.GENDER.Other
            }
            var player = Player(fname,lname,number, gender)
            team.players.add(player)
            Toast.makeText(this, "Player Added: $fname $lname", Toast.LENGTH_SHORT).show()
            fNameField.setText("")
            lnameField.setText("")
            numberField.setText("")
        }
        completeRoster.setOnClickListener{
            Log.e("team: ", team.teamName)
            Log.e("player: ", team.players.get(0).fName)
        }
    }
}