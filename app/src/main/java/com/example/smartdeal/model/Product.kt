package com.example.smartdeal.model

import com.google.gson.annotations.SerializedName

data class Product (
    val title:String,

    @SerializedName("photo_url")
    val photoUrl:String?,

    val discount:Int,

    val AllowedCollectiveShopping:Boolean
)