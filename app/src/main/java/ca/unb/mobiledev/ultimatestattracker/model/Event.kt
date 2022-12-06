package ca.unb.mobiledev.ultimatestattracker.model

class Event : java.io.Serializable {

    var eventType: EVENT_TYPE
    var player: Player?
    var player2: Player?
    var time: String

    constructor(eventType: EVENT_TYPE, player: Player?, player2: Player?, time: String) {
        this.eventType = eventType
        this.player = player
        this.player2 = player2
        this.time = time
    }

    fun getFormattedName() : String {
        return "[${player?.number}]${player?.fName} ${player?.lName}"
    }

    enum class EVENT_TYPE {
        Start, Stop, HTStart, HTStop, TOStart, TOStop, Goal, Pass, Steal, OppGoal, Turnover, Foul, Substitution
    }

    override fun toString() : String{
        return when(eventType){
            EVENT_TYPE.Start -> "Game Started"
            EVENT_TYPE.Stop -> "Game Stopped"
            EVENT_TYPE.HTStart -> "Half Time Started"
            EVENT_TYPE.HTStop -> "Half Time Stopped"
            EVENT_TYPE.TOStart -> "Time Out Started"
            EVENT_TYPE.TOStop -> "Time Out Stopped"
            EVENT_TYPE.Goal -> "${player?.getFormattedName()} scored a goal"
            EVENT_TYPE.Pass -> "${player?.getFormattedName()} passed to ${player2?.getFormattedName()}"
            EVENT_TYPE.Steal -> "${player?.getFormattedName()} stole the disc"
            EVENT_TYPE.OppGoal -> "Opponent scored a goal"
            EVENT_TYPE.Turnover -> "${player?.getFormattedName()} turned the disc over"
            EVENT_TYPE.Foul -> "${player?.getFormattedName()} committed a foul"
            EVENT_TYPE.Substitution -> "Substitution"
            else -> "Unknown Event"
        }
    }

}