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
        val viewModelData1 = ViewModelData(1, "title 1", ViewModelData.State.EXPANDED)
        val viewModelData2 = ViewModelData(2, "title 2", ViewModelData.State.COLLAPSED)
        val viewModelData3 = ViewModelData(3, "title 3", ViewModelData.State.COLLAPSED)
        val viewModelData4 = ViewModelData(4, "title 4", ViewModelData.State.COLLAPSED)
        return listOf(viewModelData1, viewModelData2, viewModelData3, viewModelData4)
    }
}
