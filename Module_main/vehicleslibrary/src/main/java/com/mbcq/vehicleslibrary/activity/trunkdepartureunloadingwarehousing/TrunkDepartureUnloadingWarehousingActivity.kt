package com.mbcq.vehicleslibrary.activity.trunkdepartureunloadingwarehousing


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.mbcq.baselibrary.dialog.common.TalkSureDialog
import com.mbcq.baselibrary.ui.BaseListMVPActivity
import com.mbcq.baselibrary.ui.mvp.BaseMVPActivity
import com.mbcq.baselibrary.ui.onSingleClicks
import com.mbcq.baselibrary.view.BaseRecyclerAdapter
import com.mbcq.baselibrary.view.LocalEntity
import com.mbcq.baselibrary.view.SingleClick
import com.mbcq.commonlibrary.ARouterConstants
import com.mbcq.vehicleslibrary.R
import kotlinx.android.synthetic.main.activity_trunk_departure_unloading_warehousing.*
import org.json.JSONObject
import java.lang.StringBuilder


/**
 * @author: lzy
 * @time: 2020-12-31 16:26:03 干线卸车入库
 */

@Route(path = ARouterConstants.TrunkDepartureUnloadingWarehousingActivity)
class TrunkDepartureUnloadingWarehousingActivity : BaseListMVPActivity<TrunkDepartureUnloadingWarehousingContract.View, TrunkDepartureUnloadingWarehousingPresenter, TrunkDepartureUnloadingWarehousingBean>(), TrunkDepartureUnloadingWarehousingContract.View {
    @Autowired(name = "TrunkDepartureUnloadingWarehousing")
    @JvmField
    var mTrunkDepartureUnloadingWarehousing: String = ""
    val mTabEntities: ArrayList<CustomTabEntity> = ArrayList()
    var mTrunkDepartureUnloadingWarehousingReceiptAdapter: TrunkDepartureUnloadingWarehousingReceiptAdapter? = null

    override fun getLayoutId(): Int = R.layout.activity_trunk_departure_unloading_warehousing
    override fun initExtra() {
        super.initExtra()
        ARouter.getInstance().inject(this)

    }

    @SuppressLint("SetTextI18n")
    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        setStatusBar(R.color.base_blue)
        initToptab()
        departure_tv.text = "发车批次：${JSONObject(mTrunkDepartureUnloadingWarehousing).optString("inoneVehicleFlag")}"
        web_info_tv.text = "运行区间：${JSONObject(mTrunkDepartureUnloadingWarehousing).optString("vehicleInterval")}"
        over_total_info_tv.text = "已 装  车：${JSONObject(mTrunkDepartureUnloadingWarehousing).optString("ps")}票 x件 ${JSONObject(mTrunkDepartureUnloadingWarehousing).optString("weight")}Kg ${JSONObject(mTrunkDepartureUnloadingWarehousing).optString("volumn")}方     ${JSONObject(mTrunkDepartureUnloadingWarehousing).optString("yf")}元"
        mTrunkDepartureUnloadingWarehousingReceiptAdapter = TrunkDepartureUnloadingWarehousingReceiptAdapter(mContext).also {
            receipt_list_recycler.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            receipt_list_recycler.adapter = it
        }
        if (JSONObject(mTrunkDepartureUnloadingWarehousing).optBoolean("isLookInfo", false)) {
            bottom_ll.visibility = View.GONE
        }
    }

    override fun initDatas() {
        super.initDatas()
        mPresenter?.getVehicleInfo(JSONObject(mTrunkDepartureUnloadingWarehousing).optString("inoneVehicleFlag"))
        mPresenter?.getVehicleReceiptInfo(JSONObject(mTrunkDepartureUnloadingWarehousing).optString("inoneVehicleFlag"))
    }

    private fun initToptab() {
        mTabEntities.add(LocalEntity("本车清单"))
        mTabEntities.add(LocalEntity("回单清单"))
        trunk_departure_unloading_warehousing_tabLayout.setTabData(mTabEntities)
        trunk_departure_unloading_warehousing_tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                if (position == 0) {
                    this_car_list_recycler.visibility = View.VISIBLE
                    receipt_list_recycler.visibility = View.GONE
                    complete_vehicle_btn.text = "卸车入库"
                    all_selected_checked.visibility = View.VISIBLE
                    printed_selected_checked.visibility = View.GONE
                } else if (position == 1) {
                    this_car_list_recycler.visibility = View.GONE
                    receipt_list_recycler.visibility = View.VISIBLE
                    complete_vehicle_btn.text = "打印"
                    all_selected_checked.visibility = View.GONE
                    printed_selected_checked.visibility = View.VISIBLE
                }
            }

            override fun onTabReselect(position: Int) {

            }
        })
    }

    override fun onClick() {
        super.onClick()
        complete_vehicle_btn.apply {
            onSingleClicks {
                if (complete_vehicle_btn.text == "卸车入库") {
                    var isSelected = false
                    for (item in adapter.getAllData()) {
                        if (item.isChecked) {
                            isSelected = true
                            break
                        }
                    }
                    if (!isSelected) {
                        showToast("请至少选择一件运单进行卸车入库")
                        return@onSingleClicks
                    }
                    val billnoSsbuilder = StringBuilder()
                    for (item in adapter.getAllData()) {
                        if (item.isChecked) {
                            billnoSsbuilder.append(item.billno).append(",")
                        }
                    }
                    mPresenter?.UnloadingWarehousing(billnoSsbuilder.toString().substring(0, billnoSsbuilder.length - 1), JSONObject(mTrunkDepartureUnloadingWarehousing).optString("inoneVehicleFlag"))
                }
            }
        }
        trunk_departure_unloading_warehousing_toolbar.setBackButtonOnClickListener(object : SingleClick() {
            override fun onSingleClick(v: View?) {
                onBackPressed()
            }

        })
    }

    override fun getRecyclerViewId(): Int = R.id.this_car_list_recycler

    override fun setAdapter(): BaseRecyclerAdapter<TrunkDepartureUnloadingWarehousingBean> = TrunkDepartureUnloadingWarehousingAdapter(mContext).also {
        all_selected_checked.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.text.contains("全选")) {
                it.checkedAll(isChecked)
            }
        }
    }

    override fun getVehicleInfoS(list: List<TrunkDepartureUnloadingWarehousingBean>) {
        adapter.appendData(list)

    }

    override fun getVehicleReceiptInfoS(list: List<TrunkDepartureUnloadingWarehousingBean>) {
        mTrunkDepartureUnloadingWarehousingReceiptAdapter?.appendData(list)

    }

    override fun UnloadingWarehousingS(result: String) {
        TalkSureDialog(mContext, getScreenWidth(), "$result 卸车入库成功,点击返回！") {
            onBackPressed()
        }.show()
    }
}