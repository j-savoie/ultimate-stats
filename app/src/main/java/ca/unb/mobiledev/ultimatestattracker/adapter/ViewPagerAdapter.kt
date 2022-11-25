package ca.unb.mobiledev.ultimatestattracker.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ca.unb.mobiledev.ultimatestattracker.fragments.FragTeamGames
import ca.unb.mobiledev.ultimatestattracker.fragments.FragTeamPlayers
import ca.unb.mobiledev.ultimatestattracker.fragments.FragTeamStats

private const val NUM_TABS = 3

public class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return FragTeamPlayers()
            1 -> return FragTeamGames()
        }
        return FragTeamStats()
    }
}