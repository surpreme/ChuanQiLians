package com.mbcq.orderlibrary.fragment.waybillinformation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mbcq.baselibrary.view.BaseRecyclerAdapter
import com.mbcq.orderlibrary.R

class WaybillInformationTableAdapter (context: Context):BaseRecyclerAdapter<WaybillInformationTableBean>(context) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = ItemViewHolder(inflater.inflate(R.layout.item_waybill_information_table, parent, false))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).product_tv.text = mDatas[position].product
        holder.qty_tv.text = mDatas[position].qty
        holder.packages_tv.text = mDatas[position].packages
        holder.weight_tv.text = mDatas[position].weight /*+ "KG"*/
        holder.volumn_tv.text = mDatas[position].volumn /*+ "m³"*/
        holder.itemView.setBackgroundColor(if (mDatas[position].isTitles) Color.parseColor("#686C74")else  Color.parseColor("#005CFF"))
    }
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var product_tv = itemView.findViewById<TextView>(R.id.product_tv)//货物名称
        var qty_tv = itemView.findViewById<TextView>(R.id.qty_tv)//件数
        var packages_tv = itemView.findViewById<TextView>(R.id.packages_tv)//包装方式
        var weight_tv = itemView.findViewById<TextView>(R.id.weight_tv)//重量
        var volumn_tv = itemView.findViewById<TextView>(R.id.volumn_tv)//体积
    }
}