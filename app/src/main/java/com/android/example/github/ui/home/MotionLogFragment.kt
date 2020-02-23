package com.android.example.github.ui.home

import com.android.example.github.databinding.MotionLogFragmentBinding
import com.verkada.endpoint.kotlin.MotionSearchBody
import java.sql.Timestamp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.android.example.github.AppExecutors
import com.android.example.github.R
import com.android.example.github.binding.FragmentDataBindingComponent
import com.android.example.github.di.Injectable
import com.android.example.github.util.autoCleared
import com.verkada.endpoint.kotlin.VKotlinEndpoint
import java.util.*
import androidx.lifecycle.Observer

import javax.inject.Inject


class MotionLogFragment : Fragment(), Injectable {

    private val TAG = "MotionLogFragment";

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<MotionLogFragmentBinding>()

    private var motionLogAdapter by autoCleared<MotionLogAdapter>()

    val motionSearchViewModel: MotionSearchViewModel by activityViewModels {
        viewModelFactory
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<MotionLogFragmentBinding>(
                inflater,
                R.layout.motion_log_fragment,
                container,
                false
        )

        binding = dataBinding

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val motionLogAdapter = MotionLogAdapter(dataBindingComponent, appExecutors)
        this.motionLogAdapter = motionLogAdapter
        binding.rvMotionLog.adapter = motionLogAdapter

        val startTimeSec: Long = System.currentTimeMillis()
        val list: MutableList<List<Int>> = ArrayList()

        motionSearchViewModel.getCells().observe(viewLifecycleOwner, Observer {
            it.filter { cell -> cell.selected }.forEach {
                val motion: MutableList<Int> = ArrayList()
                motion.add(it.row)
                motion.add(it.col)
                list.add(motion)
            }

            val body = MotionSearchBody(
                    list,
                    1582265703,
                    1582269303
            )
            VKotlinEndpoint.searchMotion(body) { motionList, _ ->
                Log.d(TAG, motionList.toString())
                this.motionLogAdapter.submitList(motionList)
            }
        })



    }


    private fun getOneHourAgo(startTimeSec: Long): Long {
        val timestamp = Timestamp(startTimeSec - 60 * 60 * 1000)
        return timestamp.time
    }


}


