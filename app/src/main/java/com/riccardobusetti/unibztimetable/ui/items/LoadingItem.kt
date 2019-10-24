package com.riccardobusetti.unibztimetable.ui.items

import com.riccardobusetti.unibztimetable.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class LoadingItem : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.item_loading

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    }
}