package ca.unb.mobiledev.ultimatestattracker.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.ultimatestattracker.CreateGame
import ca.unb.mobiledev.ultimatestattracker.R
import ca.unb.mobiledev.ultimatestattracker.StatTracker
import ca.unb.mobiledev.ultimatestattracker.adapter.GameAdapter
import ca.unb.mobiledev.ultimatestattracker.adapter.PlayerAdapter
import ca.unb.mobiledev.ultimatestattracker.databinding.FragmentTeamGamesBinding
import ca.unb.mobiledev.ultimatestattracker.databinding.FragmentTeamPlayersBinding
import ca.unb.mobiledev.ultimatestattracker.helper.FileUtils.getGamesFromFileSystem
import ca.unb.mobiledev.ultimatestattracker.model.Game
import ca.unb.mobiledev.ultimatestattracker.model.Team

/**
 * A simple [Fragment] subclass.
 * Provide the view for the ViewTeam's Games tab.
 */
class FragTeamGames : Fragment() {
    private var _binding: FragmentTeamGamesBinding? = null
    private val binding get() = _binding!!
    private lateinit var games: ArrayList<Game>
    private lateinit var team: Team
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            games = it.getSerializable("games") as ArrayList<Game>
            team = it.getSerializable("team") as Team
        }
        Log.d("FragTeamGames", "games: $games")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTeamGamesBinding.inflate(inflater, container, false)
        val view = binding.root

        // Set up the recycler view
        recyclerView = view.findViewById(R.id.recyclerGames)
        adapter = GameAdapter(view.context, games)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = layoutManager
        adapter.setGames(games)
        recyclerView.adapter = adapter

        val btnNewGame = binding.btnNewGame
        btnNewGame.setOnClickListener{
            val intent = Intent(activity, CreateGame::class.java )
            intent.putExtra("team", team)
            startActivity(intent)
        }

        return view
    }
}