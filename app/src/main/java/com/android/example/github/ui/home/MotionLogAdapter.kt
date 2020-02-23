package com.android.example.github.ui.home

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.android.example.github.AppExecutors
import com.android.example.github.R
import com.android.example.github.databinding.CellItemBinding
import com.android.example.github.databinding.MotionLogItemBinding
import com.android.example.github.ui.common.DataBoundListAdapter
import com.verkada.endpoint.kotlin.Cell
import com.verkada.endpoint.kotlin.Motion

class MotionLogAdapter (
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors
) : DataBoundListAdapter<Motion, MotionLogItemBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<Motion>() {
            override fun areItemsTheSame(oldItem: Motion, newItem: Motion): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: Motion, newItem: Motion): Boolean {
                return oldItem.durationSeconds == newItem.durationSeconds
            }
        }
) {

    override fun createBinding(parent: ViewGroup): MotionLogItemBinding {
        val binding = DataBindingUtil
                .inflate<MotionLogItemBinding>(
                        LayoutInflater.from(parent.context),
                        R.layout.motion_log_item,
                        parent,
                        false,
                        dataBindingComponent
                )
        return binding
    }

    override fun bind(binding: MotionLogItemBinding, item: Motion, index: Int) {
        binding.date = item.date.toString() + " Duration: "+ item.durationSeconds
    }

}