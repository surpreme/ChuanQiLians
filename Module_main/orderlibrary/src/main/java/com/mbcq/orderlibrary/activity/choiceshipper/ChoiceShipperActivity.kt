package com.mbcq.orderlibrary.activity.choiceshipper


import android.content.Intent
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.mbcq.baselibrary.gson.GsonUtils
import com.mbcq.baselibrary.interfaces.OnClickInterface
import com.mbcq.baselibrary.ui.BaseListMVPActivity
import com.mbcq.baselibrary.ui.mvp.BaseMVPActivity
import com.mbcq.baselibrary.view.BaseRecyclerAdapter
import com.mbcq.baselibrary.view.SingleClick
import com.mbcq.commonlibrary.ARouterConstants
import com.mbcq.orderlibrary.R
import kotlinx.android.synthetic.main.activity_choice_shipper.*
import org.json.JSONObject

/**
 * @author: lzy
 * @time: 2020-11-02 11:06:45  选择发货人
 */

@Route(path = ARouterConstants.ChoiceShipperActivity)
class ChoiceShipperActivity : BaseListMVPActivity<ChoiceShipperContract.View, ChoiceShipperPresenter, ChoiceShipperBean>(), ChoiceShipperContract.View {
    private val RESULT_DATA_CODE = 5848

    override fun getLayoutId(): Int = R.layout.activity_choice_shipper
    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        setStatusBar(R.color.base_blue)

    }

    override fun initDatas() {
        super.initDatas()
        mPresenter?.getInfo()
    }


    override fun onClick() {
        super.onClick()
        choice_shipper_toolbar.setBackButtonOnClickListener(object : SingleClick() {
            override fun onSingleClick(v: View?) {
                onBackPressed()
            }

        })
    }

    override fun getRecyclerViewId(): Int = R.id.choice_shipper_recycler

    override fun setAdapter(): BaseRecyclerAdapter<ChoiceShipperBean> = ChoiceShipperAdapter(mContext).also {
        it.mClickInterface = object : OnClickInterface.OnRecyclerClickInterface {
            override fun onItemClick(v: View, position: Int, mResult: String) {

                val resultObj = JSONObject(mResult)
                val obj = JSONObject()
                obj.put("name", resultObj.optString("contactMan"))
                obj.put("phone", resultObj.optString("contactMb"))
                obj.put("address", resultObj.optString("address"))
                obj.put("shipperTel", resultObj.optString("contactTel"))
                obj.put("shipperCid", "")
                obj.put("shipperId", "")
                val json = GsonUtils.toPrettyFormat(obj.toString())
                setResult(RESULT_DATA_CODE, Intent().putExtra("AddShipperResultData", json))
                finish()
            }

        }
    }

    override fun getInfoS(list: List<ChoiceShipperBean>) {
        adapter.appendData(list)
    }
}