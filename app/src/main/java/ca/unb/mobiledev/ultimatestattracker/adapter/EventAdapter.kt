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
import ca.unb.mobiledev.ultimatestattracker.model.Event
import ca.unb.mobiledev.ultimatestattracker.model.Game
import ca.unb.mobiledev.ultimatestattracker.model.Player
import ca.unb.mobiledev.ultimatestattracker.model.Team
import com.google.android.material.chip.Chip
import java.io.File
import java.util.*

/**
 * A [RecyclerView.Adapter] for the ViewTeams activity.
 * This adapter inflates the `partial_team.xml` layout for each team.
 */
class EventAdapter(
    private val parentActivity: Context,
    private var dataset: Game
) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    class ViewHolder(v: LinearLayout) : RecyclerView.ViewHolder(v) {
        var textEventText: TextView = v.findViewById(R.id.textEventText)
        var chipEventType: Chip = v.findViewById(R.id.chipEventType)
        var btnDelete: ImageButton = v.findViewById(R.id.btnDeleteEvent)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.partial_event, parent, false) as LinearLayout
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataset.events[position]
        holder.textEventText.text = item.toString()
        holder.chipEventType.text = item.eventType.toString()
        when(item.eventType){
            Event.EVENT_TYPE.Start, Event.EVENT_TYPE.Stop, Event.EVENT_TYPE.HTStart, Event.EVENT_TYPE.HTStop, Event.EVENT_TYPE.TOStart, Event.EVENT_TYPE.TOStop
                -> holder.chipEventType.setChipBackgroundColorResource(R.color.yellow)
            Event.EVENT_TYPE.Goal, Event.EVENT_TYPE.Pass, Event.EVENT_TYPE.Steal, Event.EVENT_TYPE.Injury
                -> holder.chipEventType.setChipBackgroundColorResource(R.color.green)
            Event.EVENT_TYPE.OppGoal, Event.EVENT_TYPE.Foul, Event.EVENT_TYPE.Turnover
                -> holder.chipEventType.setChipBackgroundColorResource(R.color.red)
        }
        holder.textEventText.setOnClickListener {
            Toast.makeText(parentActivity, "Not implemented yet", Toast.LENGTH_SHORT).show()
        }
        holder.btnDelete.setOnClickListener {
            dataset.removeEvent(item)
            dataset.events = dataset.events.filter{it != item} as ArrayList<Event>
            notifyDataSetChanged()
        }
    }

    fun setEvents(game: Game) {
        dataset = game
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataset.events.size
    }

    fun getEvents(): ArrayList<Event> {
        return dataset.events
    }
}