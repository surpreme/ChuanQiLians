package com.mbcq.baselibrary.interfaces

import android.view.View

class OnClickInterface {
    interface OnRecyclerClickInterface {
        fun onItemClick(v: View, position: Int, mResult: String)
    }

    interface OnRecyclerDeleteClickInterface {
        fun onDelete(v: View, position: Int, mResult: String)
    }

    interface OnClickInterface {
        fun onResult(s1: String, s2: String)
    }


    //toolbar返回键
    interface OnToolBarClickInterface {
        fun back()
        fun rightClick(v: View)
    }
}