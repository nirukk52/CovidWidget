package com.android.example.github.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.example.github.AppExecutors
import com.android.example.github.R
import com.android.example.github.binding.FragmentDataBindingComponent
import com.android.example.github.databinding.MotionLogFragmentBinding
import com.android.example.github.di.Injectable
import com.android.example.github.util.autoCleared
import com.google.gson.Gson
import com.verkada.endpoint.kotlin.MotionSearchBody
import com.verkada.endpoint.kotlin.VKotlinEndpoint
import java.util.*
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

    private val motionSearchViewModel: MotionSearchViewModel by activityViewModels {
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

        val currentTime: Long = System.currentTimeMillis() / 1000L
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
                    currentTime - 3600000,
                    currentTime
            )
            Log.d("Call ", Gson().toJson(body))
            VKotlinEndpoint.searchMotion(body) { motionList, _ ->
                if(motionList.isNotEmpty()){
                    this.motionLogAdapter.submitList(motionList)
                    binding.tvLogStatus.visibility = View.GONE
                } else {
                    binding.tvLogStatus.text = getString(R.string.status_no_logs)
                }
            }
        })

//        starttime 1582265703
//        endtime 1582269303


    }

}


