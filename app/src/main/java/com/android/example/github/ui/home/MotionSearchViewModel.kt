package com.android.example.github.ui.home


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.android.example.github.repository.MotionSenseRepo
import com.android.example.github.repository.RepoRepository
import com.android.example.github.testing.OpenForTesting
import com.android.example.github.ui.repo.RepoViewModel
import com.android.example.github.util.AbsentLiveData
import com.android.example.github.vo.Contributor
import com.android.example.github.vo.Repo
import com.android.example.github.vo.Resource
import com.verkada.endpoint.kotlin.Cell
import com.verkada.endpoint.kotlin.Motion
import com.verkada.endpoint.kotlin.MotionSearchBody
import com.verkada.endpoint.kotlin.MotionSearchResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@OpenForTesting
class MotionSearchViewModel @Inject constructor(repository: RepoRepository):  ViewModel() {

    val TAG = "MotionSearchViewModel"

    private val cells: MutableLiveData<ArrayList<Cell>> =  MutableLiveData(generateCells())

    fun getCells(): LiveData<ArrayList<Cell>>{
        return cells
    }

    fun toggleCell(index : Int){
        cells.value?.get(index)?.selected = !cells.value?.get(index)?.selected!!
        cells.value = cells.value
    }




    private fun generateCells(row: Int = 9, col: Int = 9): ArrayList<Cell> {
        val cellList = arrayListOf<Cell>()
        for (i in 0..row) {
            for (j in 0..col) {
                cellList.add(Cell(i, j))
            }
        }
        return cellList;
    }

    fun getOneHourAgo(startTimeSec : Long) : Long{
        val c: Calendar = Calendar.getInstance()
        val timestamp = Timestamp(startTimeSec - 60 * 60 * 1000)
        return timestamp.getTime()

    }

}