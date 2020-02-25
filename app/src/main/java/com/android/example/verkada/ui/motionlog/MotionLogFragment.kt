package com.android.example.verkada.ui.motionlog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.example.verkada.AppExecutors
import com.android.example.verkada.R
import com.android.example.verkada.binding.FragmentDataBindingComponent
import com.android.example.verkada.databinding.MotionLogFragmentBinding
import com.android.example.verkada.di.Injectable
import com.android.example.verkada.util.autoCleared
import com.android.example.verkada.viewmodel.MotionSearchViewModel
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

    /**
     * Tested times:
     * starttime 1582265703
     * endtime 1582269303
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val motionLogAdapter = MotionLogAdapter(dataBindingComponent, appExecutors)
        this.motionLogAdapter = motionLogAdapter
        binding.rvMotionLog.adapter = motionLogAdapter

        val currentTime: Long = System.currentTimeMillis() / 1000
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
                    currentTime - 3600,
                    currentTime
            )

            VKotlinEndpoint.searchMotion(body) { motionList, _ ->
                if(motionList.isNotEmpty()){
                    this.motionLogAdapter.submitList(motionList)
                    binding.tvLogStatus.visibility = View.GONE
                } else {
                    binding.tvLogStatus.text = getString(R.string.status_no_logs)
                }
            }
        })
    }

}


