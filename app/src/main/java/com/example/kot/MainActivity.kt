package com.example.kot

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.shawnlin.numberpicker.NumberPicker
import io.ghyeok.stickyswitch.widget.StickySwitch


class MainActivity : AppCompatActivity() {

    companion object {
        var isRandom = false
        var duration = 10
        var mute = false
        var list: ArrayList<Record>? = null


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val numPicker: NumberPicker = findViewById(R.id.number_picker)
        numPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            duration = newVal
        }

        val muteSwitch: StickySwitch = findViewById(R.id.switch_volume)
        muteSwitch.onSelectedChangeListener = object : StickySwitch.OnSelectedChangeListener {
            override fun onSelectedChange(direction: StickySwitch.Direction, text: String) {
                mute = text != "On"
            }
        }

        val randomSwitch: StickySwitch = findViewById(R.id.switch1)
        randomSwitch.onSelectedChangeListener = object : StickySwitch.OnSelectedChangeListener {
            override fun onSelectedChange(direction: StickySwitch.Direction, text: String) {
                isRandom = text != "Fixed"
            }
        }
    }


    fun play(view: View) {
        val play = Intent(this, PlayActivity::class.java)
        startActivity(play)
    }

    override fun onResume() {
        super.onResume()
        val txt: TextView = findViewById(R.id.score)
        txt.text = PlayActivity.total_score.toString()


    }

    fun showRecords(view: View) {
        val intent = Intent(this, RecordsActivity::class.java)
        startActivity(intent)
    }




}


