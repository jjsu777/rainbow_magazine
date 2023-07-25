package com.jju.rainbow_magazine

import android.content.Context
import android.content.Intent
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class KakaoLoginHelper(private val context: Context) {

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            val intent = Intent(context, Activity_Home::class.java)
            context.startActivity(intent)
            SharedPreferencesUtil.setLoggedInKakao(context, true)
        }
    }

    fun loginWithKakao() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    val intent = Intent(context, Activity_Home::class.java)
                    context.startActivity(intent)
                    SharedPreferencesUtil.setLoggedInKakao(context, true)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }
}
