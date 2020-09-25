package com.mbcq.vehicleslibrary.activity.alllocalagent.localagent

/**
 * 1按车
 * 2按票
 */
data class LocalAgentEvent(
        var type: Int=-1,
        var webCode: String = "",
        var startTime: String = "",
        var endTime: String = ""
)