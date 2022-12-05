package ca.unb.mobiledev.ultimatestattracker.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.ultimatestattracker.R
import ca.unb.mobiledev.ultimatestattracker.helper.FileUtils
import ca.unb.mobiledev.ultimatestattracker.helper.FileUtils.getGameDir
import ca.unb.mobiledev.ultimatestattracker.model.Game
import ca.unb.mobiledev.ultimatestattracker.model.Team
import com.google.android.material.chip.Chip
import java.io.File
import java.util.*

/**
 * A [RecyclerView.Adapter] for the ViewTeams activity.
 * This adapter inflates the `partial_team.xml` layout for each team.
 */
class GameAdapter(
    private val parentActivity: Context,
    private var dataset: ArrayList<Game>,
) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {
    class ViewHolder(v: LinearLayout) : RecyclerView.ViewHolder(v) {
        var textGameName: TextView = v.findViewById(R.id.textGameName)
        var textGameDate: TextView = v.findViewById(R.id.textGameDate)
        var chipScore: Chip = v.findViewById(R.id.chipGameScore)
        var btnDelete: ImageButton = v.findViewById(R.id.btnDeleteGame)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.partial_game, parent, false) as LinearLayout
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataset[position]
        holder.textGameName.text = "${item.myTeam.teamName} vs ${item.oppTeamName}"
        holder.textGameDate.text = item.createdAt
        holder.chipScore.text = "${item.myTeamScore} - ${item.oppTeamScore}"
        when(item.myTeamScore - item.oppTeamScore){
            0 -> holder.chipScore.setChipBackgroundColorResource(R.color.yellow)
            in 1..Int.MAX_VALUE -> holder.chipScore.setChipBackgroundColorResource(R.color.green)
            in Int.MIN_VALUE..-1 -> holder.chipScore.setChipBackgroundColorResource(R.color.red)
        }
        holder.textGameName.setOnClickListener {
//            val intent = Intent(parentActivity, ViewTeam::class.java)
//            intent.putExtra("team", item)
//            parentActivity.startActivity(intent)
            Toast.makeText(parentActivity, "Not implemented yet", Toast.LENGTH_SHORT).show()
        }
        holder.btnDelete.setOnClickListener {
            val dir = getGameDir(item.myTeam.teamName, parentActivity)
            for (gameFile in dir.listFiles()!!) {
                if (gameFile.name.contains(item.createdAt)) {
                    gameFile.delete()
                    break
                }
            }
            dataset = dataset.filter { it.createdAt != item.createdAt } as ArrayList<Game>
            notifyDataSetChanged()
        }
    }

    fun setGames(games: ArrayList<Game>) {
        dataset = games
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun getGames(): ArrayList<Game>? {
        return dataset as ArrayList<Game>
    }
}