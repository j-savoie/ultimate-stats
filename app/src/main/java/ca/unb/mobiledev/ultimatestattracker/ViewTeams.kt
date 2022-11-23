package ca.unb.mobiledev.ultimatestattracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.ultimatestattracker.helper.JsonUtils
import ca.unb.mobiledev.ultimatestattracker.model.Player
import ca.unb.mobiledev.ultimatestattracker.model.Team
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.concurrent.Executors

class ViewTeams : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TeamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_teams)

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
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        Executors.newSingleThreadExecutor()
            .execute {
                val mainHandler = Handler(Looper.getMainLooper())
                // Get the list of teams by browsing the FS
                val dir = getDir("teams", MODE_PRIVATE)
                val teams : ArrayList<Team> = ArrayList()
                val json = JsonUtils()
                for(file in dir.listFiles()) {
                    val team = json.convertJsonToObject(file.readText(), Team::class.java) as Team
                    teams.add(team)
                }
                // Update the UI
                mainHandler.post {
                    adapter.setTeams(teams)
                    recyclerView.adapter = adapter
                }
            }
        // end executor
    }
}