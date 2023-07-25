package com.jju.rainbow_magazine

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao Sdk 초기화
        KakaoSdk.init(this, "6a87f8aa78f915cf90f31acd75a5b5c6")
    }
}