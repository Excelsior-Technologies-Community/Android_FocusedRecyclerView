package com.ext.focusedrecyclerview

import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

internal class FocusEffectHelper(
    private val recyclerView: RecyclerView,
    private val scaleFactor: Float,
    private val minScale: Float,
    private val elevation: Float
) {

    fun apply(listener: ((Int) -> Unit)?) {
        val centerX = recyclerView.width / 2
        var focusedPosition = RecyclerView.NO_POSITION
        var minDistance = Int.MAX_VALUE

        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val childCenterX = (child.left + child.right) / 2
            val distance = abs(centerX - childCenterX)

            val scale = (1f - distance.toFloat() / recyclerView.width)
                .coerceIn(minScale, 1f)

            child.scaleX = scale
            child.scaleY = scale
            child.elevation = elevation * scale

            if (distance < minDistance) {
                minDistance = distance
                focusedPosition = recyclerView.getChildAdapterPosition(child)
            }
        }

        if (focusedPosition != RecyclerView.NO_POSITION) {
            listener?.invoke(focusedPosition)
        }
    }
}
