package com.example.eventapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.Adapter.FinishedEventAdapter
import com.example.eventapp.R
import com.example.eventapp.Repository.EventRepository
import com.example.eventapp.Repository.FavoriteEventRepository
import com.example.eventapp.Retrofit.ApiConfig
import com.example.eventapp.ViewModel.FavoriteViewModel
import com.example.eventapp.ViewModel.FinishedViewModel
import com.example.eventapp.ViewModelFactory.FavoriteViewModelFactory
import com.example.eventapp.ViewModelFactory.FinishedViewModelFactory
import com.example.eventapp.favoriteeventdata.AppDataBase
import kotlinx.coroutines.launch

class FinishedFragment : Fragment() {

    private val viewModel: FinishedViewModel by viewModels {
        FinishedViewModelFactory(EventRepository(ApiConfig.getApiService()))
    }

    private val favoriteViewModel: FavoriteViewModel by viewModels {
        FavoriteViewModelFactory(FavoriteEventRepository(AppDataBase.getDatabase(requireContext()).favoriteEventDao()))
    }

    private lateinit var eventAdapter: FinishedEventAdapter
    private lateinit var recyclerView: RecyclerView
    private var searchView: SearchView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_finished, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_finished)
        searchView = view.findViewById(R.id.search_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        eventAdapter = FinishedEventAdapter(emptyList(), emptyList(), favoriteViewModel)
        recyclerView.adapter = eventAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.finishedEvents.collect { events ->
                eventAdapter.setEvents(events)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            favoriteViewModel.favoriteEvents.collect { favoriteEvents ->
                eventAdapter.setFavoriteEvents(favoriteEvents)
            }
        }


        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                eventAdapter.filter(newText ?: "")
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView = null
    }
}
