package com.sample.mentalhealth.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.mentalhealth.databinding.ItemOperationCardBinding

class OperationAdapter(
    private val operations: List<Operation>,
    private val onClick: (Operation) -> Unit
) : RecyclerView.Adapter<OperationAdapter.OperationViewHolder>() {

    inner class OperationViewHolder(
        private val binding: ItemOperationCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(operation: Operation) {

            binding.txtOperationName.text = operation.title
            binding.imgOperation.setImageResource(operation.icon)

            binding.root.setOnClickListener {
                onClick(operation)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OperationViewHolder {

        val binding = ItemOperationCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return OperationViewHolder(binding)
    }

    override fun getItemCount(): Int = operations.size

    override fun onBindViewHolder(
        holder: OperationViewHolder,
        position: Int
    ) {
        holder.bind(operations[position])
    }
}