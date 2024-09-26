package com.app.bmicalculator.utils.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.scrollToPositionRV(pos: Int, listSize: Int) {
    val layoutManager = this.layoutManager as LinearLayoutManager
    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
    val totalItemCount = layoutManager.itemCount

    if (pos in 0 until (listSize / 2) || (lastVisiblePosition == totalItemCount - 1 && pos == listSize / 2)) {
        if (pos == 0) {
            this.smoothScrollToPosition(pos)
            this.scrollToPosition(pos)
        } else {
            this.smoothScrollToPosition(pos - 1)
            this.scrollToPosition(pos - 1)
        }
    } else {
        if (listSize - 1 == pos) {
            this.smoothScrollToPosition(pos)
            this.scrollToPosition(pos)
        } else {
            this.smoothScrollToPosition(pos + 1)
            this.scrollToPosition(pos + 1)
        }

    }


}