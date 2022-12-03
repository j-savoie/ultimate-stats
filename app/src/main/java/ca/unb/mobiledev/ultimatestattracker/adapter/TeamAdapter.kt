package ca.unb.mobiledev.ultimatestattracker.adapter

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import ca.unb.mobiledev.ultimatestattracker.R
import ca.unb.mobiledev.ultimatestattracker.ViewTeam
import ca.unb.mobiledev.ultimatestattracker.helper.FileUtils.getTeamDir
import ca.unb.mobiledev.ultimatestattracker.model.Team

/**
 * A [RecyclerView.Adapter] for the ViewTeams activity.
 * This adapter inflates the `partial_team.xml` layout for each team.
 */
class TeamAdapter(
    private val parentActivity: AppCompatActivity,
    private var dataset: ArrayList<Team>,
) : RecyclerView.Adapter<TeamAdapter.ViewHolder>() {
    class ViewHolder(v: LinearLayout) : RecyclerView.ViewHolder(v) {
        var imgLogo: ImageView = v.findViewById(R.id.imgTeamLogo)
        var textName: TextView = v.findViewById(R.id.textTeamName)
        var btnDelete: ImageButton = v.findViewById(R.id.btnDeleteTeam)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.partial_team, parent, false) as LinearLayout
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataset[position]
        holder.textName.text = item.teamName
        holder.textName.setOnClickListener {
            val intent = Intent(parentActivity, ViewTeam::class.java)
            intent.putExtra("team", item)
            parentActivity.startActivity(intent)
        }
        holder.btnDelete.setOnClickListener {
            val dir = getTeamDir(item.teamName, parentActivity)
            Log.d("TeamAdapter", "Deleting team ${item.teamName} at ${dir.absolutePath}")
            dir.deleteRecursively()
            dataset = dataset.filter { it.teamName != item.teamName } as ArrayList<Team>
            notifyDataSetChanged()
        }
    }

    fun setTeams(teams: ArrayList<Team>) {
        dataset = teams
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun getTeams(): ArrayList<Team>? {
        return dataset as ArrayList<Team>
    }
}