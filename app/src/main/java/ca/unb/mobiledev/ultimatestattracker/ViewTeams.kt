package ca.unb.mobiledev.ultimatestattracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ViewTeams : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_teams)

        val list = findViewById<RecyclerView>(R.id.listTeams)
        val adapter = TeamAdapter(this)

        val fab = findViewById<FloatingActionButton>(R.id.fab_addTeam)
        fab.setOnClickListener {
            val intent = Intent(this, CreateTeam::class.java)
            startActivity(intent)
        }
    }

    class TeamAdapter(teams: ViewTeams) {

    }
}