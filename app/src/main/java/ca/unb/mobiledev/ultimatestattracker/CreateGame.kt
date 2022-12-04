package ca.unb.mobiledev.ultimatestattracker

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ca.unb.mobiledev.ultimatestattracker.helper.FileUtils.getTeamsFromFileSystem
import ca.unb.mobiledev.ultimatestattracker.model.Event
import ca.unb.mobiledev.ultimatestattracker.model.Game
import ca.unb.mobiledev.ultimatestattracker.model.Team

class CreateGame : AppCompatActivity(){
    override fun onCreate(savedInstanceState : Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_game)

        val extras = intent.extras
        val team = extras?.get("team") as Team?

        val spinnerTeam = findViewById<Spinner>(R.id.spinnerTeams)
        val textOppTeam = findViewById<EditText>(R.id.editTextOppTeam)
        val textPointCap = findViewById<EditText>(R.id.editTextPointCap)
        val textTODuration = findViewById<EditText>(R.id.editTextTODuration)
        val textHTDuration = findViewById<EditText>(R.id.editTextHTDuration)
        val textTOPerHalf = findViewById<EditText>(R.id.editTextTOPerHalf)
        val btnStartGame = findViewById<Button>(R.id.btnStartGame)

        // Populate spinner with teams from file system
        val teams = getTeamsFromFileSystem(this)
        val teamNames = ArrayList<String>()
        for(t in teams) {
            teamNames.add(t.teamName)
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, teamNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTeam.adapter = adapter
        if(team != null) {
            spinnerTeam.setSelection(teamNames.indexOf(team.teamName))
            spinnerTeam.isEnabled = false
        }

        btnStartGame.setOnClickListener {
            val selectedTeam = teams[spinnerTeam.selectedItemPosition]
            if(selectedTeam.players.size < 7) {
                Toast.makeText(this, "You must have atleast 7 players on your team to start a game", Toast.LENGTH_LONG).show()
            }
            else {
                try {
                    val oppTeam = textOppTeam.text.toString()
                    val pointCap = textPointCap.text.toString().toInt()
                    val toDuration = textTODuration.text.toString().toInt()
                    val htDuration = textHTDuration.text.toString().toInt()
                    val toPerHalf = textTOPerHalf.text.toString().toInt()
                    val game = Game(
                        selectedTeam,
                        oppTeam,
                        0,
                        0,
                        pointCap,
                        toDuration,
                        htDuration,
                        toPerHalf,
                        ArrayList<Event>()
                    )
                    game.save(this)
                    val intent = Intent(this, StatTracker::class.java)
                    intent.putExtra("game", game)
                    startActivity(intent)
                    finish()
                } catch (nfe: NumberFormatException) {
                    Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}