package com.sample.mentalhealth.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sample.mentalhealth.data.entities.Item
import com.sample.mentalhealth.databinding.TaskItemBinding

class ItemAdapter (private val onItemClick : (Item) -> Unit, private val onItemDelete: (Item) -> Unit) :
    ListAdapter<Item, ItemAdapter.TodoViewHolder>(TodoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = getItem(position)
        holder.bind(todo)

        holder.itemView.setOnClickListener {
            onItemClick(todo)
        }

        holder.itemView.setOnLongClickListener {
            onItemDelete(todo)
            true
        }
    }

    class TodoViewHolder(private val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Item) {
            binding.taskItemName.text = todo.title
            binding.taskItemDes.text = todo.description
            binding.taskDate.text = todo.created
            binding.taskStatus.text = todo.priority

            if(binding.taskStatus.text.toString().equals("H")){
                binding.cardViewProduct.setCardBackgroundColor(binding.root.context.getColor(android.R.color.holo_red_light))
            } else if(binding.taskStatus.text.toString().equals("M")){
                binding.cardViewProduct.setCardBackgroundColor(binding.root.context.getColor(android.R.color.holo_orange_light))
            } else if(binding.taskStatus.text.toString().equals("L")){
                binding.cardViewProduct.setCardBackgroundColor(binding.root.context.getColor(android.R.color.holo_green_light))
            }


        }
    }

    class TodoDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }
}
