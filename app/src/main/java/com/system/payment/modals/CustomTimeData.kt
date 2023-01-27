package com.system.payment.modals

data class CustomTimeData(
    val categoryList: List<String>,
    val accountList: List<String>,
    val startDate: Long,
    val endDate: Long
)