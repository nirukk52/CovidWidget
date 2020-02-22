package com.android.example.github.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.android.example.github.AppExecutors
import com.android.example.github.R
import com.android.example.github.databinding.CellItemBinding
import com.android.example.github.databinding.ContributorItemBinding
import com.android.example.github.ui.common.DataBoundListAdapter
import com.android.example.github.vo.Contributor
import com.verkada.endpoint.kotlin.Cell

class CellAdapter (
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val callback: ((Cell) -> Unit)?
) : DataBoundListAdapter<Cell, CellItemBinding>(
appExecutors = appExecutors,
diffCallback = object : DiffUtil.ItemCallback<Cell>() {
    override fun areItemsTheSame(oldItem: Cell, newItem: Cell): Boolean {
        return false;
    }

    override fun areContentsTheSame(oldItem: Cell, newItem: Cell): Boolean {
        return false;
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
//        binding.root.setOnClickListener {
//            binding.contributor?.let {
//                callback?.invoke(it, binding.imageView)
//            }
//        }
        return binding
    }

    override fun bind(binding: CellItemBinding, item: Cell) {
        binding.cell = item
    }

}