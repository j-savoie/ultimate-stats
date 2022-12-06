package ca.unb.mobiledev.ultimatestattracker.model

class PlayerStats(var player: Player) {
    //var gamesPlayed = 0
    var goals = 0
    var assists = 0
    var passes = 0
    var passesReceived = 0
    var turnovers = 0
    var steals = 0
    var fouls = 0
    // timeWithDisc?

    override fun toString(): String {
        return "${player.getFormattedName()}: [$goals, $assists, $passes, $passesReceived, $turnovers, $steals, $fouls]"
    }
}