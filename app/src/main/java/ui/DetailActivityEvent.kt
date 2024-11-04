package ui


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.dicodingg.R
import com.example.dicodingg.databinding.ActivityDetailEventBinding
import data.response.ListEventsItem
import database.FavoriteEvent
import ui.ui.Favorite.FavoriteViewModel

class DetailActivityEvent : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private val viewModel: DetailEventViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private var event: ListEventsItem? = null
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.detail)
        }

        val eventId = intent.getIntExtra("EVENT_ID", 0)
        if (eventId != 0) {
            showLoading(true)
            viewModel.getEventDetail(eventId.toString())
            observeViewModel()
        }

        setupFavoriteButton()

        binding.btnRegister.setOnClickListener {
            event?.let { currentEvent ->
                currentEvent.link?.let { url ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } ?: run {

                    Toast.makeText(this, "Link tidak tersedia", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupFavoriteButton() {
        binding.fabFavorite.setOnClickListener {
            event?.let { currentEvent ->
                val favoriteEvent = FavoriteEvent(
                    id = currentEvent.id.toString(),
                    name = currentEvent.name ?: "Unknown Event",
                    mediaCover = currentEvent.imageLogo
                )
                if (isFavorite) {
                    favoriteViewModel.removeFavorite(favoriteEvent)
                    Toast.makeText(this, "Event removed from favorites", Toast.LENGTH_SHORT).show()
                    binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
                } else {

                    favoriteViewModel.addFavorite(favoriteEvent)
                    Toast.makeText(this, "Event added to favorites", Toast.LENGTH_SHORT).show()
                    binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                }

                isFavorite = !isFavorite

            }
        }
    }


    private fun observeViewModel() {
        viewModel.eventDetail.observe(this) { event ->

            event?.let {
                updateUI(it)
                checkIfFavorite(it.id.toString())
                showLoading(false)
            }
        }
        viewModel.error.observe(this) { error ->

            showLoading(false)
        }

    }

    private fun checkIfFavorite(eventId: String) {
        favoriteViewModel.favoriteEvents.observe(this) { favorites ->
            isFavorite = favorites.any { it.id == eventId }
            if (isFavorite) {
                binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
            }
        }
    }

    private fun updateUI(event: ListEventsItem) {
        this.event = event

        binding.tvEventName.text = event.name

        binding.tvEventDescription.text = HtmlCompat.fromHtml(
            event.description ?: "",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        binding.tvEventLocation.text = event.cityName
        binding.tvEventQuota.text = getString(R.string.quota_format, event.quota)
        binding.tvEventRegistrants.text = getString(R.string.registrants_format, event.registrants)

        val quota = event.quota ?: 0
        val registrants = event.registrants ?: 0
        val remainingQuota = (quota - registrants).coerceAtLeast(0)
        binding.tvEventRemainingQuota.text = getString(R.string.remaining_quota_format, remainingQuota)

        binding.tvEventOwnerName.text = getString(R.string.owner_name_format, event.ownerName)
        binding.tvEventDate.text = getString(R.string.event_time_format, event.beginTime)

        if (!event.imageLogo.isNullOrEmpty()) {
            Glide.with(this)
                .load(event.imageLogo)
                .into(binding.ivEventLogo)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Mengarahkan ke fragment atau Activity sebelumnya
                onBackPressed()  // Memanggil onBackPressed() akan mengarahkan kembali
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }
}