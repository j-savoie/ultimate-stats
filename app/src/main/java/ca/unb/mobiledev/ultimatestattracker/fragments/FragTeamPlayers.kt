package ca.unb.mobiledev.ultimatestattracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.unb.mobiledev.ultimatestattracker.databinding.FragmentTeamPlayersBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [FragTeamPlayers.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragTeamPlayers : Fragment() {
    // TODO: Rename and change types of parameters
    private var team: String? = null //:Team
    private var _binding: FragmentTeamPlayersBinding? = null
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
        _binding = FragmentTeamPlayersBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
}