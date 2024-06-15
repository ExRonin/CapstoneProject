package com.capstoneproject.data.model.product

data class ProductResponse(
    val status: Boolean,
    val message: String,
    val data: List<Product>
)