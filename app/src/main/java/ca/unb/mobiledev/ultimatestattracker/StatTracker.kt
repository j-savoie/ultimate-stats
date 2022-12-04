package ca.unb.mobiledev.ultimatestattracker

import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ca.unb.mobiledev.ultimatestattracker.model.Event
import ca.unb.mobiledev.ultimatestattracker.model.Game
import ca.unb.mobiledev.ultimatestattracker.model.Player
import org.w3c.dom.Text

class StatTracker : AppCompatActivity() {
    var activePlayer : Player? = null
    var previousPlayer : Player? = null
    var timeoutVal = 0
    var halftime = 1
    lateinit var game : Game
    val activeLine = mapOf<Int, Player?>(
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
        timeoutVal = game.toLimit
        //get player list
        val timerText = findViewById<TextView>(R.id.mainTimer)
        timerText.text = "90:00"
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
        var playerRadio = findViewById<RadioGroup>(R.id.player_radio)
        //on change create pass event and set active player
        playerRadio.setOnCheckedChangeListener{ _, checkId ->
            previousPlayer = activePlayer
            activePlayer = activeLine[checkId]
            var passEvent = Event(Event.EVENT_TYPE.Pass,activePlayer, previousPlayer,timerText.text.toString())
            game.addEvent(passEvent)
        }
        //set text view for team names
        val myteam =  findViewById<TextView>(R.id.myTeamName)
        val opTeam =  findViewById<TextView>(R.id.otherTeamName)
        myteam.setText(game.myTeam.teamName)
        opTeam.setText(game.oppTeamName)
        //set scores for start of game
        val myTeamScoreView = findViewById<TextView>(R.id.myTeamScore)
        val opTeamScoreView = findViewById<TextView>(R.id.otherTeamScore)
        myTeamScoreView.text = "" +game.myTeamScore
        opTeamScoreView.text = "" + game.oppTeamScore
        //Start and end game button
        var timerButton = findViewById<ToggleButton>(R.id.Start_Stop)
        timerButton.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked) {
                timer.start()
            }
            else{
                timer.cancel()
                game.save(applicationContext)
            }
        }

        //halftime Button
        var halftimeButton = findViewById<Button>(R.id.halftime)
        var timeoutButton = findViewById<Button>(R.id.timeout)
        val secondaryTimerText = findViewById<TextView>(R.id.secondaryTimer)
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

        val goalButton = findViewById<Button>(R.id.goal)
        goalButton.setOnClickListener{
            var goalEvent = Event(Event.EVENT_TYPE.Goal, activePlayer, previousPlayer,  timerText.text.toString())
            game.addEvent(goalEvent)
            game.myTeamScore += 1
            playerRadio.isEnabled = false
            myTeamScoreView.text = "" + game.myTeamScore
        }

        val opGoal = findViewById<Button>(R.id.opGoal)
        opGoal.setOnClickListener{
            var goalEvent  = Event(Event.EVENT_TYPE.OppGoal, null, null, timerText.text.toString())
            game.addEvent(goalEvent)
            game.oppTeamScore += 1
            playerRadio.isEnabled = true
            opTeamScoreView.text = "" + game.oppTeamScore
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        game.save(applicationContext)
    }
}