package com.aminheidari.age.utils

interface ItemBinder<T> {
    fun bind(item: T)
}

interface OnItemSelectListener<T> {
    fun onSelected(item: T)
}