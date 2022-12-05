package ca.unb.mobiledev.ultimatestattracker.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.unb.mobiledev.ultimatestattracker.databinding.FragmentTeamStatsBinding
import ca.unb.mobiledev.ultimatestattracker.helper.FileUtils.getGamesFromFileSystem
import ca.unb.mobiledev.ultimatestattracker.model.Event
import ca.unb.mobiledev.ultimatestattracker.model.PlayerStats
import ca.unb.mobiledev.ultimatestattracker.model.Team

/**
 * A simple [Fragment] subclass.
 * Provides the view for ViewTeam's Stats tab.
 */
class FragTeamStats : Fragment() {
    private var _binding: FragmentTeamStatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var team: Team

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            team = it.getSerializable("team") as Team
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTeamStatsBinding.inflate(inflater, container, false)
        val view = binding.root

        val progressBar = binding.progressBar
        val btnDownload = binding.btnDownload
        btnDownload.setOnClickListener{
            var arr = parseGames(team)
            Log.d("FragTeamStats", "arr: $arr")
        }

        return view
    }

    fun parseGames(team : Team) : ArrayList<PlayerStats> {
        // Construct Array of PlayerStats
        val playerStatsArray : ArrayList<PlayerStats> = ArrayList()
        for(player in team.players){
            val playerStats = PlayerStats(player)
            playerStatsArray.add(playerStats)
        }
        // Get all games from file system
        var games = getGamesFromFileSystem(team.teamName, requireContext())
        for(game in games){
            // Get all events from game
            for(event in game.events){
                // Parse the event and increment the appropriate stats
                when(event.eventType){
                    Event.EVENT_TYPE.Start -> break
                    Event.EVENT_TYPE.Stop -> break
                    Event.EVENT_TYPE.HTStart -> break
                    Event.EVENT_TYPE.HTStop -> break
                    Event.EVENT_TYPE.TOStart -> break
                    Event.EVENT_TYPE.TOStop -> break
                    Event.EVENT_TYPE.Goal -> {
                        var player = event.player
                        var playerStats = playerStatsArray.find { it.player == player }
                        playerStats?.goals = playerStats?.goals?.plus(1) ?: 0
                        Log.d("FragTeamStats", "Player ${player?.getFormattedName()} has ${playerStats?.goals} goals")
                    }
                    Event.EVENT_TYPE.Pass -> {
                        var player = event.player
                        var playerStats = playerStatsArray.find { it.player == player }
                        playerStats?.passes = playerStats?.passes?.plus(1) ?: 0
                        Log.d("FragTeamStats", "Player ${player?.getFormattedName()} has ${playerStats?.passes} passes")
                    }
                    Event.EVENT_TYPE.Steal -> {
                        break
                    }
                    Event.EVENT_TYPE.OppGoal -> break
                    Event.EVENT_TYPE.Turnover -> break
                    Event.EVENT_TYPE.Foul -> break
                    Event.EVENT_TYPE.Injury -> break
                    else -> "Unknown Event"
                }
            }
        }
        return playerStatsArray
    }
}