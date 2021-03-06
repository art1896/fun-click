package com.example.kot.activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.kot.R
import com.example.kot.activity.MainActivity.Companion.dbReference
import com.example.kot.model.Record
import com.example.kot.model.TopUser
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_play.*


@Suppress("DEPRECATION")
class PlayActivity : AppCompatActivity() {

    companion object {
        var score: Int = 0
        var total_score = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

    }

    override fun onStart() {
        super.onStart()
        score = 0
    }

    override fun onBackPressed() {
    }

    fun count(view: View) {

        val mp: MediaPlayer = MediaPlayer.create(
            this,
            R.raw.u_click
        )
        if (!MainActivity.mute) {
            mp.start()
        }
        if (score == 0) {
            val timer = object : CountDownTimer((MainActivity.duration * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    countdown.text = (millisUntilFinished / 1000).toString()
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFinish() {
                    click_button.isClickable = false
                    showDialog()
                    total_score =
                        score
                    score = 0
                }
            }
            timer.start()
        }

        score++
        if (MainActivity.isRandom) {
            randomPos()
            score_play.text = score.toString()
        } else {
            score_play.text = score.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showDialog() {

        val dialog = Dialog(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
        val txtView: TextView = dialogView.findViewById(R.id.dialog_message)
        txtView.text = score.toString()
        dialog.setContentView(dialogView)
        dialog.setCancelable(false)
        val btnOk: Button = dialogView.findViewById(R.id.btn_action)
        btnOk.setOnClickListener {
            finish()
        }
        val btnSave: Button = dialogView.findViewById(R.id.btn_action1)
        btnSave.setOnClickListener {
            initList()
            saveRecords()
            finish()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initList() {
        val score = total_score
        if (MainActivity.list == null) {
            MainActivity.list = ArrayList()
        }
        MainActivity.list!!.add(Record(score))
    }

    private fun randomPos() {
        val displaymetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displaymetrics)

        val x = displaymetrics.widthPixels
        val y = displaymetrics.heightPixels
        val btnx = click_button.width
        val btny = click_button.height

        val a = btnx..(x - (2 * btnx))
        val b = btny..(y - (2 * btny))


        val dx = a.random().toFloat()
        val dy = b.random().toFloat()

        click_button.animate()
            .x(dx)
            .y(dy)
            .setDuration(0)
            .start()
    }

    private fun saveRecords() {
        val sharedPreferences = getSharedPreferences("records", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(MainActivity.list)
        editor.putString("recordsList", json)
        editor.apply()


        val max = MainActivity.list!!.maxBy { record -> record.score }!!.score
        val user = TopUser(
            LoginActivity.user!!.displayName!!,
            max,
            LoginActivity.user!!.photoUrl!!.toString(),
            MainActivity.userId.toLong()
        )
        dbReference.child(MainActivity.userId).setValue(user)

    }

}
