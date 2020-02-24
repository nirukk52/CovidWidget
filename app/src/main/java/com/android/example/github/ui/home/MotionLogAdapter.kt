package com.android.example.github.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.android.example.github.AppExecutors
import com.android.example.github.R
import com.android.example.github.databinding.MotionLogItemBinding
import com.android.example.github.ui.common.DataBoundListAdapter
import com.verkada.endpoint.kotlin.Motion
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MotionLogAdapter(
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
        return DataBindingUtil
                .inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.motion_log_item,
                        parent,
                        false,
                        dataBindingComponent
                )
    }

    override fun bind(binding: MotionLogItemBinding, item: Motion, index: Int) {
        val dateFormatter = SimpleDateFormat("EEE, dd MMM", Locale.US)
        val timeFormatter = SimpleDateFormat("kk:mm", Locale.US)

        binding.date = dateFormatter.format(item.date)
        binding.time = timeFormatter.format(item.date)

        val seconds = item.durationSeconds
        val minute: Long = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.SECONDS.toHours(seconds) * 60
        val second: Long = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toMinutes(seconds) * 60


        binding.duration = if (minute != 0L) "$minute mins $second sec"
        else "$second sec"
    }

}
