package com.mbcq.vehicleslibrary.fragment.arrivalshortfeeder

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lzy.okgo.model.HttpParams
import com.mbcq.baselibrary.ui.mvp.BasePresenterImpl
import com.mbcq.commonlibrary.ApiInterface
import com.mbcq.vehicleslibrary.fragment.shortfeeder.ShortFeederBean
import org.json.JSONArray
import org.json.JSONObject

/**
 * @author: lzy
 * @time: 2020-09-21 09:06
 */

class ArrivalShortFeederPresenter : BasePresenterImpl<ArrivalShortFeederContract.View>(), ArrivalShortFeederContract.Presenter {
    override fun getUnLoading(selEwebidCode: String, startDate: String, endDate: String) {
      /*  val mHttpParams = HttpParams()
        mHttpParams.put("page", page)
        mHttpParams.put("limit", 15)*/
       /* mView?.getContext()?.let {
            mHttpParams.put("selWebidCode", UserInformationUtil.getWebIdCode(it))
        }*/
//         mHttpParams.put("startDate", startDate)
//         mHttpParams.put("endDate", endDate)
        val mHttpParams = HttpParams()
        mHttpParams.put("SelEwebidCode", selEwebidCode)
        mHttpParams.put("startDate", startDate)
        mHttpParams.put("endDate", endDate)
        get<String>(ApiInterface.DEPARTURE_RECORD_SHORT_FEEDER_DEPARTURE_SELECT_LOADING_LOCAL_INFO_POST, mHttpParams, object : CallBacks {
            override fun onResult(result: String) {
                val obj = JSONObject(result)
                mView?.getPageS(Gson().fromJson<List<ShortFeederBean>>(obj.optString("data"), object : TypeToken<List<ShortFeederBean>>() {}.type))
            }

        })
    }

    override fun getLoading(selEwebidCode: String, startDate: String, endDate: String) {
        val mHttpParams = HttpParams()
        mHttpParams.put("SelEwebidCode", selEwebidCode)
        mHttpParams.put("startDate", startDate)
        mHttpParams.put("endDate", endDate)
        get<String>(ApiInterface.DEPARTURE_RECORD_SHORT_FEEDER_DEPARTURE_SELECT_OVERRING_LOCAL_INFO_GET, mHttpParams, object : CallBacks {
            override fun onResult(result: String) {
                val obj = JSONObject(result)
                mView?.getPageS(Gson().fromJson<List<ShortFeederBean>>(obj.optString("data"), object : TypeToken<List<ShortFeederBean>>() {}.type))
            }

        })
    }

    override fun confirmCar(data: ShortFeederBean, position: Int) {
        val jsonObj = JSONObject()
        jsonObj.put("Id", data.id)
        jsonObj.put("InoneVehicleFlag", data.inoneVehicleFlag)
        val jsonArray = JSONArray()
        val itemObj = JSONObject()
        itemObj.put("Id", data.id)
        itemObj.put("InoneVehicleFlag", data.inoneVehicleFlag)
        jsonArray.put(itemObj)
        jsonObj.put("DbVehicleDetLst", jsonArray)
        post<String>(ApiInterface.DEPARTURE_RECORD_SHORT_FEEDER_DEPARTURE_ARRIVAL_CONFIRM_LOCAL_INFO_POST, getRequestBody(jsonObj), object : CallBacks {
            override fun onResult(result: String) {
                data.vehicleState = 2
                data.vehicleStateStr = "到货"
                mView?.confirmCarS(data, position)
//                mView?.confirmCarS( position)
            }

        })

    }

    override fun canCelCar(data: ShortFeederBean, position: Int) {
        /* val jsonObj = JSONObject()
         jsonObj.put("Id", data.id)
         jsonObj.put("InoneVehicleFlag", data.inoneVehicleFlag)
         jsonObj.put("ContractNo", data.contractNo)
         jsonObj.put("webidCode", data.webidCode)
         jsonObj.put("webidCodeStr", data.webidCodeStr)
         val jsonArray = JSONArray()
         val itemObj = JSONObject()
         itemObj.put("Id", data.id)
         itemObj.put("InoneVehicleFlag", data.inoneVehicleFlag)
         itemObj.put("ContractNo", data.contractNo)
         itemObj.put("webidCode", data.webidCode)
         itemObj.put("webidCodeStr", data.webidCodeStr)
         jsonArray.put(itemObj)
         jsonObj.put("DbVehicleDetLst", jsonArray)*/

        post<String>(ApiInterface.DEPARTURE_RECORD_SHORT_FEEDER_DEPARTURE_ARRIVAL_CANCEL_LOCAL_INFO_GET, getRequestBody(Gson().toJson(data)), object : CallBacks {
            override fun onResult(result: String) {
                data.vehicleState = 1
                data.vehicleStateStr = "发货"
                mView?.canCelCarS(data, position)
            }

        })
    }

}