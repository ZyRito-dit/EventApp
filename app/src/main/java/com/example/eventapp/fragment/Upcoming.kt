package com.example.eventapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.Adapter.EventAdapter
import com.example.eventapp.R
import com.example.eventapp.Repository.EventRepository
import com.example.eventapp.Repository.FavoriteEventRepository
import com.example.eventapp.Retrofit.ApiConfig
import com.example.eventapp.ViewModel.UpcomingViewModel
import com.example.eventapp.ViewModelFactory.UpcomingViewModelFactory
import com.example.eventapp.ViewModelFactory.FavoriteViewModelFactory
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.eventapp.ViewModel.FavoriteViewModel
import com.example.eventapp.favoriteeventdata.AppDataBase
import kotlinx.coroutines.launch

class Upcoming : Fragment() {


    private val viewModel: UpcomingViewModel by viewModels {
        UpcomingViewModelFactory(EventRepository(ApiConfig.getApiService()))
    }


    private val favoriteViewModel: FavoriteViewModel by viewModels {
        FavoriteViewModelFactory(
            FavoriteEventRepository(
                AppDataBase.getDatabase(requireContext()).favoriteEventDao()
            )
        )
    }

    private lateinit var searchView: SearchView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_upcoming, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_upcoming)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        eventAdapter = EventAdapter(emptyList(), favoriteViewModel)
        recyclerView.adapter = eventAdapter


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.upcomingEvents.collect { events ->
                    Log.d("UpcomingFragment", "Event API diterima: ${events.size}")
                    eventAdapter.setEvents(events)
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteViewModel.favoriteEvents.collect { favoriteEvents ->
                    Log.d("UpcomingFragment", "Favorite events: ${favoriteEvents.size}")
                    eventAdapter.setFavoriteEvents(favoriteEvents)
                }
            }
        }


        if (viewModel.upcomingEvents.value.isEmpty()) {
            viewModel.getUpcomingEvents()
        }


    }
}


