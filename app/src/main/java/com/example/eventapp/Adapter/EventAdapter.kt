package com.example.eventapp.Adapter


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.example.eventapp.ViewModel.FavoriteViewModel
import com.example.eventapp.favoriteeventdata.FavoriteEvent
import com.example.eventapp.fragment.DetailEventFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class EventAdapter(
    private var eventList: List<ListEventsItem>,
    private val favoriteViewModel: FavoriteViewModel
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private var favoriteEvents: Set<Int> = emptySet()

    fun setFavoriteEvents(events: List<FavoriteEvent>) {
        favoriteEvents = events.map { it.id }.toSet()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.bind(event, favoriteEvents.contains(event.id))
    }

    override fun getItemCount(): Int = eventList.size

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventName: TextView = itemView.findViewById(R.id.tv_event_title)
        private val eventDate: TextView = itemView.findViewById(R.id.tv_event_date)
        private val eventImage: ImageView = itemView.findViewById(R.id.img_event)
        private val btnFavorite: ImageView = itemView.findViewById(R.id.btn_favorite)
        private val btnDetailEvent: Button = itemView.findViewById(R.id.btn_detailevent)
        private val btnLihatEvent: Button = itemView.findViewById(R.id.btn_lihatevent)

        fun bind(event: ListEventsItem, isFavorite: Boolean) {
            eventName.text = event.name
            eventDate.text = formatDate(event.beginTime)

            // Button untuk melihat event (buka link event)
            btnLihatEvent.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                itemView.context.startActivity(intent)
            }

            // Menampilkan gambar event
            Glide.with(itemView.context)
                .load(event.mediaCover)
                .into(eventImage)

            // Mengatur ikon tombol favorit
            btnFavorite.setImageResource(
                if (isFavorite) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )

            // Button untuk menambah atau menghapus event dari favorit
            btnFavorite.setOnClickListener {
                if (isFavorite) {
                    favoriteViewModel.removeFromFavorite(event.id)
                } else {
                    val favoriteEvent = FavoriteEvent(
                        id = event.id,
                        name = event.name,
                        beginTime = event.beginTime,
                        mediaCover = event.mediaCover,
                        link = event.link
                    )
                    favoriteViewModel.addToFavorite(favoriteEvent)
                }
            }

            btnDetailEvent.setOnClickListener {
                val fragment = DetailEventFragment()
                val bundle = Bundle()
                bundle.putInt("event_id", event.id)  // Pass event_id ke fragment
                fragment.arguments = bundle

                val transaction = (itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, fragment)  // Ganti dengan ID container fragment Anda
                transaction.addToBackStack(null)  // Optional: Menambah fragment ke back stack
                transaction.commit()
            }



        }

    }

    fun setEvents(events: List<ListEventsItem>) {
        eventList = events
        notifyDataSetChanged()
    }

    private fun formatDate(dateTime: String?): String {
        if (dateTime.isNullOrEmpty()) return "Tanggal Tidak Tersedia"

        return try {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val outputFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy HH:mm", Locale("id"))
            val date = LocalDateTime.parse(dateTime, inputFormatter)
            date.format(outputFormatter)
        } catch (e: Exception) {
            "Format Tanggal Tidak Valid"
        }
    }
}
