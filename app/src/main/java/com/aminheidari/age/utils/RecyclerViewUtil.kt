package com.aminheidari.age.utils

interface ItemBinder<T> {
    fun bind(item: T)
}

interface OnItemSelectedListener<T> {
    fun onItemSelected(item: T)
}