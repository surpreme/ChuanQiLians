package com.mbcq.vehicleslibrary.activity.alllocalagent.fixlocalgentshortfeederhouse


import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.mbcq.baselibrary.dialog.common.TalkSureCancelDialog
import com.mbcq.baselibrary.dialog.common.TalkSureDialog
import com.mbcq.baselibrary.gson.GsonUtils
import com.mbcq.baselibrary.ui.mvp.BaseMVPActivity
import com.mbcq.baselibrary.ui.mvp.BasePresenterImpl
import com.mbcq.baselibrary.ui.mvp.BaseView
import com.mbcq.baselibrary.util.screen.ScreenSizeUtils
import com.mbcq.baselibrary.view.BaseItemDecoration
import com.mbcq.baselibrary.view.SingleClick
import com.mbcq.commonlibrary.ARouterConstants
import com.mbcq.vehicleslibrary.R
import com.mbcq.vehicleslibrary.activity.alllocalagent.localgentshortfeederhouse.LocalGentShortFeederHouseBean
import com.mbcq.vehicleslibrary.activity.alllocalagent.localgentshortfeederhouse.LocalGentShortFeederHouseInventoryAdapter
import com.mbcq.vehicleslibrary.activity.alllocalagent.localgentshortfeederhouse.LocalGentShortFeederHouseLoadingAdapter
import com.mbcq.vehicleslibrary.bean.StockWaybillListBean
import kotlinx.android.synthetic.main.activity_fixed_local_gent_short_feeder_house.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * @author: lzy
 * @time: 2020-09-23 11:05:00
 */
abstract  class BaseFixedLocalGentShortFeederHouseActivity<V : BaseView, T : BasePresenterImpl<V>> : BaseMVPActivity<V,T>(),BaseView {


    var mDepartureLot = ""

    override fun initExtra() {
        super.initExtra()
        ARouter.getInstance().inject(this)

    }

    @SuppressLint("SetTextI18n")
    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        setStatusBar(R.color.base_blue)
        initLoadingList()
        initInventoryList()


    }




    fun removeSomeThing() {
        val mSelectedDatas = mutableListOf<LocalGentShortFeederHouseBean>()
        val mUnSelectedDatas = mutableListOf<LocalGentShortFeederHouseBean>()
        mLoadingListAdapter?.getAllData()?.let {
            for ((index, item) in (it.withIndex())) {
                if (item.isChecked) {
                    item.isChecked = false
                    mSelectedDatas.add(item)
                } else {
                    mUnSelectedDatas.add(item)
                }
            }
            mInventoryListAdapter?.appendData(mSelectedDatas)
            mLoadingListAdapter?.clearData()
            mLoadingListAdapter?.appendData(mUnSelectedDatas)
            if (all_selected_checked.isChecked) {
                all_selected_checked.isChecked = false
            }
        }
    }

    fun addSomeThing() {
        val mSelectedDatas = mutableListOf<LocalGentShortFeederHouseBean>()
        val mUnSelectedDatas = mutableListOf<LocalGentShortFeederHouseBean>()
        mInventoryListAdapter?.getAllData()?.let {
            for ((index, item) in (it.withIndex())) {
                if (item.isChecked) {
                    item.isChecked = false
                    mSelectedDatas.add(item)
                } else {
                    mUnSelectedDatas.add(item)
                }
            }
            mLoadingListAdapter?.appendData(mSelectedDatas)
            mInventoryListAdapter?.clearData()
            mInventoryListAdapter?.appendData(mUnSelectedDatas)
            if (all_selected_checked.isChecked) {
                all_selected_checked.isChecked = false
            }

        }

    }

    var mInventoryListAdapter: LocalGentShortFeederHouseInventoryAdapter? = null
    var mLoadingListAdapter: LocalGentShortFeederHouseLoadingAdapter? = null
     protected open fun initLoadingList() {
        loading_list_recycler.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        mLoadingListAdapter = LocalGentShortFeederHouseLoadingAdapter(mContext)
        loading_list_recycler.adapter = mLoadingListAdapter
        if (loading_list_recycler.itemDecorationCount == 0) {
            loading_list_recycler.addItemDecoration(object : BaseItemDecoration(mContext) {
                override fun configExtraSpace(position: Int, count: Int, rect: Rect) {
                    rect.top = ScreenSizeUtils.dp2px(mContext, 10f)
                }

                override fun doRule(position: Int, rect: Rect) {
                    rect.bottom = rect.top
                }
            })
        }
    }

    var mTypeIndex = 1

    protected open fun initInventoryList() {
        inventory_list_recycler.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        mInventoryListAdapter = LocalGentShortFeederHouseInventoryAdapter(mContext)
        inventory_list_recycler.adapter = mInventoryListAdapter

        if (inventory_list_recycler.itemDecorationCount == 0) {
            inventory_list_recycler.addItemDecoration(object : BaseItemDecoration(mContext) {
                override fun configExtraSpace(position: Int, count: Int, rect: Rect) {
                    rect.top = ScreenSizeUtils.dp2px(mContext, 10f)
                }

                override fun doRule(position: Int, rect: Rect) {
                    rect.bottom = rect.top
                }
            })
        }
    }

    protected fun selectIndex(type: Int) {
        mTypeIndex = type
        when (type) {
            1 -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    inventory_list_tv.setTextColor(getColor(R.color.base_blue))
                    inventory_list_line.background = ContextCompat.getDrawable(mContext, R.color.base_blue)
                    loading_list_tv.setTextColor(getColor(R.color.black))
                    loading_list_line.background = ContextCompat.getDrawable(mContext, R.color.white)
                } else {
                    inventory_list_tv.setTextColor(resources.getColor(R.color.base_blue))
                    inventory_list_line.background = ContextCompat.getDrawable(mContext, R.color.base_blue)
                    loading_list_tv.setTextColor(resources.getColor(R.color.black))
                    loading_list_line.background = ContextCompat.getDrawable(mContext, R.color.white)
                }
                inventory_list_recycler.visibility = View.VISIBLE
                loading_list_recycler.visibility = View.GONE
                operating_btn.text = "添加本车"
            }
            2 -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    loading_list_tv.setTextColor(getColor(R.color.base_blue))
                    loading_list_line.background = ContextCompat.getDrawable(mContext, R.color.base_blue)
                    inventory_list_tv.setTextColor(getColor(R.color.black))
                    inventory_list_line.background = ContextCompat.getDrawable(mContext, R.color.white)
                } else {
                    loading_list_tv.setTextColor(resources.getColor(R.color.base_blue))
                    loading_list_line.background = ContextCompat.getDrawable(mContext, R.color.base_blue)
                    inventory_list_tv.setTextColor(resources.getColor(R.color.black))
                    inventory_list_line.background = ContextCompat.getDrawable(mContext, R.color.white)
                }
                loading_list_recycler.visibility = View.VISIBLE
                inventory_list_recycler.visibility = View.GONE
                operating_btn.text = "移出本车"

            }
        }
    }

    fun getSelectInventoryList(): List<LocalGentShortFeederHouseBean>? {
        val mSelectedDatas = mutableListOf<LocalGentShortFeederHouseBean>()
        mInventoryListAdapter?.getAllData()?.let {
            for ((index, item) in (it.withIndex())) {
                if (item.isChecked) {
                    mSelectedDatas.add(item)
                }
            }
            return mSelectedDatas

        }
        return null

    }

    @SuppressLint("SetTextI18n")
    fun showTopTotal() {
        inventory_list_tv.text = "库存清单(${mInventoryListAdapter?.getAllData()?.size})"
        loading_list_tv.text = "配载清单(${mLoadingListAdapter?.getAllData()?.size})"
    }

    override fun onClick() {
        super.onClick()
        local_gent_short_feeder_house_toolbar.setBackButtonOnClickListener(object : SingleClick() {
            override fun onSingleClick(v: View?) {
                onBackPressed()
            }

        })
    }

    fun getSelectInventoryOrder(): String {
        val mSelectWaybillNumber = java.lang.StringBuilder()
        mInventoryListAdapter?.getAllData()?.let {
            for ((index, item) in (it.withIndex())) {
                if (item.isChecked) {
                    mSelectWaybillNumber.append(item.billno)
                    if (index != it.size - 1)
                        mSelectWaybillNumber.append(",")
                }
            }
        }
        return mSelectWaybillNumber.toString()
    }

    fun getSelectLoadingOrder(): String {
        val mSelectWaybillNumber = StringBuilder()
        mLoadingListAdapter?.getAllData()?.let {
            for ((index, item) in (it.withIndex())) {
                if (item.isChecked) {
                    mSelectWaybillNumber.append(item.billno)
                    if (index != it.size - 1)
                        mSelectWaybillNumber.append(",")
                }
            }
        }
        return mSelectWaybillNumber.toString()
    }



}