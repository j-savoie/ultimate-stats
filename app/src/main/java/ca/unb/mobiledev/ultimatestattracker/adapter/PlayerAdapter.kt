package ca.unb.mobiledev.ultimatestattracker.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.Toast
import ca.unb.mobiledev.ultimatestattracker.R
import ca.unb.mobiledev.ultimatestattracker.model.Team

/**
 * A [RecyclerView.Adapter] for the ViewTeams activity.
 * This adapter inflates the `partial_player.xml` layout for each team.
 */
class PlayerAdapter(
    private val context: Context,
    private var dataset: Team,
) : RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {
    class ViewHolder(v: LinearLayout) : RecyclerView.ViewHolder(v) {
        var textNumber: TextView = v.findViewById(R.id.textPlayerNumber)
        var textName: TextView = v.findViewById(R.id.textPlayerName)
        var btnDelete: ImageButton = v.findViewById(R.id.btnDeletePlayer)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.partial_player, parent, false) as LinearLayout
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataset.players[position]
        holder.textNumber.text = item.number.toString()
        holder.textName.text = "${item.fName} ${item.lName}"
        holder.textName.setOnClickListener {
//            val intent = Intent(parentActivity, ViewPlayer::class.java)
//            intent.putExtra("team", item)
//            parentActivity.startActivity(intent)
            Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show()
        }
        holder.btnDelete.setOnClickListener {
            dataset.removePlayer(item)
            dataset.save(context)
            notifyDataSetChanged()
        }
    }

    fun setTeam(team: Team) {
        dataset = team
    }

    override fun getItemCount(): Int {
        return dataset.players.size
    }
}