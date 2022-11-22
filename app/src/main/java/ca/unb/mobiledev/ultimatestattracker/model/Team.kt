package ca.unb.mobiledev.ultimatestattracker.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import ca.unb.mobiledev.ultimatestattracker.helper.JsonUtils
import java.io.File

class Team : java.io.Serializable {
    var teamName: String
    var players: ArrayList<Player>

    constructor(name: String, players: ArrayList<Player>) {
        this.teamName = name
        this.players = players
    }

    fun addPlayer(player: Player) {
        this.players.add(player)
    }

    fun removePlayer(player: Player) {
        this.players.remove(player)
    }

    fun save(c : Context) {
        val json = JsonUtils()
        val dir = c.getDir("teams", MODE_PRIVATE)
        val fileName = "${teamName}.json"
        val file = File(dir, fileName)
        val jsonStr = json.convertObjectToJson(this)
        if (jsonStr != null)
            file.writeText(jsonStr)
    }

    override fun toString() : String {
        return "Team Name: ${this.teamName} \nPlayers: ${this.players}"
    }
}