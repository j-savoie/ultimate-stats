package ca.unb.mobiledev.ultimatestattracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.unb.mobiledev.ultimatestattracker.adapter.ViewPagerAdapter
import ca.unb.mobiledev.ultimatestattracker.databinding.ActivityViewTeamBinding
import ca.unb.mobiledev.ultimatestattracker.model.Team
import com.google.android.material.tabs.TabLayoutMediator

val tabArray = arrayOf(
    "Players",
    "Games",
    "Stats"
)

class ViewTeam : AppCompatActivity() {
    lateinit var binding: ActivityViewTeamBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val extras = intent.extras
        val team = extras?.get("team") as Team
        super.onCreate(savedInstanceState)

        // Set up the binding
        binding = ActivityViewTeamBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Set up the ViewPager
        val viewPager = binding.viewPager
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
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