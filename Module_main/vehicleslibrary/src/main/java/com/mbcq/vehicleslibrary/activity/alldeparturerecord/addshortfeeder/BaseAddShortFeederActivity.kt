package com.mbcq.vehicleslibrary.activity.alldeparturerecord.addshortfeeder


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.gson.Gson
import com.mbcq.baselibrary.interfaces.OnClickInterface
import com.mbcq.baselibrary.ui.mvp.BaseMVPActivity
import com.mbcq.baselibrary.ui.mvp.BasePresenterImpl
import com.mbcq.baselibrary.ui.mvp.BaseView
import com.mbcq.baselibrary.view.SingleClick
import com.mbcq.commonlibrary.CommonApplication
import com.mbcq.commonlibrary.RadioGroupUtil
import com.mbcq.commonlibrary.db.WebAreaDbInfo
import com.mbcq.commonlibrary.dialog.FilterDialog
import com.mbcq.commonlibrary.greendao.DaoSession
import com.mbcq.commonlibrary.greendao.WebAreaDbInfoDao
import com.mbcq.vehicleslibrary.R
import kotlinx.android.synthetic.main.activity_add_short_feeder.*

/**
 * @author: lzy
 * @time: 2020-09-14 13:42:27
 * 添加短驳发车 运输方式 update
 */
abstract class BaseAddShortFeederActivity<V : BaseView, T : BasePresenterImpl<V>> : BaseMVPActivity<V, T>(), BaseView {
    var mFirstEwebidCode = ""
    var mSencondEwebidCode = ""
    var mThridEwebidCode = ""
    var mECompanyId = ""
    var mWebCodeId = ""
    var mWebCodeIdStr = ""
    var mTransneed = 1//运输类型编码
    var mTransneedStr = ""//运输类型
    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        setStatusBar(R.color.base_blue)
        initModeOfTransport()
        cash_freight_hide_ll.visibility = View.GONE
        freight_onarrival_hide_ll.visibility = View.GONE
        oil_card_first_ed.isFocusable = false
        oil_card_first_ed.isFocusableInTouchMode = false
        oil_card_second_ed.isFocusable = false
        oil_card_second_ed.isFocusableInTouchMode = false
        oil_card_third_ed.isFocusable = false
        oil_card_third_ed.isFocusableInTouchMode = false
        initTotalPrice()

    }

    override fun onClick() {
        super.onClick()
        oil_card_first_tv.setOnClickListener(object : SingleClick() {
            override fun onSingleClick(v: View?) {
                getDbWebId(object : WebDbInterface {
                    override fun isNull() {
                    }

                    override fun isSuccess(list: MutableList<WebAreaDbInfo>) {
                        geDeliveryPointLocal(list, 1)
                    }

                })

            }

        })
        oil_card_second_tv.setOnClickListener(object : SingleClick() {
            override fun onSingleClick(v: View?) {
                getDbWebId(object : WebDbInterface {
                    override fun isNull() {
                    }

                    override fun isSuccess(list: MutableList<WebAreaDbInfo>) {
                        geDeliveryPointLocal(list, 2)
                    }

                })
            }

        })
        oil_card_third_tv.setOnClickListener(object : SingleClick() {
            override fun onSingleClick(v: View?) {
                getDbWebId(object : WebDbInterface {
                    override fun isNull() {
                    }

                    override fun isSuccess(list: MutableList<WebAreaDbInfo>) {
                        geDeliveryPointLocal(list, 3)
                    }

                })
            }

        })
        destination_tv.setOnClickListener(object : SingleClick() {
            override fun onSingleClick(v: View?) {
                getDbWebId(object : WebDbInterface {
                    override fun isNull() {
                    }

                    override fun isSuccess(list: MutableList<WebAreaDbInfo>) {
                        geDeliveryPointLocal(list, 0)
                    }

                })
            }

        })
        add_short_feeder_toolbar.setBackButtonOnClickListener(object : SingleClick() {
            override fun onSingleClick(v: View?) {
                onBackPressed()
            }

        })
    }
    private fun initTotalPrice() {

        cash_freight_ed.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                changeTotalPrice()


            }

        })

        oil_card_first_ed.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                changeTotalPrice()


            }

        })

        oil_card_second_ed.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                changeTotalPrice()


            }

        })
        oil_card_third_ed.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                changeTotalPrice()


            }

        })
        return_freight_ed.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                changeTotalPrice()


            }

        })

    }


    private fun initModeOfTransport() {
        mTransneedStr = "普运"
        mode_transport_rg.addView(RadioGroupUtil.addSelectItem(mContext, "普运", 0))
        mode_transport_rg.addView(RadioGroupUtil.addSelectItem(mContext, "马帮快线", 1))
        mode_transport_rg.addView(RadioGroupUtil.addSelectItem(mContext, "补发数据", 2))
        mode_transport_rg.check(0)
        mode_transport_rg.setOnCheckedChangeListener { _, checkedId ->
            mTransneed = checkedId + 1
            when (checkedId) {
                0 -> mTransneedStr = "普运"
                1 -> mTransneedStr = "马帮快线"
                2 -> mTransneedStr = "补发数据"
            }
        }

    }

    var mToPayTotalPrice = 0.00
    protected fun changeTotalPrice() {
        var totalPrice = 0.00//油卡费用不计算
        var otherTotalPrice = 0.00//
        var ToPayTotalPrice = 0.00//到付 3个金额 不包括回付
        if (cash_freight_ed.text.toString().isNotBlank()) {
            totalPrice += (cash_freight_ed.text.toString()).toDouble()
        }
        if (return_freight_ed.text.toString().isNotBlank()) {
            totalPrice += (return_freight_ed.text.toString()).toDouble()
        }
        if (oil_card_first_tv.text.toString().isNotBlank() && oil_card_first_ed.text.toString().isNotBlank()) {
            totalPrice += (oil_card_first_ed.text.toString()).toDouble()
            ToPayTotalPrice += (oil_card_first_ed.text.toString()).toDouble()
        }
        if (oil_card_second_tv.text.toString().isNotBlank() && oil_card_second_ed.text.toString().isNotBlank()) {
            totalPrice += (oil_card_second_ed.text.toString()).toDouble()
            ToPayTotalPrice += (oil_card_second_ed.text.toString()).toDouble()
        }
        if (oil_card_third_tv.text.toString().isNotBlank() && oil_card_third_ed.text.toString().isNotBlank()) {
            totalPrice += (oil_card_third_ed.text.toString()).toDouble()
            ToPayTotalPrice += (oil_card_third_ed.text.toString()).toDouble()
        }
        total_freight_tv.text = haveTwoDouble(totalPrice)
        mToPayTotalPrice = ToPayTotalPrice
    }
    fun showHideCashFreight() {
        if (cash_freight_hide_ll.visibility == View.VISIBLE) {
            cash_freight_hide_ll.visibility = View.GONE
            cash_freight_down_iv.rotation = 0f
//            cash_freight_tv.text = "请选择现付运费"
        } else if (cash_freight_hide_ll.visibility == View.GONE) {
            cash_freight_hide_ll.visibility = View.VISIBLE
            cash_freight_down_iv.rotation = 180f
//            cash_freight_tv.text = ""


        }
    }

    fun showHideFreightOnArrival() {
        if (freight_onarrival_hide_ll.visibility == View.VISIBLE) {
            freight_onarrival_hide_ll.visibility = View.GONE
            freight_onarrival_down_iv.rotation = 0f
            freight_onarrival_tv.text = "请选择现付运费"
        } else if (freight_onarrival_hide_ll.visibility == View.GONE) {
            freight_onarrival_hide_ll.visibility = View.VISIBLE
            freight_onarrival_down_iv.rotation = 180f
            freight_onarrival_tv.text = ""


        }
    }


    interface WebDbInterface {
        fun isNull()
        fun isSuccess(list: MutableList<WebAreaDbInfo>)

    }

    /**
     * 得到greenDao数据库中的网点
     * 可视化 stetho 度娘
     */
    protected fun getDbWebId(mWebDbInterface: WebDbInterface) {
        val daoSession: DaoSession = (application as CommonApplication).daoSession
        val userInfoDao: WebAreaDbInfoDao = daoSession.webAreaDbInfoDao
        val dbDatas = userInfoDao.queryBuilder().list()
        if (dbDatas.isNullOrEmpty()) {
            mWebDbInterface.isNull()
        } else {
            mWebDbInterface.isSuccess(dbDatas)
        }
    }

    fun geDeliveryPointLocal(list: MutableList<WebAreaDbInfo>, type: Int) {
        FilterDialog(getScreenWidth(), Gson().toJson(list), "webid", "选择到货网点", true, isShowOutSide = true, mClickInterface = object : OnClickInterface.OnRecyclerClickInterface {
            override fun onItemClick(v: View, position: Int, mResult: String) {
                when (type) {
                    0 -> {
                        destination_tv.text = list[position].webid
                        mECompanyId = list[position].companyId
                        mWebCodeId = list[position].webidCode
                    }
                    1 -> {
                        if (list[position].webidCode.toString() == mSencondEwebidCode || list[position].webidCode.toString() == mThridEwebidCode) {
                            showToast("您已经选择过${list[position].webid}了哦")
                            return
                        }
                        oil_card_first_tv.text = list[position].webid
                        mFirstEwebidCode = list[position].webidCode.toString()
                        oil_card_first_ed.isFocusableInTouchMode = true
                        oil_card_first_ed.isFocusable = true
                        oil_card_first_ed.requestFocus()

                    }
                    2 -> {
                        if (list[position].webidCode.toString() == mFirstEwebidCode || list[position].webidCode.toString() == mThridEwebidCode) {
                            showToast("您已经选择过${list[position].webid}了哦")
                            return
                        }
                        oil_card_second_tv.text = list[position].webid
                        mSencondEwebidCode = list[position].webidCode.toString()
                        oil_card_second_ed.isFocusableInTouchMode = true
                        oil_card_second_ed.isFocusable = true
                        oil_card_second_ed.requestFocus()
                    }
                    3 -> {
                        if (list[position].webidCode.toString() == mFirstEwebidCode || list[position].webidCode.toString() == mSencondEwebidCode) {
                            showToast("您已经选择过${list[position].webid}了哦")
                            return
                        }
                        oil_card_third_tv.text = list[position].webid
                        mThridEwebidCode = list[position].webidCode.toString()
                        oil_card_third_ed.isFocusableInTouchMode = true
                        oil_card_third_ed.isFocusable = true
                        oil_card_third_ed.requestFocus()
                    }
                }
            }

        }).show(supportFragmentManager, "geDeliveryPointLocalDialogFilterDialog$type")
    }


}