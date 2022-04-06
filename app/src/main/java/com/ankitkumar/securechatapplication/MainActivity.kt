package com.ankitkumar.securechatapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ankitkumar.securechatapplication.network.WebSocketWrapper
import com.ankitkumar.securechatapplication.util.PreferenceHelper
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    val webSocketWrapper by inject<WebSocketWrapper>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_SecureChatApplication)
        setContentView(R.layout.activity_main)
        webSocketWrapper.connect()
        val curveParams  = getCurveParameters()
        PreferenceHelper.saveCurveParams(curveParams,this)
    }

    private fun getCurveParameters(): Pair<Int, Int> {
        TODO("get curve params from server")
        return Pair(0,0)
    }

    override fun onDestroy() {
        webSocketWrapper.close()
        super.onDestroy()
    }
}