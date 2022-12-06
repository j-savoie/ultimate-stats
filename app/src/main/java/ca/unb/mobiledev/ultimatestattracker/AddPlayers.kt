package ca.unb.mobiledev.ultimatestattracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ca.unb.mobiledev.ultimatestattracker.model.Player
import ca.unb.mobiledev.ultimatestattracker.model.Team

class AddPlayers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_players)
        val fNameField = findViewById<EditText>(R.id.editTextfName)
        val lnameField = findViewById<EditText>(R.id.editTextLname)
        val numberField = findViewById<EditText>(R.id.editTextNumber)
        val genderSpinner = findViewById<Spinner>(R.id.spinner)
        val btnAddPlayer : Button = findViewById(R.id.AddPlayerButton)
        val completeRoster : Button = findViewById(R.id.completeRoster)
        val team : Team = intent.getSerializableExtra("team") as Team
        val genders = arrayOf("Male", "Female", "Other") // TODO reference instead to Player.Gender type for extensibility?
        val adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = adapter

        btnAddPlayer.setOnClickListener{
            val fname = fNameField.text.toString()
            val lname = lnameField.text.toString()
            val number = numberField.text.toString().toInt()
            val genderString = genderSpinner.selectedItem.toString()
            var gender : Player.GENDER = Player.GENDER.Male
            when(genderString){
                "Male" -> gender = Player.GENDER.Male
                "Female" -> gender = Player.GENDER.Female
                "Other" -> gender = Player.GENDER.Other
            }
            val player = Player(fname,lname,number, gender)
            val pAdded = team.addPlayer(player, this)
            if(pAdded) Toast.makeText(this, "Player Added: $fname $lname", Toast.LENGTH_SHORT).show()
            fNameField.setText("")
            lnameField.setText("")
            numberField.setText("")
        }
        completeRoster.setOnClickListener{
            team.save(this)
            val intent = Intent(this, ViewTeam::class.java)
            intent.putExtra("team", team)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}