package com.android.example.github.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.afollestad.dragselectrecyclerview.DragSelectReceiver
import com.afollestad.dragselectrecyclerview.DragSelectTouchListener
import com.afollestad.dragselectrecyclerview.Mode
import com.android.example.github.AppExecutors
import com.android.example.github.R
import com.android.example.github.binding.FragmentDataBindingComponent
import com.android.example.github.databinding.HomeFragmentBinding
import com.android.example.github.di.Injectable
import com.android.example.github.util.autoCleared
import com.verkada.endpoint.kotlin.Cell
import com.verkada.endpoint.kotlin.Motion
import com.verkada.endpoint.kotlin.VKotlinEndpoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), DragSelectReceiver, Injectable {

    val TAG = "HomeFragment";

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<HomeFragmentBinding>()

    private var cellAdapter by autoCleared<CellAdapter>()

    val cellList = arrayListOf<Cell>()
    private val selectedCells = HashSet<Cell>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<HomeFragmentBinding>(
                inflater,
                R.layout.home_fragment,
                container,
                false
        )

        binding = dataBinding

        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR, -7)
        val aroundFive = cal.time

        binding.click.setOnClickListener {

            VKotlinEndpoint.searchMotion(5, 5, aroundFive, 3600) { list: List<Motion>, throwable: Throwable? ->
                Log.d(TAG, list.toString())
            }
        }

        cellList.addAll(generateCells())


        return dataBinding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        fillCells()

    }
    private fun fillCells() {
        binding.lifecycleOwner = viewLifecycleOwner
        val dragSelectTouchListener = DragSelectTouchListener.create(context!!, this) {
            disableAutoScroll()
            mode = Mode.PATH
        }

        val cellAdapter = CellAdapter(dataBindingComponent, appExecutors) { index ,toggle ->
            if(toggle){
                cellList[index].selected = !cellList[index].selected
                cellAdapter.notifyItemChanged(index)
            }else{
                dragSelectTouchListener.setIsActive(true, index)
            }
        }
        this.cellAdapter = cellAdapter
        binding.cellRv.adapter = cellAdapter

        cellAdapter.submitList(cellList)

        binding.cellRv.addOnItemTouchListener(dragSelectTouchListener);
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

    override fun setSelected(index: Int, selected: Boolean) {

        Log.d(TAG,"Cells Changed" + index)
        cellList[index].selected = !cellList[index].selected
        cellAdapter.notifyItemChanged(index)

    }

    override fun isSelected(index: Int): Boolean {
        return cellList[index].selected
    }

    override fun isIndexSelectable(index: Int): Boolean {
        return true
    }

    override fun getItemCount(): Int {
        return cellList.size
    }


}


