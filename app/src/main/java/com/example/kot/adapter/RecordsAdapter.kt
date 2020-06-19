package com.example.kot.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kot.R
import com.example.kot.activity.LoginActivity
import com.example.kot.model.Record
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.record_item.view.*

@Suppress("DEPRECATION")
class RecordsAdapter(private val list: ArrayList<Record>) : RecyclerView.Adapter<RecordsAdapter.RecordsViewHolder>() {

    class RecordsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.image_view
        val recordScore: TextView = itemView.record_score
        val name: TextView = itemView.record_time

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.record_item, parent, false)
        itemView.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return RecordsViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecordsViewHolder, position: Int) {
        val currentRecord = list[position]
        Picasso.get().load("${LoginActivity.user?.photoUrl}?type=large").into(holder.imageView)
        holder.recordScore.text = currentRecord.score.toString()
        holder.name.text = LoginActivity.user?.displayName
    }
}