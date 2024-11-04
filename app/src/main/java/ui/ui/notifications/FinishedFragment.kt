package ui.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dicodingg.R
import com.example.dicodingg.databinding.FragmentFinishedBinding
import ui.DetailActivityEvent
import ui.EventAdapter

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private lateinit var finishedViewModel: FinishedViewModel
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        finishedViewModel = ViewModelProvider(this).get(FinishedViewModel::class.java)
        setupRecyclerView()
        observeViewModel()

        finishedViewModel.fetchEvents()
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = getString(R.string.finished_events)
        }
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivityEvent::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = eventAdapter

    }

    private fun observeViewModel() {
        finishedViewModel.events.observe(viewLifecycleOwner) { events ->
            events?.let {
                eventAdapter.submitList(it)
            } ?: run {
                Log.d("FinishedFragment", "Events are null or empty")
            }
        }
        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
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