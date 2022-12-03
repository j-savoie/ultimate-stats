package ca.unb.mobiledev.ultimatestattracker.model

import android.content.Context
import ca.unb.mobiledev.ultimatestattracker.helper.FileUtils.*
import ca.unb.mobiledev.ultimatestattracker.helper.JsonUtils
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Game : java.io.Serializable {
    var myTeam: Team
    var oppTeamName: String
    var myTeamScore: Int
    var oppTeamScore: Int
    var scoreLimit: Int
    var htMins: Int
    var toMins: Int
    var toLimit: Int
    var events: ArrayList<Event>
    var createdAt: String

    constructor(myTeam: Team, oppTeamName: String, myTeamScore: Int, oppTeamScore: Int, scoreLimit: Int, htMins: Int, toMins: Int, toLimit: Int, events: ArrayList<Event>) {
        this.myTeam = myTeam
        this.oppTeamName = oppTeamName
        this.myTeamScore = myTeamScore
        this.oppTeamScore = oppTeamScore
        this.scoreLimit = scoreLimit
        this.htMins = htMins
        this.toMins = toMins
        this.toLimit = toLimit
        this.events = events
        this.createdAt = getFormattedTime()
    }

    fun addEvent(event: Event) {
        this.events.add(event)
    }

    fun removeEvent(event: Event) {
        this.events.remove(event)
    }

    fun refreshTeam(c : Context) {
        var teamName = myTeam.teamName
        var dir = getTeamDir(teamName, c)
        val fileName = "${toFileName(teamName)}.json"
        val file = File(dir, fileName)
        val jsonStr = file.readText()
        val json = JsonUtils()
        val team : Team = json.convertJsonToObject(jsonStr, Team::class.java) as Team
        myTeam = team
    }

    fun save(c : Context) {
        val json = JsonUtils()
        val gameDir = getGameDir(myTeam.teamName, c)
        val fileName = "game_${toFileName(oppTeamName)}_${createdAt}.json"
        val file = File(gameDir, fileName)
        val jsonStr = json.convertObjectToJson(this)
        if (jsonStr != null)
            file.writeText(jsonStr)
    }

    private fun getFormattedTime() : String {
        var time = LocalDateTime.now()
        var formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm")
        var formatted = time.format(formatter)
        return formatted
    }

    override fun toString() : String{
        return "Game: ${myTeam.teamName} vs ${oppTeamName} - Score: ${myTeamScore} - ${oppTeamScore}"
    }
}