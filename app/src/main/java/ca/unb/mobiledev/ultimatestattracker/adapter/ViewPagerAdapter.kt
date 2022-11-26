package ca.unb.mobiledev.ultimatestattracker.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ca.unb.mobiledev.ultimatestattracker.fragments.FragTeamGames
import ca.unb.mobiledev.ultimatestattracker.fragments.FragTeamPlayers
import ca.unb.mobiledev.ultimatestattracker.fragments.FragTeamStats

private const val NUM_TABS = 3

/**
 * A [FragmentStateAdapter] for the ViewTeam activity.
 * This adapter provides the fragments for each tab.
 */
public class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, bundle: Bundle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    val bundle = bundle
    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        var frag : Fragment = FragTeamPlayers()
        when (position) {
            0 -> frag = FragTeamPlayers()
            1 -> frag = FragTeamGames()
            2 -> frag = FragTeamStats()
        }
        frag.arguments = bundle
        return frag
    }

}