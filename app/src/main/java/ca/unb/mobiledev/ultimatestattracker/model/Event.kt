package ca.unb.mobiledev.ultimatestattracker.model

class Event : java.io.Serializable {

    var eventType: EVENT_TYPE
    var player: Player
    var player2: Player
    var time: Number

    constructor(eventType: EVENT_TYPE, player: Player, player2: Player, time: Number) {
        this.eventType = eventType
        this.player = player
        this.player2 = player2
        this.time = time
    }

    enum class EVENT_TYPE {
        Start, Stop, HTStart, HTStop, TOStart, TOStop, Goal, Pass, Steal, OppGoal, Turnover, Foul, Injury
    }

}