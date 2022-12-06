package ca.unb.mobiledev.ultimatestattracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import ca.unb.mobiledev.ultimatestattracker.helper.FileUtils.getGamesFromFileSystem
import ca.unb.mobiledev.ultimatestattracker.helper.FileUtils.getTeamsFromFileSystem
import ca.unb.mobiledev.ultimatestattracker.helper.JsonUtils
import ca.unb.mobiledev.ultimatestattracker.model.Team
import java.util.concurrent.Executors

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

        Executors.newSingleThreadExecutor()
            .execute {
                val mainHandler = Handler(Looper.getMainLooper())
                // Get the list of teams by browsing the FS
                var teams : ArrayList<Team> = ArrayList()
                teams = getTeamsFromFileSystem(this)
                if(teams.size == 0) {
                    mainHandler.post {
                        btn_new_game.isEnabled = false
                    }
                }
            }
        // end executor
    }
}