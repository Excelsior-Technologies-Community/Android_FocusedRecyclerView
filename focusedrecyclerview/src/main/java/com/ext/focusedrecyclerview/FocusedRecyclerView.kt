package com.ext.focusedrecyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class FocusedRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    private var scaleFactor = 0.15f
    private var minScale = 0.85f
    private var elevationValue = 12f
    private var focusListener: ((Int) -> Unit)? = null
    private val snapHelper = CenterSnapHelper()
    private lateinit var effectHelper: FocusEffectHelper

    private var focusViewListener: ((view: android.view.View, position: Int, isFocused: Boolean) -> Unit)? = null


    init {
        readAttrs(context, attrs)
        setupRecycler()
    }

    private fun readAttrs(context: Context, attrs: AttributeSet?) {
        attrs ?: return
        val ta = context.obtainStyledAttributes(attrs, R.styleable.FocusedRecyclerView)

        scaleFactor = ta.getFloat(
            R.styleable.FocusedRecyclerView_frv_scaleFactor,
            scaleFactor
        )
        minScale = ta.getFloat(
            R.styleable.FocusedRecyclerView_frv_minScale,
            minScale
        )
        elevationValue = ta.getDimension(
            R.styleable.FocusedRecyclerView_frv_elevation,
            elevationValue
        )

        ta.recycle()
    }

    private fun setupRecycler() {
        isFocusable = true
        isFocusableInTouchMode = true
        overScrollMode = OVER_SCROLL_NEVER

        effectHelper = FocusEffectHelper(
            recyclerView = this,
            scaleFactor = scaleFactor,
            minScale = minScale,
            elevation = elevationValue
        )

        snapHelper.attachToRecyclerView(this)

        addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                effectHelper.apply(
                    focusListener = focusListener,
                    focusViewListener = focusViewListener
                )
            }
        })
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        post { effectHelper.apply(
            focusListener = focusListener,
            focusViewListener = focusViewListener
        ) }
    }


    fun setOnItemFocusListener(listener: (Int) -> Unit) {
        this.focusListener = listener
    }

    fun setOnItemFocusViewListener(
        listener: (view: android.view.View, position: Int, isFocused: Boolean) -> Unit
    ) {
        this.focusViewListener = listener
    }

}
