package com.mbcq.vehicleslibrary.activity.alldeparturerecord.shipmentinventory


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.mbcq.baselibrary.interfaces.OnClickInterface
import com.mbcq.baselibrary.ui.BaseSmartMVPActivity
import com.mbcq.baselibrary.ui.mvp.BaseMVPActivity
import com.mbcq.baselibrary.ui.mvp.UserInformationUtil
import com.mbcq.baselibrary.view.BaseRecyclerAdapter
import com.mbcq.baselibrary.view.SingleClick
import com.mbcq.commonlibrary.ARouterConstants
import com.mbcq.commonlibrary.FilterTimeUtils
import com.mbcq.commonlibrary.WebDbUtil
import com.mbcq.commonlibrary.WebsDbInterface
import com.mbcq.commonlibrary.db.WebAreaDbInfo
import com.mbcq.commonlibrary.dialog.FilterWithTimeDialog
import com.mbcq.vehicleslibrary.R
import com.mbcq.vehicleslibrary.activity.allarrivalrecord.arrivalinventory.ArrivalInventoryAdapter
import kotlinx.android.synthetic.main.activity_shipment_inventory.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: lzy
 * @time: 2020-09-27 09:47:00
 * 发货库存
 */
@Route(path = ARouterConstants.ShipmentInventoryActivity)
class ShipmentInventoryActivity : BaseSmartMVPActivity<ShipmentInventoryContract.View, ShipmentInventoryPresenter, ShipmentInventoryBean>(), ShipmentInventoryContract.View {
    var mStartDateTag = ""
    var mEndDateTag = ""
    var mShippingOutletsTag = ""
    override fun getLayoutId(): Int = R.layout.activity_shipment_inventory
    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        setStatusBar(R.color.base_blue)
    }

    @SuppressLint("SimpleDateFormat")
    override fun initExtra() {
        super.initExtra()
        mStartDateTag = FilterTimeUtils.getStartTime(7)
        mEndDateTag = FilterTimeUtils.getEndTime()
        mShippingOutletsTag = UserInformationUtil.getWebIdCode(mContext)
    }

    override fun getPageDatas(mCurrentPage: Int) {
        super.getPageDatas(mCurrentPage)
        mPresenter?.getPage(mCurrentPage, mShippingOutletsTag, mStartDateTag, mEndDateTag)

    }

    override fun onClick() {
        super.onClick()
        shipment_inventory_toolbar.setRightButtonOnClickListener {
            WebDbUtil.getDbWebId(application, object : WebsDbInterface {
                override fun isNull() {

                }

                override fun isSuccess(list: MutableList<WebAreaDbInfo>) {
                    FilterWithTimeDialog(getScreenWidth(), Gson().toJson(list), "webid", "webidCode", true, "发货库存记录筛选", true, mClickInterface = object : OnClickInterface.OnClickInterface {
                        /**
                         * s1 网点
                         * s2  start@end
                         */
                        override fun onResult(s1: String, s2: String) {
                            mShippingOutletsTag = s1
                            val timeList = s2.split("@")
                            if (timeList.isNotEmpty() && timeList.size == 2) {
                                mStartDateTag = timeList[0]
                                mEndDateTag = timeList[1]
                            }
                            refresh()

                        }

                    }).show(supportFragmentManager, "mArrivalInventoryActivityFilterWithTimeDialog")
                }

            })
        }
        shipment_inventory_toolbar.setBackButtonOnClickListener(object : SingleClick() {
            override fun onSingleClick(v: View?) {
                onBackPressed()
            }

        })
    }


    override fun getSmartLayoutId() = R.id.shipment_inventory_smart
    override fun getSmartEmptyId() = R.id.shipment_inventory_smart_frame
    override fun getRecyclerViewId(): Int = R.id.shipment_inventory_recycler
    override fun setAdapter(): BaseRecyclerAdapter<ShipmentInventoryBean> = ShipmentInventoryAdapter(mContext).also {
        it.mClickInterface = object : OnClickInterface.OnRecyclerClickInterface {
            override fun onItemClick(v: View, position: Int, mResult: String) {
                ARouter.getInstance().build(ARouterConstants.WaybillDetailsActivity).withString("WaybillDetails", mResult).navigation()
            }

        }
    }

    @SuppressLint("SetTextI18n")
    override fun getPageS(list: List<ShipmentInventoryBean>, totalBean: ShipmentInventoryToTalBean, page: Int, count: String) {
        appendDatas(list)
        if (page == 1) {
            shipment_inventory_toolbar.setCenterTitleText("发货库存(${count})")
            all_info_bottom_tv.text = "合计：${totalBean.rowCou}票，${totalBean.weight}kg，${totalBean.volumn}m³，运费${totalBean.accSum}"
        }

    }
}