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
import kotlinx.coroutines.launch
import android.widget.TextView
import android.widget.ImageView
import com.example.eventapp.databinding.FragmentDetailEventBinding

class DetailEventFragment : Fragment() {

    private lateinit var eventApiService: ApiService
    private val eventId by lazy { arguments?.getInt("event_id") ?: 0 }
    private var _binding: FragmentDetailEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        eventApiService = ApiConfig.getApiService()


        if (eventId != 0) {
            loadEventDetails(eventId)
        }


        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private val eventType by lazy { arguments?.getString("event_type") ?: "upcoming" }

    private fun loadEventDetails(eventId: Int) {
        lifecycleScope.launch {
            try {
                val response = when (eventType) {
                    "finished" -> eventApiService.getFinishedEvents()
                    else -> eventApiService.getUpcomingEvents()
                }

                if (response.isSuccessful) {
                    val event = response.body()?.listEvents?.find { it.id == eventId }
                    event?.let {
                        showEventDetails(it)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun showEventDetails(event: ListEventsItem) {

        binding.tvEventName.text = event.name
        binding.tvEventDate.text = event.endTime


        binding.tvEventDescription.text = Html.fromHtml(event.description, Html.FROM_HTML_MODE_COMPACT)


        Glide.with(requireContext())
            .load(event.mediaCover)
            .into(binding.imgEvent)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}


