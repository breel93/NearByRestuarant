/**
 *  Designed and developed by Kola Emiola
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package com.kolaemiola.nearbyrestaurant.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kolaemiola.nearbyrestaurant.databinding.VenueItemBinding
import com.kolaemiola.nearbyrestaurant.model.Venue

class RestaurantRecyclerAdapter(
  private val venueClickListener: VenueClickListener
) : ListAdapter<Venue, RestaurantRecyclerAdapter.VenueViewHolder>(VenueDiffCallback) {

  private lateinit var binding: VenueItemBinding

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    binding = VenueItemBinding.inflate(layoutInflater, parent, false)
    return VenueViewHolder(binding)
  }

  override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
    val venue = getItem(position)
    holder.bind(venue, venueClickListener)
  }
  inner class VenueViewHolder(binding: VenueItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(venue: Venue, venueClickListener: VenueClickListener) {
      itemView.setOnClickListener {
        venueClickListener.showVenueDetail(venue)
      }
      binding.restaurantName.text = venue.name
      binding.retuarantAddress.text = venue.location.address
    }
  }
  companion object {
    val VenueDiffCallback = object : DiffUtil.ItemCallback<Venue>() {
      override fun areItemsTheSame(oldItem: Venue, newItem: Venue): Boolean {
        return oldItem.name == newItem.name
      }
      override fun areContentsTheSame(oldItem: Venue, newItem: Venue): Boolean {
        return oldItem == newItem
      }
    }
  }
}
