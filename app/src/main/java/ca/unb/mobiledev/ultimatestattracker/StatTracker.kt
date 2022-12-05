package ca.unb.mobiledev.ultimatestattracker

import FragDialogSetLine
import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import ca.unb.mobiledev.ultimatestattracker.model.Event
import ca.unb.mobiledev.ultimatestattracker.model.Game
import ca.unb.mobiledev.ultimatestattracker.model.Player
import org.w3c.dom.Text
import java.util.ArrayList
//TODO:JACOB,FIX:::::: DISABLE BUTTONS BASED ON OFFENSE/DEFENCE
class StatTracker : AppCompatActivity(), FragDialogSetLine.DialogListener {
    var activePlayer : Player? = null
    var previousPlayer : Player? = null
    var timeoutVal = 0
    var halftime = 1
    lateinit var game : Game
    var stolen = false
    var fouled = false
    var activeLine = mapOf<Int, Player?>(
        R.id.player1 to null,
        R.id.player2 to null,
        R.id.player3 to null,
        R.id.player4 to null,
        R.id.player5 to null,
        R.id.player6 to null,
        R.id.player7 to null,
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stat_tracker)
        //get game from intent
        game = intent.getSerializableExtra("game") as Game

        showSelectLineDialog()

        timeoutVal = game.toLimit
        //get player list
        val timerText = findViewById<TextView>(R.id.mainTimer)
        val myteam =  findViewById<TextView>(R.id.myTeamName)
        val opTeam =  findViewById<TextView>(R.id.otherTeamName)
        var playerRadio = findViewById<RadioGroup>(R.id.player_radio)
        val myTeamScoreView = findViewById<TextView>(R.id.myTeamScore)
        val opTeamScoreView = findViewById<TextView>(R.id.otherTeamScore)
        var timerButton = findViewById<ToggleButton>(R.id.Start_Stop)
        var halftimeButton = findViewById<Button>(R.id.halftime)
        var timeoutButton = findViewById<Button>(R.id.timeout)
        val secondaryTimerText = findViewById<TextView>(R.id.secondaryTimer)
        val goalButton = findViewById<Button>(R.id.goal)
        val opGoal = findViewById<Button>(R.id.opGoal)
        val turnButton = findViewById<Button>(R.id.turnover)
        val stealButton = findViewById<Button>(R.id.steal)
        val foulButton = findViewById<Button>(R.id.foul)

        timerText.text = "90:00"
        timerButton.isEnabled = true
        var timer = object: CountDownTimer(5400000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                var minutes = millisUntilFinished/60000
                var seconds =  millisUntilFinished % 60000 /1000
                if(seconds < 10){
                    timerText.text = "" + minutes + ":0" + seconds
                }
                else {
                    timerText.text = "" + minutes + ":" + seconds
                }
            }

            override fun onFinish() {
                timerText.text = "time expired"
            }
        }

        //on change create pass event and set active player
        playerRadio.setOnCheckedChangeListener{ _, checkId ->
            previousPlayer = activePlayer
            activePlayer = activeLine[checkId]
            if(stolen){
                var stealEvent = Event(Event.EVENT_TYPE.Steal, activePlayer, null, timerText.text.toString())
                game.addEvent(stealEvent)
                stolen = false
            }
            if(fouled){
                var foulEvent = Event(Event.EVENT_TYPE.Foul, activePlayer, null, timerText.text.toString())
                game.addEvent(foulEvent)
                fouled = false
                playerRadio.clearCheck()
                setIsEnabled(playerRadio, false)
            }
            else {
                var passEvent = Event(
                    Event.EVENT_TYPE.Pass,
                    activePlayer,
                    previousPlayer,
                    timerText.text.toString()
                )
                game.addEvent(passEvent)
            }
        }
        //set text view for team names
        myteam.setText(game.myTeam.teamName)
        opTeam.setText(game.oppTeamName)
        //set scores for start of game
        myTeamScoreView.text = "" +game.myTeamScore
        opTeamScoreView.text = "" + game.oppTeamScore
        //Start and end game button
        timerButton.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked) {
                timer.start()
                setIsEnabled(playerRadio, true)
                halftimeButton.isEnabled = true
                timeoutButton.isEnabled = true
                opGoal.isEnabled = true
                turnButton.isEnabled = true
                foulButton.isEnabled = true
                goalButton.isEnabled = true
                stealButton.isEnabled = true
            }
            else{
                timer.cancel()
                timerButton.text = "Game Over"
                timerButton.isEnabled = false
                setIsEnabled(playerRadio, false)
                halftimeButton.isEnabled = false
                timeoutButton.isEnabled = false
                opGoal.isEnabled = false
                turnButton.isEnabled = false
                foulButton.isEnabled = false
                goalButton.isEnabled = false
                stealButton.isEnabled = false
                game.save(applicationContext)

            }
        }

        //halftime Button
        secondaryTimerText.text = "0:00"
        halftimeButton.setOnClickListener{
            var htEvent = Event(Event.EVENT_TYPE.HTStart, null,null, timerText.text.toString())
            game.addEvent(htEvent)
            halftimeButton.isEnabled = false
            timeoutButton.isEnabled = false
            var htTimer = object: CountDownTimer((game.htMins*60000).toLong(), 1000){
                override fun onTick(millisUntilFinished: Long) {
                    var minutes = millisUntilFinished/60000
                    var seconds =  millisUntilFinished % 60000 /1000
                    if(seconds < 10){
                        secondaryTimerText.text = "" + minutes + ":0" + seconds
                    }
                    else {
                        secondaryTimerText.text = "" + minutes + ":" + seconds
                    }
                }

                override fun onFinish() {
                    secondaryTimerText.text = "0:00"
                    halftime--
                    if(halftime != 0){
                        halftimeButton.isEnabled = true
                    }
                    if(timeoutVal != 0){
                        timeoutButton.isEnabled = true
                    }
                    timeoutVal = game.toLimit
                    timeoutButton.isEnabled = true
                    var htendEvent = Event(Event.EVENT_TYPE.HTStop, null,null, timerText.text.toString())
                    game.addEvent(htendEvent)
                    showSelectLineDialog()
                    setIsEnabled(playerRadio, true)
                    goalButton.isEnabled = true
                    stealButton.isEnabled = true
                    turnButton.isEnabled = true
                    timeoutButton.isEnabled = true
                    foulButton.isEnabled = true
                    opGoal.isEnabled = true
                }
            }
            htTimer.start()
        }
        //Timeout button logic
        timeoutButton.setOnClickListener{
            var toEvent = Event(Event.EVENT_TYPE.TOStart, null,null, timerText.text.toString())
            game.addEvent(toEvent)
            halftimeButton.isEnabled = false
            timeoutButton.isEnabled = false
            var toTimer = object: CountDownTimer((game.toMins*60000).toLong(), 1000){
                override fun onTick(millisUntilFinished: Long) {
                    var minutes = millisUntilFinished/60000
                    var seconds =  millisUntilFinished % 60000 /1000
                    if(seconds < 10){
                        secondaryTimerText.text = "" + minutes + ":0" + seconds
                    }
                    else {
                        secondaryTimerText.text = "" + minutes + ":" + seconds
                    }
                }

                override fun onFinish() {
                    secondaryTimerText.text = "0:00"
                    timeoutVal--
                    if(halftime != 0){
                        halftimeButton.isEnabled = true
                    }
                    if(timeoutVal != 0){
                        timeoutButton.isEnabled = true
                    }
                    var toendEvent = Event(Event.EVENT_TYPE.TOStop, null,null, timerText.text.toString())
                    game.addEvent(toendEvent)

                }
            }
            toTimer.start()
        }


        goalButton.setOnClickListener{
            var goalEvent = Event(Event.EVENT_TYPE.Goal, activePlayer, previousPlayer,  timerText.text.toString())
            game.addEvent(goalEvent)
            game.myTeamScore += 1
            playerRadio.clearCheck()
            setIsEnabled(playerRadio, false)
            myTeamScoreView.text = "" + game.myTeamScore
            goalButton.isEnabled = false
            stealButton.isEnabled = true
            timeoutButton.isEnabled = false
            foulButton.isEnabled = true
            opGoal.isEnabled = true
            turnButton.isEnabled = false
            if(game.myTeamScore == game.scoreLimit){
                timerButton.performClick()
            }
            else{
                showSelectLineDialog()
            }

        }

        opGoal.setOnClickListener{
            var goalEvent  = Event(Event.EVENT_TYPE.OppGoal, null, null, timerText.text.toString())
            game.addEvent(goalEvent)
            game.oppTeamScore += 1
            setIsEnabled(playerRadio, true)
            goalButton.isEnabled = true
            stealButton.isEnabled = false
            turnButton.isEnabled = true
            timeoutButton.isEnabled = true
            foulButton.isEnabled = false
            opGoal.isEnabled = false
            opTeamScoreView.text = "" + game.oppTeamScore
            setPlayerNull()
            if(game.oppTeamScore == game.scoreLimit){
                timerButton.performClick()
            }
            else{
                showSelectLineDialog()
            }
        }

        turnButton.setOnClickListener{
            var turnEvent = Event(Event.EVENT_TYPE.Turnover, activePlayer, previousPlayer, timerText.text.toString())
            game.addEvent(turnEvent)
            playerRadio.clearCheck()
            setIsEnabled(playerRadio, false)
            goalButton.isEnabled = false
            timeoutButton.isEnabled = false
            turnButton.isEnabled = false
            foulButton.isEnabled = true
            stealButton.isEnabled = true
            opGoal.isEnabled = true
            setPlayerNull()
        }
        stealButton.setOnClickListener{
            setIsEnabled(playerRadio, true)
            goalButton.isEnabled = true
            timeoutButton.isEnabled = true
            foulButton.isEnabled = false
            turnButton.isEnabled = true
            stealButton.isEnabled = false
            opGoal.isEnabled = false
            stolen = true

        }

        foulButton.setOnClickListener{
            setIsEnabled(playerRadio, true)
            fouled = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        game.save(applicationContext)
    }
    fun setIsEnabled(group: RadioGroup, bool : Boolean){
        for(i in 0 until group.childCount) {
            group.getChildAt(i).isEnabled = bool
        }
    }
    fun setPlayerNull(){
        activePlayer = null
        previousPlayer = null
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, selectedPlayers: ArrayList<Player>) {
        Log.d("StatTracker", "Selected Players: $selectedPlayers")
        setLine(selectedPlayers)
    }

    private fun showSelectLineDialog() {
        val lineupDialog = FragDialogSetLine(game.myTeam)
        lineupDialog.isCancelable = false
        lineupDialog.show(supportFragmentManager, "lineupDialog")
        lineupDialog.positiveButton?.isEnabled = false
    }
    fun setLine(selectedPlayers: ArrayList<Player>){
        activeLine = mapOf<Int, Player?>(
            R.id.player1 to selectedPlayers[0],
            R.id.player2 to selectedPlayers[1],
            R.id.player3 to selectedPlayers[2],
            R.id.player4 to selectedPlayers[3],
            R.id.player5 to selectedPlayers[4],
            R.id.player6 to selectedPlayers[5],
            R.id.player7 to selectedPlayers[6],
        )
        var player1 = findViewById<RadioButton>(R.id.player1)
        player1.text = activeLine[R.id.player1]?.getFormattedName() ?: "No Name"
        var player2 = findViewById<RadioButton>(R.id.player2)
        player2.text = activeLine[R.id.player2]?.getFormattedName() ?: "No Name"
        var player3 = findViewById<RadioButton>(R.id.player3)
        player3.text = activeLine[R.id.player3]?.getFormattedName() ?: "No Name"
        var player4 = findViewById<RadioButton>(R.id.player4)
        player4.text = activeLine[R.id.player4]?.getFormattedName() ?: "No Name"
        var player5 = findViewById<RadioButton>(R.id.player5)
        player5.text = activeLine[R.id.player5]?.getFormattedName() ?: "No Name"
        var player6 = findViewById<RadioButton>(R.id.player6)
        player6.text = activeLine[R.id.player6]?.getFormattedName() ?: "No Name"
        var player7 = findViewById<RadioButton>(R.id.player7)
        player7.text = activeLine[R.id.player7]?.getFormattedName() ?: "No Name"
    }
}