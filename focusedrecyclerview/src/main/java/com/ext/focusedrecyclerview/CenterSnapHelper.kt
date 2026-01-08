package com.ext.focusedrecyclerview

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import kotlin.math.abs

internal class CenterSnapHelper : SnapHelper() {

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray {
        val out = IntArray(2)

        if (layoutManager is LinearLayoutManager) {
            if (layoutManager.orientation == RecyclerView.HORIZONTAL) {
                out[0] = distanceToCenterX(layoutManager, targetView)
                out[1] = 0
            } else {
                out[0] = 0
                out[1] = distanceToCenterY(layoutManager, targetView)
            }
        }
        return out
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        if (layoutManager is LinearLayoutManager) {
            return findCenterView(layoutManager)
        }
        return null
    }

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager,
        velocityX: Int,
        velocityY: Int
    ): Int {
        if (layoutManager !is LinearLayoutManager) {
            return RecyclerView.NO_POSITION
        }

        val itemCount = layoutManager.itemCount
        if (itemCount == 0) return RecyclerView.NO_POSITION

        val isHorizontal = layoutManager.orientation == RecyclerView.HORIZONTAL
        val velocity = if (isHorizontal) velocityX else velocityY

        val currentView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
        val currentPosition = layoutManager.getPosition(currentView)

        return when {
            velocity > 0 -> (currentPosition + 1).coerceAtMost(itemCount - 1)
            velocity < 0 -> (currentPosition - 1).coerceAtLeast(0)
            else -> currentPosition
        }
    }

    private fun findCenterView(layoutManager: LinearLayoutManager): View? {
        var closestChild: View? = null
        var closestDistance = Int.MAX_VALUE

        val center = if (layoutManager.orientation == RecyclerView.HORIZONTAL) {
            layoutManager.width / 2
        } else {
            layoutManager.height / 2
        }

        for (i in 0 until layoutManager.childCount) {
            val child = layoutManager.getChildAt(i) ?: continue

            val childCenter = if (layoutManager.orientation == RecyclerView.HORIZONTAL) {
                (child.left + child.right) / 2
            } else {
                (child.top + child.bottom) / 2
            }

            val distance = abs(childCenter - center)
            if (distance < closestDistance) {
                closestDistance = distance
                closestChild = child
            }
        }
        return closestChild
    }

    private fun distanceToCenterX(
        layoutManager: LinearLayoutManager,
        targetView: View
    ): Int {
        val childCenter = (targetView.left + targetView.right) / 2
        val recyclerCenter = layoutManager.width / 2
        return childCenter - recyclerCenter
    }

    private fun distanceToCenterY(
        layoutManager: LinearLayoutManager,
        targetView: View
    ): Int {
        val childCenter = (targetView.top + targetView.bottom) / 2
        val recyclerCenter = layoutManager.height / 2
        return childCenter - recyclerCenter
    }
}
