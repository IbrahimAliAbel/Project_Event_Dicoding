package ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingg.R
import com.example.dicodingg.databinding.ItemEventBinding
import database.FavoriteEvent

class FavoriteAdapter(
    private val onItemClick: (FavoriteEvent) -> Unit
) : ListAdapter<FavoriteEvent, FavoriteAdapter.FavoriteViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteEvent = getItem(position)
        holder.bind(favoriteEvent)
        holder.itemView.setOnClickListener { onItemClick(favoriteEvent) }
    }

    class FavoriteViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteEvent: FavoriteEvent) {
            binding.tvEventName.text = favoriteEvent.name
            val imageUrl = favoriteEvent.mediaCover
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(binding.ivEventLogo.context)
                    .load(imageUrl)
                    .into(binding.ivEventLogo)
            } else {
                binding.ivEventLogo.setImageResource(R.drawable.placeholder_image)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEvent>() {
            override fun areItemsTheSame(
                oldItem: FavoriteEvent,
                newItem: FavoriteEvent
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FavoriteEvent,
                newItem: FavoriteEvent
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}