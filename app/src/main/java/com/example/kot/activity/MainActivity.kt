package com.example.kot.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kot.App
import com.example.kot.R
import com.example.kot.activity.LoginActivity.Companion.user
import com.example.kot.adapter.TopRecordsAdapter
import com.example.kot.model.Friends
import com.example.kot.model.Record
import com.example.kot.model.TopUser
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import io.ghyeok.stickyswitch.widget.StickySwitch
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        var isRandom = false
        var duration = 20
        var mute = true
        lateinit var userId: String
        var list: ArrayList<Record>? = null
        lateinit var dbReference: DatabaseReference
        lateinit var firebaseDb: FirebaseDatabase
        var toplist: ArrayList<TopUser>? = null
        lateinit var notificationManager: NotificationManagerCompat
        lateinit var recyclerView: RecyclerView
        var friendList: ArrayList<Friends> = ArrayList()
        var fullFriendsList: ArrayList<TopUser> = ArrayList()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toplist = ArrayList()
        list = ArrayList()
        notificationManager = NotificationManagerCompat.from(this)
        initFriendsList()
        firebaseDb = FirebaseDatabase.getInstance()
        dbReference = firebaseDb.getReference("user")
        activity_main_userInfo.setOnClickListener {
            dialog()
        }
        getUserId()


        dbReference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                sendNotification(p0)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })

        val muteSwitch: StickySwitch = findViewById(R.id.switch_volume)
        muteSwitch.setDirection(StickySwitch.Direction.RIGHT)
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
        Picasso.get().load("${user!!.photoUrl}?type=large").into(user_avatar)
        user_name.text = user!!.displayName
    }

    private fun sendNotification(p0: DataSnapshot) {
        val notification = NotificationCompat.Builder(this@MainActivity,
            App.CHANEL_ID
        )
            .setSmallIcon(R.drawable.winner)
            .setContentTitle("${p0.getValue(TopUser::class.javaObjectType)!!.name} New Score")
            .setContentText("${p0.getValue(TopUser::class.javaObjectType)!!.maxScore}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .build()

        notificationManager.notify(1, notification)
    }

    private fun getUserId() {
        val request = GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken()
        ) { `object`, response ->
            userId = `object`.getString("id")
        }

        val parameters = Bundle()
        parameters.putString("fields", "id")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun initFriendsList() {
        val friendsRequest = GraphRequest.newGraphPathRequest(
            AccessToken.getCurrentAccessToken(),
            "/${AccessToken.getCurrentAccessToken().userId}/friends"
        ) {
            val array = it.jsonObject.getJSONArray("data")
            for (i in 0 until array.length()) {
                val name = array.getJSONObject(i).getString("name")
                val id = array.getJSONObject(i).getString("id").toLong()
                val friend = Friends(name, id)
                friendList.add(friend)
            }
        }

        friendsRequest.executeAsync()


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

    override fun onStart() {
        super.onStart()
        loadTops()


    }

    override fun onBackPressed() {
    }

    fun showRecords(view: View) {
        val intent = Intent(this, RecordsActivity::class.java)
        startActivity(intent)
    }

    private fun dialog() {

        val dialog = Dialog(this)
        val dialogView = layoutInflater.inflate(R.layout.user_dialog, null)
        val image = dialogView.findViewById<ImageView>(R.id.dialog_userphoto)
        val name = dialogView.findViewById<TextView>(R.id.dialog_userName)
        val closeButton = dialogView.findViewById<Button>(R.id.btn_close)
        Picasso.get().load("${user!!.photoUrl}?type=large").into(image)
        name.text = user!!.displayName
        dialog.setCancelable(false)
        dialog.setContentView(dialogView)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }

    private fun loadTops() {
        val view = layoutInflater.inflate(R.layout.fragment_top100, container, false)
        recyclerView = view.findViewById(
            R.id.recycler_view_top100
        )

        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                toplist!!.clear()
                p0.children.forEach {
                    if (p0.hasChildren()) {
                        val topUser = it.getValue(TopUser::class.javaObjectType)
                        toplist!!.add(topUser!!)
                    }
                }
                recyclerView.adapter =
                    TopRecordsAdapter(toplist!!)
                toplist!!.sortByDescending { it.maxScore }
                recyclerView.adapter!!.notifyDataSetChanged()
            }
        })
    }

}




