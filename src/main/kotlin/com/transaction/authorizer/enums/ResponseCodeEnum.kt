package com.transaction.authorizer.enums

enum class ResponseCodeEnum(val code: String, val message: String) {
    REJECTED("51", "Transaction rejected - account balance is insufficient"),
    APPROVED("00", "Transaction approved"),
    ERROR("07", "Transaction rejected - Generic error");
}