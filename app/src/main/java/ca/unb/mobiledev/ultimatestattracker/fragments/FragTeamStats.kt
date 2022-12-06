package ca.unb.mobiledev.ultimatestattracker.fragments

import android.app.DownloadManager
import android.app.DownloadManager.ACTION_VIEW_DOWNLOADS
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import ca.unb.mobiledev.ultimatestattracker.databinding.FragmentTeamStatsBinding
import ca.unb.mobiledev.ultimatestattracker.helper.FileUtils.getGamesFromFileSystem
import ca.unb.mobiledev.ultimatestattracker.model.Event
import ca.unb.mobiledev.ultimatestattracker.model.Game
import ca.unb.mobiledev.ultimatestattracker.model.PlayerStats
import ca.unb.mobiledev.ultimatestattracker.model.Team
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

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
            // Parse the games into an array of stats
            var arr = parseGames(team)
            // Write the stats to csv file
            val file : File = writeStatsToCSV(arr)

            // Start intent to open csv file
            // Cannot be done, sheets/excel does not support csv files for some reason
//            val intent = Intent(Intent.ACTION_VIEW)
//            val fileUri = FileProvider.getUriForFile(requireContext(), "ca.unb.mobiledev.ultimatestattracker.fileprovider", file)
//            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.setDataAndType(fileUri, "text/*")

            // Start intent to view downloads folder
            val intent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
            startActivity(intent)
            // Check if there is an app that can handle the intent
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "No app found to open file", Toast.LENGTH_SHORT).show()
            }
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
        var games : ArrayList<Game>
        Executors.newSingleThreadExecutor().execute {
            val mainHandler = Handler(Looper.getMainLooper())
            games = getGamesFromFileSystem(team.teamName, requireContext())

            for(game in games){
                // Get all events from game
                for(event in game.events){

                    // Parse the event and increment the appropriate stats
                    when(event.eventType){
                        Event.EVENT_TYPE.Start -> {}
                        Event.EVENT_TYPE.Stop -> {}
                        Event.EVENT_TYPE.HTStart -> {}
                        Event.EVENT_TYPE.HTStop -> {}
                        Event.EVENT_TYPE.TOStart -> {}
                        Event.EVENT_TYPE.TOStop -> {}
                        Event.EVENT_TYPE.Substitution -> {}
                        Event.EVENT_TYPE.OppGoal -> {}
                        Event.EVENT_TYPE.Goal -> {
                            var player = event.player
                            var playerStats = playerStatsArray.find { it.player.number == player?.number }
                            playerStats?.goals = playerStats?.goals?.plus(1)!!
                            // If previous event was a pass, record assist
                            if(game.events[game.events.indexOf(event)-1].eventType == Event.EVENT_TYPE.Pass){
                                var assistPlayer = game.events[game.events.indexOf(event)-1].player
                                var assistPlayerStats = playerStatsArray.find { it.player.number == assistPlayer?.number }
                                assistPlayerStats?.assists = assistPlayerStats?.assists?.plus(1)!!
                            }
                        }
                        Event.EVENT_TYPE.Pass -> {
                            var player = event.player
                            var player2 = event.player2
                            var p1 = playerStatsArray.find { it.player.number == player?.number }
                            var p2 = playerStatsArray.find { it.player.number == player2?.number }
                            p1?.passes = p1?.passes?.plus(1)!!
                            p2?.passesReceived = p2?.passesReceived?.plus(1)!!
                        }
                        Event.EVENT_TYPE.Steal -> {
                            var player = event.player
                            var playerStats = playerStatsArray.find { it.player.number == player?.number }
                            playerStats?.steals = playerStats?.steals?.plus(1)!!
                        }
                        Event.EVENT_TYPE.Turnover -> {
                            var player = event.player
                            var playerStats = playerStatsArray.find { it.player.number == player?.number }
                            playerStats?.turnovers = playerStats?.turnovers?.plus(1)!!
                        }
                        Event.EVENT_TYPE.Foul -> {
                            var player = event.player
                            var playerStats = playerStatsArray.find { it.player.number == player?.number }
                            playerStats?.fouls = playerStats?.fouls?.plus(1)!!
                        }
                        else -> "Unknown Event"
                    }
                }
            }
        } // end of thread
        return playerStatsArray
    }

    private fun writeStatsToCSV(arr: ArrayList<PlayerStats>) : File {
        val dlFolder : File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        val file = File(dlFolder, "${team.teamName}_stats.csv")
        val writer = BufferedWriter(FileWriter(file))

        val csvPrinter = CSVPrinter(writer, CSVFormat.DEFAULT
            .withHeader("Player", "Goals", "Assists", "Passes", "Passes Received", "Steals", "Turnovers", "Fouls"))
        for(playerStats in arr){
            val playerData = Arrays.asList(
                playerStats.player.getFormattedName(),
                playerStats.goals,
                playerStats.assists,
                playerStats.passes,
                playerStats.passesReceived,
                playerStats.steals,
                playerStats.turnovers,
                playerStats.fouls,
            )
            csvPrinter.printRecord(playerData)
        }
        csvPrinter.flush()
        csvPrinter.close()
        return file
    }
}