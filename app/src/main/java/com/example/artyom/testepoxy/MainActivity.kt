package com.example.artyom.testepoxy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        EpoxyDataController().apply {
            mainRecyclerView.adapter = adapter
            setData(getData())
        }
    }

    private fun getData(): List<ViewModelData> {
        val viewModelData1 = ViewModelData(1, "title 1", false, CurrencyType.FIAT)
        val viewModelData2 = ViewModelData(2, "title 2", false, CurrencyType.CRYPTO)
        return listOf(viewModelData1, viewModelData2)
    }
}
