package ca.unb.mobiledev.ultimatestattracker

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.ultimatestattracker.adapter.PlayerAdapter
import ca.unb.mobiledev.ultimatestattracker.adapter.ViewPagerAdapter
import ca.unb.mobiledev.ultimatestattracker.databinding.ActivityViewTeamBinding
import ca.unb.mobiledev.ultimatestattracker.helper.FileUtils.getGamesFromFileSystem
import ca.unb.mobiledev.ultimatestattracker.model.Game
import ca.unb.mobiledev.ultimatestattracker.model.Team
import com.google.android.material.tabs.TabLayoutMediator
import java.util.concurrent.Executors

val tabArray = arrayOf(
    "Players",
    "Games",
    "Stats"
)

class ViewTeam : AppCompatActivity() {
    lateinit var binding: ActivityViewTeamBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extras = intent.extras
        if (extras == null) finish()
        val team = extras?.getSerializable("team") as Team
        var games : ArrayList<Game> = ArrayList()
        Executors.newSingleThreadExecutor().execute(){
            val mainHandler = Handler(Looper.getMainLooper())
            var gotGames = getGamesFromFileSystem(team.teamName, this)
            mainHandler.post {
                games = gotGames
            }
        }

        // Make Bundle to pass info to fragments
        val bundle = Bundle()
        bundle.putSerializable("team", team)
        bundle.putSerializable("games", games)

        // Set up the binding
        binding = ActivityViewTeamBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Set up the ViewPager and Adapter
        val viewPager = binding.viewPager
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, bundle)
        viewPager.adapter = adapter

        // Set up the TabLayout
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabArray[position]
        }.attach()

        val toolbar = binding.toolbar
        val tabs = binding.tabLayout
        // Set title in toolbar
        toolbar.title = team.teamName
    }
}