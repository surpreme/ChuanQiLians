package com.mbcq.orderlibrary.activity.receipt.receiptreceive

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.mbcq.baselibrary.dialog.dialogfragment.BaseDialogFragment
import com.mbcq.baselibrary.dialog.popup.XDialog
import com.mbcq.baselibrary.interfaces.OnClickInterface
import com.mbcq.baselibrary.util.screen.ScreenSizeUtils
import com.mbcq.baselibrary.view.SingleClick
import com.mbcq.commonlibrary.adapter.BaseTextAdapterBean
import com.mbcq.commonlibrary.dialog.TimeDialogUtil
import com.mbcq.orderlibrary.R
import com.mbcq.orderlibrary.activity.receipt.receiptconsignment.ReceiptConsignmentCompleteBottomDialog
import kotlinx.android.synthetic.main.dialog_receipt_receive_compelete.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class ReceiptReceiveCompleteDialog(val mScreenWidth: Int, var mOnResultInterface: OnResultInterface? = null) : BaseDialogFragment() {
    interface OnResultInterface {
        fun onResult(mReceiveDate: String, mState: String)
    }

    override fun setDialogWidth(): Int = mScreenWidth / 10 * 9

    override fun initView(view: View, savedInstanceState: Bundle?) {
        return_situation_ll.setOnClickListener(object : SingleClick() {
            override fun onSingleClick(v: View?) {
                XDialog.Builder(mContext)
                        .setContentView(R.layout.dialog_receipt_consignment_compelete_bottom)
                        .setWidth(ScreenSizeUtils.dip2px(mContext, 200f))
                        .setIsDarkWindow(false)
                        .asCustom(ReceiptConsignmentCompleteBottomDialog(mContext, mutableListOf(BaseTextAdapterBean("正常回单", "正常回单"), BaseTextAdapterBean("无效回单", "无效回单"), BaseTextAdapterBean("超期回单", "超期回单"), BaseTextAdapterBean("收条", "收条"))).also {
                            it.mCclick = object : OnClickInterface.OnRecyclerClickInterface {
                                override fun onItemClick(v: View, position: Int, mResult: String) {
                                    return_situation_tv.text = mResult
                                }

                            }
                        })
                        .show(return_situation_ll)
            }

        })
        return_time_ll.setOnClickListener(object : SingleClick() {
            @SuppressLint("SimpleDateFormat")
            override fun onSingleClick(v: View?) {
                TimeDialogUtil.getChoiceTimer(mContext, OnTimeSelectListener { date, _ ->
                    val mDateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val format: String = mDateFormat.format(date)
                    return_time_tv.text = format

                }, "选择寄出时间", isStartCurrentTime = false, isEndCurrentTime = false, isYear = true, isHM = false, isDialog = true).show(return_time_ll)
            }

        })
        commit_tv.setOnClickListener(object : SingleClick() {
            override fun onSingleClick(v: View?) {
                mOnResultInterface?.onResult(return_time_tv.text.toString(),  return_situation_tv.text.toString() )
            }

        })
//
        dismiss_tv.setOnClickListener(object : SingleClick() {
            override fun onSingleClick(v: View?) {
                dismiss()
            }

        })
    }

    override fun setContentView(): Int = R.layout.dialog_receipt_receive_compelete

}