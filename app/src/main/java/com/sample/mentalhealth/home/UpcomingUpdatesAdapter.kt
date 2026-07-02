package com.sample.mentalhealth.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.mentalhealth.databinding.ItemUpcomingUpdateBinding

class UpcomingUpdatesAdapter(
    private val list: MutableList<UpcomingUpdate>,
    private val listener: (UpcomingUpdate) -> Unit
) : RecyclerView.Adapter<UpcomingUpdatesAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemUpcomingUpdateBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UpcomingUpdate) {

            binding.tvUpdateTitle.text = item.title
            binding.tvUpdateDescription.text = item.description

            binding.root.setOnClickListener {
                listener(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = ItemUpcomingUpdateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun submitList(data: List<UpcomingUpdate>) {
        list.clear()
        list.addAll(data.take(5)) // Show only first 5 items
        notifyDataSetChanged()
    }
}