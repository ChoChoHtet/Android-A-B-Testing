package com.android.ab_testing_example

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class DummyAdapter(private val clickListener: ClickListener): ListAdapter<Container, DummyViewHolder>(ContainerDiffUtil) {
    interface  ClickListener{
        fun onItemClickListener(container: Container)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DummyViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_container,parent,false)
        return DummyViewHolder(view,clickListener)
    }

    override fun onBindViewHolder(holder: DummyViewHolder, position: Int) {
       holder.bind(getItem(position))
    }
}