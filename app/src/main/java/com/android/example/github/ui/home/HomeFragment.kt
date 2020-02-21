package com.android.example.github.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.example.github.R
import com.android.example.github.api.VJavaEndpoint
import com.android.example.github.binding.FragmentDataBindingComponent
import com.android.example.github.databinding.HomeFragmentBinding
import com.android.example.github.util.autoCleared
import com.verkada.endpoint.kotlin.Motion
import com.verkada.endpoint.kotlin.VKotlinEndpoint
import java.util.*


class HomeFragment : Fragment(){

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<HomeFragmentBinding>()


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
//
//
//            vj.searchMotion(0, 1, Date(), 3600) { motionList, error ->
//                Log.d(TAG,motionList.toString())
//            }
        }
        return dataBinding.root;
    }
}