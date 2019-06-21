package com.shykun.volodymyr.videoeditor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shykun.volodymyr.videoeditor.R
import kotlinx.android.synthetic.main.item_color_picker.view.*

interface OnColorPickerClickListener {
    fun onColorPicked(colorCode: Int)
}

class ColorPickerAdapter(context: Context) : RecyclerView.Adapter<ColorPickerViewHolder>() {

    val colors = getColors(context)
    var onColorPickerListener: OnColorPickerClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorPickerViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.item_color_picker, parent, false)
        return ColorPickerViewHolder(view)
    }

    override fun getItemCount() = colors.size

    override fun onBindViewHolder(holder: ColorPickerViewHolder, position: Int) =
        holder.bind(colors[position], onColorPickerListener)
}

class ColorPickerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(colorId: Int, onClolorPickerListener: OnColorPickerClickListener?) {
        itemView.colorPickerView.setBackgroundColor(colorId)
        itemView.setOnClickListener {
            onClolorPickerListener?.onColorPicked(colorId)
        }
    }
}

private fun getColors(context: Context) = arrayOf(
    ContextCompat.getColor(context, R.color.blue_color_picker),
    ContextCompat.getColor(context, R.color.brown_color_picker),
    ContextCompat.getColor(context, R.color.green_color_picker),
    ContextCompat.getColor(context, R.color.orange_color_picker),
    ContextCompat.getColor(context, R.color.black_color_picker),
    ContextCompat.getColor(context, R.color.red_orange_color_picker),
    ContextCompat.getColor(context, R.color.sky_blue_color_picker),
    ContextCompat.getColor(context, R.color.violet_color_picker),
    ContextCompat.getColor(context, R.color.white_color_picker),
    ContextCompat.getColor(context, R.color.yellow_color_picker),
    ContextCompat.getColor(context, R.color.yellow_green_color_picker)
)


