package com.mbcq.vehicleslibrary.activity.loadingvehicles

import com.mbcq.baselibrary.ui.mvp.BasePresenter
import com.mbcq.baselibrary.ui.mvp.BaseView

/**
 * @author: lzy
 * @time: 2020-11-04 09:49:32 装车
 */

class LoadingVehiclesContract {

    interface View : BaseView {
//        fun getShortFeederS(list: List<LoadingVehiclesBean>, isScan: Boolean, isCanRefresh: Boolean)
//        fun getTrunkDepartureS(list: List<LoadingVehiclesBean>, isScan: Boolean, isCanRefresh: Boolean)
        fun searchScanInfoS(list: List<LoadingVehiclesBean>)
        fun invalidOrderS(position: Int)
        fun saveScanPostS(position: Int)
        fun getScanVehicleListS(list: List<LoadingVehiclesBean>)
    }

    interface Presenter : BasePresenter<View> {
        fun getScanVehicleList(startDate: String, endDate: String)
        fun searchShortFeeder(inoneVehicleFlag: String)
//        fun getTrunkDeparture(startDate: String, endDate: String)
        fun searchTrunkDeparture(inoneVehicleFlag: String)
        fun searchScanInfo(sendInfo: String)

        /**
         * 作废
         * @1短驳
         * @2干线
         */
        fun invalidOrder(inoneVehicleFlag: String, id: Int, mType: Int, position: Int)
        /**
         * 发车
         * @1短驳
         * @2干线
         */
        fun saveScanPost(id: Int, inoneVehicleFlag: String, mType: Int, position: Int)

    }
}
