package ca.unb.mobiledev.ultimatestattracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
            // Make team object with team name
            teamName = editText.text.toString()
            var playerList = ArrayList<Player>()
            var team = Team(teamName, playerList)

            // Save team to file system
            team.save(this)

            // Launch AddPlayers activity
            val teams = intent.extras?.getSerializable("teams") as ArrayList<Team>
            teams.add(team)
            Log.d("CreateTeam", "Team added to list: $teams")
            val intent = Intent(this, ViewTeam::class.java)
            intent.putExtra("teams", teams)
            intent.putExtra("team", team)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}