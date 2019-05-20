package com.shykun.volodymyr.videoeditor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_option.view.*


class ActionAdapter : RecyclerView.Adapter<ActionViewHolder>() {

    private val clickSubject = PublishSubject.create<Action>()
    val clickObservable: Observable<Action> = clickSubject

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ActionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_option, parent, false)
        return ActionViewHolder(view)
    }

    override fun getItemCount() = actions.size

    override fun onBindViewHolder(viewHolder: ActionViewHolder, position: Int) =
        viewHolder.bind(actions[position], clickSubject)

}

class ActionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(action: Action, clickSubject: PublishSubject<Action>) {
        itemView.optionName.text = action.name
        itemView.optionIcon.setImageResource(action.iconResourceId)

        itemView.setOnClickListener {
            clickSubject.onNext(action)
        }
    }
}

