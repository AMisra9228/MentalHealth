package com.sample.mentalhealth.home

import com.sample.mentalhealth.data.ProductDataStorage

class ProductRepository {
    var productDataStorage = ProductDataStorage()

    suspend fun addProductsToList() : List<Product> = productDataStorage.addProductsToList()


}