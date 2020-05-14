package com.example.kot

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.record_item.view.*

@Suppress("DEPRECATION")
class RecordsAdapter(private val list: ArrayList<Record>) : RecyclerView.Adapter<RecordsAdapter.RecordsViewHolder>() {

    class RecordsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.image_view
        val recordScore: TextView = itemView.record_score
        val recordTime: TextView = itemView.record_time
        val recordDate: TextView = itemView.record_date

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.record_item, parent, false)
        itemView.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return RecordsViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecordsViewHolder, position: Int) {
        val currentRecord = list[position]
        holder.imageView.background = null
        holder.imageView.setImageResource(currentRecord.modeImage)
        holder.recordScore.text = currentRecord.score
        holder.recordTime.text = currentRecord.time
        holder.recordDate.text = currentRecord.date
    }
}