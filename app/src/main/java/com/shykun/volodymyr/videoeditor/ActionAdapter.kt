package com.shykun.volodymyr.videoeditor

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_option.view.*

val actions = arrayOf(
    "Cut",
    "Reverse",
    "Compress",
    "Fast Motion",
    "Slow Motion"
)
val iconResourceIds = arrayOf(
    R.drawable.ic_cut_white_24dp,
    R.drawable.ic_error_outline_black_24dp,
    R.drawable.ic_error_outline_black_24dp,
    R.drawable.ic_error_outline_black_24dp,
    R.drawable.ic_error_outline_black_24dp
)

class ActionAdapter : RecyclerView.Adapter<ActionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ActionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_option, parent, false)
        return ActionViewHolder(view)
    }

    override fun getItemCount() = actions.size

    override fun onBindViewHolder(viewHolder: ActionViewHolder, position: Int) =
        viewHolder.bind(actions[position], iconResourceIds[position])

}

class ActionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(actionName: String, actionImageResource: Int) {
        itemView.optionName.text = actionName
        itemView.optionIcon.setImageResource(actionImageResource)
    }
}