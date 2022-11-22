package ca.unb.mobiledev.ultimatestattracker.model

class Game : java.io.Serializable {
    var myTeam: Team
    var oppTeamName: String
    var myTeamScore: Number
    var oppTeamScore: Number
    var scoreLimit: Number
    var htMins: Number
    var toMins: Number
    var toLimit: Number
    var events: ArrayList<Event>

    constructor(myTeam: Team, oppTeamName: String, myTeamScore: Number, oppTeamScore: Number, scoreLimit: Number, htMins: Number, toMins: Number, toLimit: Number, events: ArrayList<Event>) {
        this.myTeam = myTeam
        this.oppTeamName = oppTeamName
        this.myTeamScore = myTeamScore
        this.oppTeamScore = oppTeamScore
        this.scoreLimit = scoreLimit
        this.htMins = htMins
        this.toMins = toMins
        this.toLimit = toLimit
        this.events = events
    }
}