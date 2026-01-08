package com.ext.focusedrecyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

internal class FocusEffectHelper(
    private val recyclerView: RecyclerView,
    private val scaleFactor: Float,
    private val minScale: Float,
    private val elevation: Float
) {

    private var lastFocusedPosition = RecyclerView.NO_POSITION

    fun apply(
        focusListener: ((Int) -> Unit)?,
        focusViewListener: ((android.view.View, Int, Boolean) -> Unit)?
    ) {
        val layoutManager = recyclerView.layoutManager as? androidx.recyclerview.widget.LinearLayoutManager
            ?: return

        val isHorizontal = layoutManager.orientation == RecyclerView.HORIZONTAL

        val recyclerCenter = if (isHorizontal) {
            recyclerView.width / 2
        } else {
            recyclerView.height / 2
        }

        val recyclerSize = if (isHorizontal) {
            recyclerView.width
        } else {
            recyclerView.height
        }

        var focusedPosition = RecyclerView.NO_POSITION
        var minDistance = Int.MAX_VALUE

        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)

            val childCenter = if (isHorizontal) {
                (child.left + child.right) / 2
            } else {
                (child.top + child.bottom) / 2
            }

            val distance = kotlin.math.abs(childCenter - recyclerCenter)

            val scale = (1f - (distance.toFloat() / recyclerSize) * scaleFactor)
                .coerceIn(minScale, 1f)

            child.scaleX = scale
            child.scaleY = scale
            child.elevation = elevation * scale

            if (distance < minDistance) {
                minDistance = distance
                focusedPosition = recyclerView.getChildAdapterPosition(child)
            }
        }

        // 🔥 Notify ONLY when focus changes
        if (focusedPosition != RecyclerView.NO_POSITION &&
            focusedPosition != lastFocusedPosition
        ) {
            // Old focused view
            if (lastFocusedPosition != RecyclerView.NO_POSITION) {
                recyclerView.findViewHolderForAdapterPosition(lastFocusedPosition)
                    ?.itemView
                    ?.let { view ->
                        focusViewListener?.invoke(view, lastFocusedPosition, false)
                    }
            }

            // New focused view
            recyclerView.findViewHolderForAdapterPosition(focusedPosition)
                ?.itemView
                ?.let { view ->
                    focusViewListener?.invoke(view, focusedPosition, true)
                }

            lastFocusedPosition = focusedPosition
            focusListener?.invoke(focusedPosition)
        }
    }

}
