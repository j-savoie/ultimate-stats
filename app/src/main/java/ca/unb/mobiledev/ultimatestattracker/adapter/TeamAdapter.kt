package ca.unb.mobiledev.ultimatestattracker.adapter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageButton
import ca.unb.mobiledev.ultimatestattracker.MainActivity
import ca.unb.mobiledev.ultimatestattracker.R
import ca.unb.mobiledev.ultimatestattracker.ViewTeam
import ca.unb.mobiledev.ultimatestattracker.model.Team

class TeamAdapter(
    private val parentActivity: AppCompatActivity,
    private var dataset: List<Team>,
) : RecyclerView.Adapter<TeamAdapter.ViewHolder>() {
    class ViewHolder(v: LinearLayout) : RecyclerView.ViewHolder(v) {
        var textNumber: TextView = v.findViewById(R.id.text_number)
        var textName: TextView = v.findViewById(R.id.text_name)
        var btnDelete: ImageButton = v.findViewById(R.id.btnDelete)
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
        val item = dataset[position]
        holder.textName.text = item.teamName
        holder.textNumber.text = position.toString()
        holder.textName.setOnClickListener {
            val intent = Intent(parentActivity, ViewTeam::class.java)
            intent.putExtra("team", item)
            parentActivity.startActivity(intent)
        }
        holder.btnDelete.setOnClickListener {
            val dir = parentActivity.getDir("teams", Context.MODE_PRIVATE)
            val file = dir.resolve(item.teamName + ".json")
            file.delete()
            dataset = dataset.filter { it.teamName != item.teamName }
            notifyDataSetChanged()
        }
    }

    fun setTeams(teams: List<Team>) {
        dataset = teams
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}