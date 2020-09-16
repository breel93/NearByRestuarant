package com.kolaemiola.nearbyrestaurant.model

data class RestaurantQuery(val latLong: String,
                           val near: String,
                           val radius: Int,
                           val limit: Int )