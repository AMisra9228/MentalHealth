package com.sample.mentalhealth.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sample.mentalhealth.databinding.ItemOverviewBinding

class OverviewAdapter(
    private val listener: (OverviewItem) -> Unit
) : RecyclerView.Adapter<OverviewAdapter.ViewHolder>() {

    private val list = ArrayList<OverviewItem>()

    fun submitList(data: List<OverviewItem>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        val binding: ItemOverviewBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = ItemOverviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val item = list[position]

        holder.binding.txtTitle.text = item.title

        Glide.with(holder.itemView.context)
            .load(item.image)
            .into(holder.binding.imgOverview)

        holder.binding.root.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = list.size
}