@file:Suppress("DEPRECATION")

package com.example.kot.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kot.R
import com.example.kot.ui.FriendsScoreFragment
import com.example.kot.ui.MyScoreFragment
import com.example.kot.ui.Top100Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class RecordsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)

        MainActivity.fullFriendsList.clear()
        for (i in 0 until MainActivity.friendList.size) {
            for (j in 0 until MainActivity.toplist!!.size) {
                if (MainActivity.friendList[i].id == MainActivity.toplist!![j].id) {
                    MainActivity.fullFriendsList.add(
                        MainActivity.toplist!![j])
                }
            }
        }


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, MyScoreFragment()).commit()
        }
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            var fragment: Fragment? = null

            when (item.itemId) {
                R.id.myScores -> fragment = MyScoreFragment()
                R.id.friendsScores -> fragment =
                    FriendsScoreFragment()
                R.id.top100score -> fragment =
                    Top100Fragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment!!).commit()
            true
        }


    }
}
