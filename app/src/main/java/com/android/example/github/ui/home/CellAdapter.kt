package com.android.example.github.ui.home

import android.view.LayoutInflater
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.android.example.github.AppExecutors
import com.android.example.github.R
import com.android.example.github.databinding.CellItemBinding
import com.android.example.github.ui.common.DataBoundListAdapter
import com.verkada.endpoint.kotlin.Cell

class CellAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val cellClickCallback: ((Int, Boolean) -> Unit)?
) : DataBoundListAdapter<Cell, CellItemBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<Cell>() {
            override fun areItemsTheSame(oldItem: Cell, newItem: Cell): Boolean {
                return oldItem.row == newItem.row && oldItem.col == newItem.col
            }

            override fun areContentsTheSame(oldItem: Cell, newItem: Cell): Boolean {
                return oldItem.selected == newItem.selected;
            }
        }
) {

    override fun createBinding(parent: ViewGroup): CellItemBinding {
        val binding = DataBindingUtil
                .inflate<CellItemBinding>(
                        LayoutInflater.from(parent.context),
                        R.layout.cell_item,
                        parent,
                        false,
                        dataBindingComponent
                )
        binding.root.setOnClickListener {
            binding.index?.let {
                cellClickCallback?.invoke(it, true)
            }
        }
        binding.root.setOnLongClickListener {
            binding.index?.let {
                cellClickCallback?.invoke(it, false)
            }
            false
        }
        return binding
    }

    override fun bind(binding: CellItemBinding, item: Cell, index: Int) {
        binding.cell = item
        binding.index = index
    }


}