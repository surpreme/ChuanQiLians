package com.mbcq.orderlibrary.activity.signrecord

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mbcq.baselibrary.view.BaseRecyclerAdapter
import com.mbcq.baselibrary.view.SingleClick
import com.mbcq.orderlibrary.R
import com.mbcq.orderlibrary.activity.waybillrecord.WaybillRecordAdapter

class SignRecordRecyclerAdapter(context: Context?) : BaseRecyclerAdapter<SignRecordBean>(context) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = ItemViewHolder(inflater.inflate(R.layout.item_sign_record, parent, false))
    var mOnCancelInterface: OnCancelInterface? = null

    interface OnCancelInterface {
        fun result(v: View, position: Int, item: SignRecordBean)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).waybill_number_tv.text = mDatas[position].billno
        holder.waybill_number_tv.text = mDatas[position].billno
        holder.waybill_time_tv.text = mDatas[position].fetchDate
        context?.let {
            holder.record_checkbox_iv.setImageDrawable(ContextCompat.getDrawable(it, if (mDatas[position].selected) R.drawable.ic_checked_icon else R.drawable.ic_unchecked_icon))
        }
        holder.record_checkbox_iv.setOnClickListener(object : SingleClick() {
            override fun onSingleClick(v: View?) {
                for (index in 0 until mDatas.size) {
                    if (index != position)
                        mDatas[index].selected = false
                    else
                        mDatas[index].selected = !mDatas[index].selected
                }
                notifyDataSetChanged()
            }

        })
        holder.cancel_record_tv.setOnClickListener(object : SingleClick() {
            override fun onSingleClick(v: View) {
                mOnCancelInterface?.result(v, position, mDatas[position])
            }

        })
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var waybill_number_tv: TextView = itemView.findViewById(R.id.waybill_number_tv)
        var record_checkbox_iv: ImageView = itemView.findViewById(R.id.record_checkbox_iv)
        var information_tv: TextView = itemView.findViewById(R.id.information_tv)
        var cancel_record_tv: TextView = itemView.findViewById(R.id.cancel_record_tv)
        var waybill_time_tv: TextView = itemView.findViewById(R.id.waybill_time_tv)
        var shipper_outlets_tv: TextView = itemView.findViewById(R.id.shipper_outlets_tv)
        var receiver_outlets_tv: TextView = itemView.findViewById(R.id.receiver_outlets_tv)
        var receiver_tv: TextView = itemView.findViewById(R.id.receiver_tv)
        var destination_tv: TextView = itemView.findViewById(R.id.destination_tv)
    }
}