package com.example.eventapp.fragment

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.eventapp.R
import com.example.eventapp.Retrofit.ApiConfig
import com.example.eventapp.Retrofit.ApiService
import com.example.eventapp.Response.ListEventsItem
import com.example.eventapp.databinding.FragmentDetailEventBinding
import com.example.eventapp.favoriteeventdata.FavoriteEvent
import kotlinx.coroutines.launch

class DetailEventFragment : Fragment() {

    private var _binding: FragmentDetailEventBinding? = null
    private val binding get() = _binding!!
    private var eventId: Int = 0
    private lateinit var eventTypes: Array<String>
    private lateinit var eventApiService: ApiService
    private val favoriteEvent: FavoriteEvent? by lazy { arguments?.getSerializable("event_data") as? FavoriteEvent }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventApiService = ApiConfig.getApiService()


        arguments?.let {
            eventId = it.getInt("event_id", -1)
            eventTypes = it.getStringArray("event_type") ?: arrayOf("upcoming")
        }

        if (eventId == -1) {
            showError("Event tidak ditemukan", "Coba periksa kembali daftar event.")
            return
        }

        if (favoriteEvent != null) {
            showEventDetails(favoriteEvent!!)
        } else {
            loadEventDetails(eventId, eventTypes)
        }

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Upcoming())
                .commit()
        }

    }

    private fun loadEventDetails(eventId: Int, types: Array<String>) {
        lifecycleScope.launch {
            try {
                var event: ListEventsItem? = null

                for (type in types) {
                    val response = when (type) {
                        "finished" -> eventApiService.getFinishedEvents()
                        else -> eventApiService.getUpcomingEvents()
                    }
                    if (response.isSuccessful) {
                        event = response.body()?.listEvents?.find { it.id == eventId }
                        if (event != null) break
                    }
                }

                if (event != null) {
                    showEventDetails(event)
                } else {
                    showError("Event tidak ditemukan", "Coba periksa kembali daftar event.")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                showError("Terjadi kesalahan", "Tidak dapat memuat detail event.")
            }
        }
    }

    private fun showEventDetails(event: FavoriteEvent) {
        binding.tvEventName.text = event.name
        binding.tvEventDate.text = event.beginTime
        binding.tvEventDescription.text = Html.fromHtml(event.link, Html.FROM_HTML_MODE_COMPACT)

        Glide.with(requireContext())
            .load(event.mediaCover)
            .into(binding.imgEvent)
    }

    private fun showEventDetails(event: ListEventsItem) {
        binding.tvEventName.text = event.name
        binding.tvEventDate.text = event.endTime
        binding.tvEventDescription.text = Html.fromHtml(event.description, Html.FROM_HTML_MODE_COMPACT)

        Glide.with(requireContext())
            .load(event.mediaCover)
            .into(binding.imgEvent)
    }

    private fun showError(title: String, message: String) {
        binding.tvEventName.text = title
        binding.tvEventDescription.text = message
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
