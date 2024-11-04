package ui.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingg.R
import com.example.dicodingg.databinding.FragmentUpcomingBinding
import ui.DetailActivityEvent
import ui.EventAdapter

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private lateinit var upcomingViewModel: UpcomingViewModel
    private lateinit var eventAdapter: EventAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upcomingViewModel = ViewModelProvider(this).get(UpcomingViewModel::class.java)
        setupRecyclerView()
        observeViewModel()

        upcomingViewModel.fetchEvents()

        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = getString(R.string.upcoming_events)
        }
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter { event ->

            val intent = Intent(requireContext(), DetailActivityEvent::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = eventAdapter
    }
    private fun observeViewModel() {
            upcomingViewModel.events.observe(viewLifecycleOwner) { events ->
                events?.let {
                    eventAdapter.submitList(it)
                }
            }
        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}