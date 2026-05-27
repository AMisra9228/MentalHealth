package com.sample.mentalhealth.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    var productRepository = ProductRepository()
    var productList = MutableLiveData<List<Product>>()

    init {
        addProductsToList()
    }
    fun addProductsToList() {
        CoroutineScope(Dispatchers.Main).launch {
            productList.value = productRepository.addProductsToList()

        }
    }

}