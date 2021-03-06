package com.irzstudio.runningapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.irzstudio.runningapp.R
import com.irzstudio.runningapp.db.Run
import com.irzstudio.runningapp.util.TrackingUtility
import kotlinx.android.synthetic.main.item_run.view.*
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    private var list: MutableList<Run> = mutableListOf()

    private val diffCalback = object : DiffUtil.ItemCallback<Run>(){
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    val differ = AsyncListDiffer(this, diffCalback)

    fun submitList(list: List<Run>) = differ.submitList(list)

    inner class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(run: Run) {
            Glide.with(itemView)
                .load(run.img)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(itemView.iv_runimage)
            val calendar = Calendar.getInstance().apply {
                timeInMillis = run.timestamp
            }
            val dataFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            itemView.tv_date.text = dataFormat.format(calendar.time)

            itemView.tv_avgspeed.text = "${run.avgSpeedInKMH} Km/h"

            itemView.tv_distance.text = "${run.distanceInMeters / 1000f} Km"

            itemView.tv_time.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)

            itemView.tv_calories.text = "${run.caloriesBurned} Kcal"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_run, parent, false)
        return RunViewHolder(view)
    }

    override fun onBindViewHolder(holder: RunAdapter.RunViewHolder, position: Int) {
        val run = differ.currentList[position]
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setDataRun(data: List<Run>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

}