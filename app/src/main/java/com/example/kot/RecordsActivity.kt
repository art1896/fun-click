package com.example.kot

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_records.*
import java.lang.reflect.Type

class RecordsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)

        loadRecords()

        recycler_view.adapter = RecordsAdapter(MainActivity.list!!)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
    }

    fun loadRecords() {
        val sharedPreferences = getSharedPreferences("records", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("recordsList", null)
        val type: Type = object : TypeToken<ArrayList<Record?>?>() {}.type
        MainActivity.list = gson.fromJson(json, type)

    }
}
