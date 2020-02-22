package com.android.example.github.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.example.github.R
import com.verkada.endpoint.kotlin.Cell


//class MotionView @JvmOverloads
//constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
//    : FrameLayout(context, attrs, defStyleAttr){
//
//    lateinit var cellRv: RecyclerView
//
//    init {
//        context.theme.obtainStyledAttributes(
//                attrs,
//                R.styleable.MotionView,
//                0, 0).apply {
//
//            try {
////                textPos = getInteger(R.styleable.PieChart_labelPosition, 0)
//            } finally {
//                recycle()
//            }
//        }
//
//        LayoutInflater.from(context).inflate(R.layout.motion_view, this, true)
//
//        cellRv = findViewById<RecyclerView>(R.id.cellRv)
//
//
//    }
//
//    fun fillCells(cellList: ArrayList<Cell>){
//        val adapter = CellAdapter(cellList,context)
//        cellRv.adapter = adapter
////        val v = DragSelectTouchListener.create(context, adapter) {
////            disableAutoScroll()
////            mode = Mode.RANGE
////        }
//
//        cellRv.addOnItemTouchListener(v);
//        v.setIsActive(true, 0)
//
//    }
//
//    class CellAdapter(private val cellList: ArrayList<Cell>, private val context: Context) : DragSelectReceiver,
//            RecyclerView.Adapter<CellAdapter.ViewHolder>() {
//
//        val TAG = "CellAdapter";
//
//        private val selectedCells =  HashSet<Cell>()
//
//        override fun setSelected(index: Int, selected: Boolean) {
//
//            if(selected){
//                selectedCells.add(cellList[index])
//            }else{
//                selectedCells.remove(cellList[index])
//            }
//
//        }
//
//        override fun isSelected(index: Int): Boolean {
//            return selectedCells.contains(cellList.get(index))
//        }
//
//        override fun isIndexSelectable(index: Int): Boolean {
//            return true
//        }
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//
//            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.cell_item, parent, false))
//
//        }
//
//        override fun getItemCount(): Int {
//            return cellList.size
//        }
//
//        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//
//            holder.cellTv.setText(" ${cellList.get(position).row} ${cellList.get(position).col}")
//        }
//
//        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//            var cellTv: TextView
//
//            init {
//                cellTv = itemView.findViewById<View>(R.id.cellTv) as TextView
//            }
//        }
//
//}
//
//
//
//}