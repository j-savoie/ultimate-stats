package ca.unb.mobiledev.ultimatestattracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.ultimatestattracker.adapter.TeamAdapter
import ca.unb.mobiledev.ultimatestattracker.helper.JsonUtils
import ca.unb.mobiledev.ultimatestattracker.model.Team
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.concurrent.Executors

class ViewTeams : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TeamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_teams)

        // Set up the RecyclerView
        adapter = TeamAdapter(this, ArrayList())
        recyclerView = findViewById<RecyclerView>(R.id.listTeams)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        // Set up the FAB
        val fab = findViewById<FloatingActionButton>(R.id.fab_addTeam)
        fab.setOnClickListener {
            val intent = Intent(this, CreateTeam::class.java)
            intent.putExtra("teams", adapter.getTeams())
            startActivityForResult(intent, 2)
        }
    }

    override fun onResume() {
        super.onResume()
        // Load the teams from the file system
        Executors.newSingleThreadExecutor()
            .execute {
                val mainHandler = Handler(Looper.getMainLooper())
                // Get the list of teams by browsing the FS
                var teams : ArrayList<Team> = ArrayList()
                val json = JsonUtils()
                var team : Team? = null
                teams = getTeamsFromFileSystem()
                // Update the UI
                mainHandler.post {
                    adapter.setTeams(teams)
                    recyclerView.adapter = adapter
                }
            }
        // end executor
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                val teams : ArrayList<Team> = data?.getSerializableExtra("teams") as java.util.ArrayList<Team>
                val team = data.getSerializableExtra("team") as Team
                adapter.setTeams(teams)
                recyclerView.adapter = adapter
                val intent = Intent(this, ViewTeam::class.java)
                intent.putExtra("team", team)
                startActivity(intent)
            }
        }
    }

    private fun getTeamsFromFileSystem() : ArrayList<Team>{
        val json = JsonUtils()
        var teams : ArrayList<Team> = ArrayList()
        val teamsDir = getDir("teams", MODE_PRIVATE)
        for(teamDir in teamsDir.listFiles()!!) {
            var team : Team? = null
            val teamDirName = teamDir.name
            for(file in teamDir.listFiles()!!) {
                if(file.name == "$teamDirName.json") {
                    team = json.convertJsonToObject(file.readText(), Team::class.java) as Team
                }
            }
            if(team != null) teams.add(team)
        }
        return teams
    }
}