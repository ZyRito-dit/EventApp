package com.example.eventapp.Adapter

import android.content.Intent
import android.net.Uri
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
import com.example.eventapp.Response.ListEventsItem
import com.example.eventapp.Response.toFavoriteEvent
import com.example.eventapp.ViewModel.FavoriteViewModel
import com.example.eventapp.favoriteeventdata.FavoriteEvent
import com.example.eventapp.fragment.DetailEventFragment

class FinishedEventAdapter(
    private var eventList: List<ListEventsItem>,
    private var favoriteEvents: List<FavoriteEvent>,
    private val favoriteViewModel: FavoriteViewModel
) : RecyclerView.Adapter<FinishedEventAdapter.EventViewHolder>() {

    private var filteredList: List<ListEventsItem> = eventList // 🔥 Daftar hasil pencarian

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = filteredList[position] // 🔥 Gunakan daftar hasil filter
        val isFavorite = favoriteEvents.any { it.id == event.id }
        holder.bind(event, isFavorite)
    }

    override fun getItemCount(): Int = filteredList.size // 🔥 Gunakan daftar hasil filter

    fun setEvents(events: List<ListEventsItem>) {
        eventList = events
        filteredList = events // 🔥 Pastikan daftar filter diperbarui
        notifyDataSetChanged()
    }

    fun setFavoriteEvents(favorites: List<FavoriteEvent>) {
        favoriteEvents = favorites
        notifyDataSetChanged()
    }

    // 🔥 Fungsi filter untuk pencarian berdasarkan nama event
    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            eventList
        } else {
            eventList.filter { it.name.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventName: TextView = itemView.findViewById(R.id.tv_event_title)
        private val eventDate: TextView = itemView.findViewById(R.id.tv_event_date)
        private val eventImage: ImageView = itemView.findViewById(R.id.img_event)
        private val btnLihatEvent: TextView = itemView.findViewById(R.id.btn_lihatevent)
        private val btnDetailEvent: Button = itemView.findViewById(R.id.btn_detailevent)
        private val likeButton: ImageView = itemView.findViewById(R.id.btn_favorite)

        fun bind(event: ListEventsItem, isFavorite: Boolean) {
            eventName.text = event.name
            eventDate.text = event.endTime

            btnLihatEvent.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                itemView.context.startActivity(intent)
            }

            Glide.with(itemView.context)
                .load(event.mediaCover)
                .into(eventImage)

            likeButton.setImageResource(
                if (isFavorite) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )

            likeButton.setOnClickListener {
                val favoriteEvent = event.toFavoriteEvent()
                favoriteViewModel.toggleFavorite(favoriteEvent)
            }

            btnDetailEvent.setOnClickListener {
                val fragment = DetailEventFragment()
                val bundle = Bundle()
                bundle.putInt("event_id", event.id)
                bundle.putString("event_type", "finished")
                fragment.arguments = bundle

                val transaction = (itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
    }
}
