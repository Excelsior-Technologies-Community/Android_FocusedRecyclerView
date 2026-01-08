package com.ext.android_focusedrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FocusAdapter(
    private val items: List<String>
) : RecyclerView.Adapter<FocusAdapter.FocusViewHolder>() {

    inner class FocusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.txtTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FocusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_focus, parent, false)
        return FocusViewHolder(view)
    }

    override fun onBindViewHolder(holder: FocusViewHolder, position: Int) {
        holder.text.text = items[position]
    }

    override fun getItemCount(): Int = items.size
}
