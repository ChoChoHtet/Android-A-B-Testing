package com.android.ab_testing_example

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_container.view.*

class DummyViewHolder(
    private val view: View,
    private val clickListener: DummyAdapter.ClickListener
) : RecyclerView.ViewHolder(view) {
    fun bind(data: Container) {
        view.tv_container_number.text = data.containerNumber
        view.setOnClickListener {
            clickListener.onItemClickListener(data)
        }
    }
}