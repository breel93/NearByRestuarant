package com.kolaemiola.domain.model

data class VenuesQueryParams(val latLong: String,
                             val near: String,
                             val radius: Int,
                             val limit: Int )