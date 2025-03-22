package com.example.eventapp.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventapp.R

import com.example.eventapp.favoriteeventdata.FavoriteEvent
import com.example.eventapp.fragment.DetailEventFragment

class FavoriteEventAdapter(
    private var eventList: List<FavoriteEvent>,
    private val onRemoveClick: (FavoriteEvent) -> Unit
) : RecyclerView.Adapter<FavoriteEventAdapter.FavoriteEventViewHolder>() {
    inner class FavoriteEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgEvent: ImageView = itemView.findViewById(R.id.img_favorite_event)
        val tvEventTitle: TextView = itemView.findViewById(R.id.tv_favorite_event_title)
        val tvEventDate: TextView = itemView.findViewById(R.id.tv_favorite_event_date)
        val btnRemove: Button = itemView.findViewById(R.id.btn_remove_favorite)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return FavoriteEventViewHolder(view)
    }
    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        val event = eventList[position]
        holder.tvEventTitle.text = event.name
        holder.tvEventDate.text = event.beginTime

        Glide.with(holder.itemView.context)
            .load(event.mediaCover)
            .into(holder.imgEvent)
        holder.btnRemove.setOnClickListener {
            onRemoveClick(event)

        }
        holder.itemView.setOnClickListener {
            val fragment = DetailEventFragment()
            val bundle = Bundle()
            bundle.putInt("event_id", event.id)
            bundle.putStringArray("event_type", arrayOf("finished", "upcoming"))
            fragment.arguments = bundle

            val activity = holder.itemView.context as AppCompatActivity
            val transaction = activity.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun getItemCount() = eventList.size

    fun setEvents(events: List<FavoriteEvent>) {
        eventList = events
    }
}
