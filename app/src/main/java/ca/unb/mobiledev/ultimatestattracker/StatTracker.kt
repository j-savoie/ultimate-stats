package ca.unb.mobiledev.ultimatestattracker

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

class StatTracker : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stat_tracker)

    }
    val activePlayer = null
    val previousPlayers = null


}