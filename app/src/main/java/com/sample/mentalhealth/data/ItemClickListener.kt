package com.sample.mentalhealth.data

import com.sample.mentalhealth.data.entities.Item

interface ItemClickListener {
    fun onItemClick(item: Item)
    fun onItemDel(item: Item)
}