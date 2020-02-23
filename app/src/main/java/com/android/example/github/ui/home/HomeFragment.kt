package com.android.example.github.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.dragselectrecyclerview.DragSelectReceiver
import com.afollestad.dragselectrecyclerview.DragSelectTouchListener
import com.afollestad.dragselectrecyclerview.Mode
import com.android.example.github.AppExecutors
import com.android.example.github.R
import com.android.example.github.binding.FragmentDataBindingComponent
import com.android.example.github.databinding.HomeFragmentBinding
import com.android.example.github.di.Injectable
import com.android.example.github.util.autoCleared
import com.bumptech.glide.Glide
import com.verkada.endpoint.kotlin.Cell
import com.verkada.endpoint.kotlin.Motion
import com.verkada.endpoint.kotlin.VKotlinEndpoint
import java.util.*
import javax.inject.Inject


class HomeFragment : Fragment(), DragSelectReceiver, Injectable {

    val TAG = "HomeFragment"


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<HomeFragmentBinding>()

    private var cellAdapter by autoCleared<CellAdapter>()

    val motionSearchViewModel: MotionSearchViewModel by activityViewModels {
        viewModelFactory
    }

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

        binding.btMotionSearch.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.showLogs()
            )
        }

        return dataBinding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner

//        Glide.with(this).load("http://ec2-54-187-236-58.us-west-2.compute.amazonaws.com:8021/ios/thumbnail/1569630000.jpg").into(binding.ivMotionView)

        fillCells()

    }

    private fun fillCells() {

        val dragSelectTouchListener = DragSelectTouchListener.create(context!!, this) {
            disableAutoScroll()
            mode = Mode.PATH
        }
        binding.cellRv.addOnItemTouchListener(dragSelectTouchListener)

        val cellAdapter = CellAdapter(dataBindingComponent, appExecutors) { index ,longPress ->
            if(longPress){
                dragSelectTouchListener.setIsActive(true, index)
            } else {
                motionSearchViewModel.toggleCell(index)
            }
        }
        this.cellAdapter = cellAdapter
        binding.cellRv.adapter = cellAdapter
        this.cellAdapter.submitList(motionSearchViewModel.getCells().value)

        motionSearchViewModel.getCells().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d(TAG,"" + it.size)
            this.cellAdapter.notifyDataSetChanged()
        })

    }

    private fun setCellHeight() {
        (binding.cellRv.layoutManager as GridLayoutManager).spanCount = 20
    }

    override fun setSelected(index: Int, selected: Boolean) {
        motionSearchViewModel.toggleCell(index)
    }

    override fun isSelected(index: Int): Boolean {
        return motionSearchViewModel.getCells().value?.get(index)?.selected!!
    }

    override fun isIndexSelectable(index: Int): Boolean {
        return true
    }

    override fun getItemCount(): Int {
        return motionSearchViewModel.getCells().value?.size!!
    }


}


