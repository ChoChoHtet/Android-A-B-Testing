package com.android.ab_testing_example

import androidx.recyclerview.widget.DiffUtil

data class Container(val id: Int, val containerNumber: String)

object ContainerDiffUtil : DiffUtil.ItemCallback<Container>() {
    override fun areItemsTheSame(oldItem: Container, newItem: Container): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Container, newItem: Container): Boolean {
        return oldItem == newItem
    }

}