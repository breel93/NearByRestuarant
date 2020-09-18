package com.kolaemiola.nearbyrestaurant.snap

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

class SnapOnScrollListener(
  private val snapHelper: SnapHelper,
  private val behavior: NotifyBehavior,
  private val onSnapPositionChanged: (Int?) -> Unit
) :
  RecyclerView.OnScrollListener() {

  enum class NotifyBehavior {
    NOTIFY_ON_SCROLL,
    NOTIFY_ON_STATE_IDLE
  }

  private var snapPosition = RecyclerView.NO_POSITION

  override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
    if (behavior == NotifyBehavior.NOTIFY_ON_SCROLL) {
      notifyPosition(recyclerView)
    }
  }

  override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
    if (behavior == NotifyBehavior.NOTIFY_ON_STATE_IDLE &&
      newState == RecyclerView.SCROLL_STATE_IDLE) {
      notifyPosition(recyclerView)
    }
  }

  private fun notifyPosition(recyclerView: RecyclerView) {
    val centerView = snapHelper.findSnapView(recyclerView.layoutManager)
    val position = recyclerView.layoutManager?.getPosition(centerView!!)

    position?.let {
      if (position != this.snapPosition) {
        onSnapPositionChanged(position)
        this.snapPosition = position
      }
    }
  }
}
