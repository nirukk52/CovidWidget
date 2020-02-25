package com.android.example.verkada.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.verkada.testing.OpenForTesting
import com.verkada.endpoint.kotlin.Cell
import java.util.*
import javax.inject.Inject


@OpenForTesting
class MotionSearchViewModel @Inject constructor() :  ViewModel() {

    val TAG = "MotionSearchViewModel"

    private val cells: MutableLiveData<ArrayList<Cell>> =  MutableLiveData(generateCells())

    private val cellUpdateIndex: MutableLiveData<Int> =  MutableLiveData(-1)

    fun getCells(): LiveData<ArrayList<Cell>>{
        return cells
    }

    fun getCellUpdateIndex(): LiveData<Int>{
        return cellUpdateIndex
    }

    fun setCellUpdateIndex(index: Int){
         cellUpdateIndex.value = index
    }

    fun toggleCell(index : Int){
        cells.value?.get(index)?.selected = !cells.value?.get(index)?.selected!!
        cellUpdateIndex.value = index
        Log.d(TAG, cells.value?.get(index).toString())
    }

    /**
     * Try changing this value to row = 19, col = 19.
     * Still equally spaced grids are displayed.
     */
    private fun generateCells(row: Int = 9, col: Int = 9): ArrayList<Cell> {
        val cellList = arrayListOf<Cell>()
        for (i in 0..row) {
            for (j in 0..col) {
                cellList.add(Cell(i, j))
            }
        }
        return cellList
    }

    fun clearSelection(){
        cells.value?.clear()
        cellUpdateIndex.value = -1
        cells.value?.addAll(generateCells())
    }
}