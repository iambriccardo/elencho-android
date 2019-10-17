package com.riccardobusetti.unibztimetable.ui.items

import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.ui.configuration.ConfigurationViewModel
import com.riccardobusetti.unibztimetable.ui.configuration.OnConfigurationClickCallback
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_configuration.view.*

class ConfigurationItem(
    private val configuration: ConfigurationViewModel.Configuration,
    private val clickCallback: OnConfigurationClickCallback
) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.item_configuration

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            this.item_configuration_title.text = configuration.title
            this.item_configuration_description.text = configuration.description
            this.item_configuration_icon.setImageResource(configuration.iconResId)

            this.setOnClickListener {
                clickCallback(configuration) { isSuccessful ->
                    if (isSuccessful) {
                        this.item_configuration_icon.setImageResource(R.drawable.ic_check)
                        this.setOnClickListener(null)
                    }
                }
            }
        }
    }
}