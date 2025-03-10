package com.example.eventapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventapp.Adapter.FavoriteEventAdapter
import com.example.eventapp.R
import com.example.eventapp.Repository.FavoriteEventRepository
import com.example.eventapp.ViewModel.FavoriteViewModel
import com.example.eventapp.ViewModelFactory.FavoriteViewModelFactory
import com.example.eventapp.favoriteeventdata.AppDataBase
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private val favoriteViewModel: FavoriteViewModel by viewModels {
        FavoriteViewModelFactory(FavoriteEventRepository(AppDataBase.getDatabase(requireContext()).favoriteEventDao()))
    }

    private lateinit var adapter: FavoriteEventAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_favorite)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        adapter = FavoriteEventAdapter(emptyList()) { event ->
            favoriteViewModel.removeFromFavorite(event.id)
        }

        recyclerView.adapter = adapter


        viewLifecycleOwner.lifecycleScope.launch {
            favoriteViewModel.favoriteEvents.collect { events ->
                adapter.setEvents(events)
            }
        }
    }
}
