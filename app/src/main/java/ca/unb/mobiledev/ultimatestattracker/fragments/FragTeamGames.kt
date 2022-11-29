package ca.unb.mobiledev.ultimatestattracker.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.unb.mobiledev.ultimatestattracker.StatTracker
import ca.unb.mobiledev.ultimatestattracker.databinding.FragmentTeamGamesBinding
import ca.unb.mobiledev.ultimatestattracker.databinding.FragmentTeamPlayersBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [FragTeamPlayers.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragTeamGames : Fragment() {
    // TODO: Rename and change types of parameters
    private var team: String? = null //:Team
    private var _binding: FragmentTeamGamesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            team = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTeamGamesBinding.inflate(inflater, container, false)
        val view = binding.root

        val newGame = binding.btnNewGame

        newGame.setOnClickListener{
            val intent = Intent(activity, StatTracker::class.java )
            startActivity(intent)
        }
        return view
    }
}