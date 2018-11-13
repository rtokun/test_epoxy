package com.example.artyom.testepoxy

import android.support.v7.widget.AppCompatRadioButton
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.transitionseverywhere.ChangeBounds
import com.transitionseverywhere.Fade
import com.transitionseverywhere.TransitionManager
import com.transitionseverywhere.TransitionSet
import kotlinx.android.synthetic.main.row_layout.view.additionalDataContainer
import kotlinx.android.synthetic.main.row_layout.view.borderViewGroup
import kotlinx.android.synthetic.main.row_layout.view.radioButton
import kotlinx.android.synthetic.main.row_layout.view.textViewAdditionalData
import kotlinx.android.synthetic.main.row_layout.view.textViewTitle

enum class CurrencyType {
    FIAT,
    CRYPTO
}

interface OnSelectedListener {
    fun onViewModelSelected(data: ViewModelData)
}

@EpoxyModelClass(layout = R.layout.row_layout)
abstract class ViewModel : EpoxyModelWithHolder<ViewModel.Holder>() {

    @EpoxyAttribute
    lateinit var data: ViewModelData

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onSelectedListener: OnSelectedListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.radioButton.setOnCheckedChangeListener(null)
        holder.radioButton.isChecked = data.isSelected
        holder.radioButton.setOnCheckedChangeListener { compoundButton, isChecked ->
            onSelectedListener?.onViewModelSelected(data)
        }

        holder.title.text = data.titleText

        when (data.currencyType) {
            CurrencyType.FIAT -> {
                holder.tvAdditionalData.visibility = View.GONE
            }
            CurrencyType.CRYPTO -> {
                if (data.animateChanges) {
                    val transitionSet = TransitionSet()
                    transitionSet.ordering = TransitionSet.ORDERING_SEQUENTIAL
                    transitionSet.addTransition(ChangeBounds())
                    TransitionManager.beginDelayedTransition(holder.additionalDataContainer, transitionSet)
                }
                holder.tvAdditionalData.visibility = if (data.isSelected) View.VISIBLE else View.GONE
            }
        }

        if (data.animateChanges) {
            TransitionManager.beginDelayedTransition(holder.borderViewGroup, TransitionSet()
                .addTransition(Fade())
                .addTransition(ChangeBounds()))
        }
        holder.borderViewGroup.visibility = if (data.isSelected) View.VISIBLE else View.INVISIBLE
    }

    class Holder : EpoxyHolder() {

        lateinit var radioButton: AppCompatRadioButton
        lateinit var title: TextView
        lateinit var tvAdditionalData: TextView
        lateinit var additionalDataContainer: ViewGroup
        lateinit var borderViewGroup: CheckableFrameLayout

        override fun bindView(itemView: View) {
            radioButton = itemView.radioButton
            title = itemView.textViewTitle
            tvAdditionalData = itemView.textViewAdditionalData
            additionalDataContainer = itemView.additionalDataContainer
            borderViewGroup = itemView.borderViewGroup
        }

    }

}

data class ViewModelData(
    val id: Long,
    val titleText: String,
    val isSelected: Boolean,
    val currencyType: CurrencyType,
    var animateChanges: Boolean = false
)

class EpoxyDataController : EpoxyController(), OnSelectedListener {

    private lateinit var data: List<ViewModelData>

    override fun buildModels() {
        for (item in this.data) {
            add(
                ViewModel_()
                    .data(item)
                    .onSelectedListener(this)
                    .id(item.id)
            )
        }
    }

    fun setData(data: List<ViewModelData>) {
        this.data = data
        requestModelBuild()
    }

    override fun onViewModelSelected(data: ViewModelData) {
        this.data = this.data.map {
            it.copy(isSelected = it.id == data.id, animateChanges = true)
        }
        requestModelBuild()
    }
}