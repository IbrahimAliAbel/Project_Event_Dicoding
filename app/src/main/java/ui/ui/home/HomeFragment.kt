package ui.ui.home

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodingg.R
import com.example.dicodingg.databinding.FragmentHomeBinding
import ui.DetailActivityEvent
import ui.EventAdapter


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var finishedAdapter: EventAdapter
    private lateinit var upcomingAdapter: EventAdapter
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = getString(R.string.home)
        }

        setupRecyclerViews()
        setupSearchView()

        // Observe finished events
        homeViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            finishedAdapter.submitList(events)
        }

        // Observe upcoming events
        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            upcomingAdapter.submitList(events)
        }

        homeViewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            if (searchResults.isNotEmpty()) {
                upcomingAdapter.submitList(searchResults)
            } else {
                // Tampilkan pesan atau kembalikan event default jika tidak ada hasil
                homeViewModel.fetchUpcomingEvents()
            }
        }

        // Panggil API untuk mengambil data event finished dan upcoming
        homeViewModel.fetchFinishedEvents()
        homeViewModel.fetchUpcomingEvents()
    }

    private fun setupRecyclerViews() {
        upcomingAdapter = EventAdapter { event ->
            // Klik pada item event akan membawa ke DetailActivityEvent
            val intent = Intent(requireContext(), DetailActivityEvent::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }

        finishedAdapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivityEvent::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }
        // Setup RecyclerView untuk Finished Events (Vertikal)
        finishedAdapter = EventAdapter { event ->
            // Handle item click untuk finished events
        }

        binding.rvFinishedEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinishedEvents.adapter = finishedAdapter

        // Setup RecyclerView untuk Upcoming Events (Horizontal)
        upcomingAdapter = EventAdapter { event ->
            // Handle item click untuk upcoming events
        }
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing_small)
        binding.rvUpcomingEvents.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingEvents.addItemDecoration(HorizontalSpacingItemDecoration(spacingInPixels))
        binding.rvUpcomingEvents.adapter = upcomingAdapter

    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        // Memanggil fungsi untuk melakukan pencarian event berdasarkan query
                        homeViewModel.searchEvents(it)
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Jika ingin langsung mencari saat mengetik, bisa panggil searchEvents di sini
                return false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class HorizontalSpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = spacing / 2  // Menambahkan jarak ke kiri
            outRect.right = spacing / 2  // Menambahkan jarak ke kanan
        }
    }
}