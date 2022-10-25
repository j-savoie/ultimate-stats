package ca.unb.mobiledev.ultimatestattracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ca.unb.mobiledev.ultimatestattracker.model.Player
import ca.unb.mobiledev.ultimatestattracker.model.Team
import java.io.Serializable

class CreateTeam : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_team)
        val button : Button = findViewById<Button>(R.id.button2)
        val editText = findViewById<EditText>(R.id.editTextTeamName)
        var teamName : String
        button.setOnClickListener {
            teamName = editText.text.toString()
            val intent = Intent(this, AddPlayers::class.java)
            var playerList = ArrayList<Player>()
            var team = Team(teamName, playerList)
            intent.putExtra("AddPlayers", team)
            startActivity(intent)
        }
    }
}