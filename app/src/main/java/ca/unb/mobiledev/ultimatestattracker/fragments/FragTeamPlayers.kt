package ca.unb.mobiledev.ultimatestattracker.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.ultimatestattracker.AddPlayers
import ca.unb.mobiledev.ultimatestattracker.R
import ca.unb.mobiledev.ultimatestattracker.adapter.PlayerAdapter
import ca.unb.mobiledev.ultimatestattracker.databinding.FragmentTeamPlayersBinding
import ca.unb.mobiledev.ultimatestattracker.model.Team

/**
 * A simple [Fragment] subclass.
 * Provides the view for ViewTeam's Players tab.
 */
class FragTeamPlayers : Fragment() {
    private var _binding: FragmentTeamPlayersBinding? = null
    private val binding get() = _binding!!
    private lateinit var team: Team
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlayerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get the team from the bundle
        arguments?.let {
            team = it.getSerializable("team") as Team
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTeamPlayersBinding.inflate(inflater, container, false)
        val view = binding.root
        // Set up the recycler view
        recyclerView = view.findViewById(R.id.recyclerPlayers)
        adapter = PlayerAdapter(view.context, team)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = layoutManager
        adapter.setTeam(team)
        recyclerView.adapter = adapter

        val fab = binding.fabAddPlayer
        fab.setOnClickListener{
            val intent = Intent(activity, AddPlayers::class.java)
            intent.putExtra("team", team)
            startActivityForResult(intent, 1)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                team = data?.getSerializableExtra("team") as Team
                adapter.setTeam(team)
                recyclerView.adapter = adapter
            }
        }
    }
}