package ca.unb.mobiledev.ultimatestattracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import ca.unb.mobiledev.ultimatestattracker.helper.FileUtils.getGamesFromFileSystem

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_new_game : Button = findViewById<Button>(R.id.btn_new_game)
        btn_new_game.setOnClickListener {
            val intent = Intent(this, CreateGame::class.java)
            startActivity(intent)
        }

        val btn_view_teams : Button = findViewById<Button>(R.id.btn_view_teams)
        btn_view_teams.setOnClickListener {
            val intent = Intent(this, ViewTeams::class.java)
            startActivity(intent)
        }
    }
}