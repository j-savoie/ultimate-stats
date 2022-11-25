package ca.unb.mobiledev.ultimatestattracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ca.unb.mobiledev.ultimatestattracker.model.Player
import ca.unb.mobiledev.ultimatestattracker.model.Team

class CreateTeam : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_team)
        val btnSave : Button = findViewById<Button>(R.id.btnCreateTeam)
        val editText = findViewById<EditText>(R.id.editTextTeamName)
        var teamName : String
        btnSave.setOnClickListener {
            teamName = editText.text.toString()
            var playerList = ArrayList<Player>()
            var team = Team(teamName, playerList)

            // Save team to file system
            team.save(this)

            // Check dir after save
            val teamsDir = getDir("teams", MODE_PRIVATE)
            println(teamsDir.absolutePath)
            for(file in teamsDir.listFiles()!!){
                println(file.name)
            }

            // Launch AddPlayers activity
            val intent = Intent(this, AddPlayers::class.java)
            intent.putExtra("Team", team)
            startActivity(intent)
        }
    }
}