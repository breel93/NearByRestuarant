package com.kolaemiola.data.mapper

internal interface Mapper<in Data, out Domain> {
  fun from(data: Data): Domain
  fun mapModelList(models:List<Data>):List<Domain>{
    return models.mapTo(mutableListOf(), ::from)
  }
}