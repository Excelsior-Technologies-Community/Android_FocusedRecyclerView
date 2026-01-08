package com.ext.android_focusedrecyclerview

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ext.focusedrecyclerview.FocusedRecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val recycler = findViewById<FocusedRecyclerView>(R.id.focusedRecycler)

        recycler.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        recycler.adapter = FocusAdapter(
            listOf("One", "Two", "Three", "Four", "Five", "Six","Seven","Eight","Nine","Ten")
        )

        recycler.setOnItemFocusViewListener { view, _, isFocused ->

            val card = view as? com.google.android.material.card.MaterialCardView
                ?: return@setOnItemFocusViewListener

            if (isFocused) {
                card.strokeWidth = 3
                card.strokeColor = getColor(R.color.white)
            } else {
                card.strokeWidth = 0
            }
        }

    }
}