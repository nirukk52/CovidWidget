package com.android.example.github.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.android.example.github.AppExecutors
import com.android.example.github.R
import com.android.example.github.api.VJavaEndpoint
import com.android.example.github.binding.FragmentDataBindingComponent
import com.android.example.github.databinding.HomeFragmentBinding
import com.android.example.github.ui.repo.ContributorAdapter
import com.android.example.github.ui.repo.RepoFragmentDirections
import com.android.example.github.util.autoCleared
import com.verkada.endpoint.kotlin.Cell
import com.verkada.endpoint.kotlin.Motion
import com.verkada.endpoint.kotlin.VKotlinEndpoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class HomeFragment : Fragment()  {

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<HomeFragmentBinding>()



    @Inject
    lateinit var appExecutors: AppExecutors

    val TAG = "HomeFragment";

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

            VKotlinEndpoint.searchMotion(5,5, aroundFive,3600){ list: List<Motion>, throwable: Throwable? ->
                Log.d(TAG,list.toString())
            }
//            val vj = VJavaEndpoint()
//            vj.searchMotion(0, 1, Date(),  3600) { motionList, error ->
//                Log.d(TAG,motionList.toString())
//            }
        }



        val adapter = CellAdapter(dataBindingComponent, appExecutors) {
            cell ->

        }




        fillCells(generateCells(9,9))


        return dataBinding.root;
    }

    fun fillCells(cellList: ArrayList<Cell>){
//        val adapter = MotionView.CellAdapter(cellList, context!!)
//        binding.cellRv.adapter = adapter
//        val v = DragSelectTouchListener.create(context, adapter) {
//            disableAutoScroll()
//            mode = Mode.RANGE
//        }
//
//        cellRv.addOnItemTouchListener(v);
//        v.setIsActive(true, 0)

    }

    private fun generateCells(row:Int = 9, col:Int = 9) : ArrayList<Cell>{
        val cellList = arrayListOf<Cell>()
        for (i in 0..row) {
            for (j in 0..col) {
                cellList.add(Cell(i,j))
            }
        }
        return cellList;
    }



}


