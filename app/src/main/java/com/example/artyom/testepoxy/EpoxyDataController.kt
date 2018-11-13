package com.example.artyom.testepoxy

import android.support.v7.widget.AppCompatRadioButton
import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.TypedEpoxyController
import kotlinx.android.synthetic.main.row_layout.view.radioButton
import kotlinx.android.synthetic.main.row_layout.view.textViewTitle

interface OnSelectedListener{
    fun onViewModelSelected(data: ViewModelData)
}

@EpoxyModelClass(layout = R.layout.row_layout)
abstract class ViewModel : EpoxyModelWithHolder<ViewModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: ViewModelData

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onSelectedListener: OnSelectedListener? = null

    override fun bind(holder: Holder) {
        holder.radioButton.isChecked = data.state == ViewModelData.State.EXPANDED
        holder.radioButton.setOnCheckedChangeListener { compoundButton, isChecked ->
            onSelectedListener?.onViewModelSelected(data)
        }
        holder.title.text = data.titleText
    }

    class Holder : EpoxyHolder() {

        lateinit var radioButton: AppCompatRadioButton
        lateinit var title: TextView

        override fun bindView(itemView: View) {
            radioButton = itemView.radioButton
            title = itemView.textViewTitle
        }

    }

}

data class ViewModelData(
    val id: Long,
    val titleText: String,
    var state: State
) {
    enum class State {
        COLLAPSED,
        EXPANDED
    }
}

class EpoxyDataController : TypedEpoxyController<List<ViewModelData>>(), OnSelectedListener {

    lateinit var data: List<ViewModelData>

    override fun buildModels(data: List<ViewModelData>) {
        this.data = data
        for ((index, item) in this.data.withIndex()) {
            add(ViewModel_()
                .data(item)
                .onSelectedListener(this)
                .id(index))
        }
    }

    override fun onViewModelSelected(data: ViewModelData) {
        this.data = this.data.map {
            if (it.id == data.id) {
                it.state = ViewModelData.State.EXPANDED
            } else {
                it.state = ViewModelData.State.COLLAPSED
            }
            return@map it
        }
        setData(this.data)
    }
}