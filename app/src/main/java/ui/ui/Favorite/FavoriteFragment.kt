package ui.ui.Favorite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingg.R
import com.example.dicodingg.databinding.FragmentFavoriteBinding
import ui.DetailActivityEvent
import ui.FavoriteAdapter

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        showLoading(true)
        favoriteViewModel.fetchFavorites()
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = getString(R.string.favorite_events)
        }
    }

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter { favoriteEvent ->
            val intent = Intent(requireContext(), DetailActivityEvent::class.java)
            intent.putExtra("EVENT_ID", favoriteEvent.id.toInt())
            startActivity(intent)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun observeViewModel() {
        favoriteViewModel.favoriteEvents.observe(viewLifecycleOwner) { events ->
            showLoading(false)
            events?.let {
            favoriteAdapter.submitList(events)
                binding.recyclerView.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            }
        }
        favoriteViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}