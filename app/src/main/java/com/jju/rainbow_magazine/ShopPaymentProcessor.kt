package com.jju.rainbow_magazine

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentManager
import kr.co.bootpay.android.Bootpay
import kr.co.bootpay.android.events.BootpayEventListener
import kr.co.bootpay.android.models.BootExtra
import kr.co.bootpay.android.models.BootItem
import kr.co.bootpay.android.models.BootUser
import kr.co.bootpay.android.models.Payload

class ShopPaymentProcessor(private val fragmentManager: FragmentManager, private val context: Context) {
    private val AppId = "648d9424d25985001cdbcd78"
    private var shop_title: String? = null
    private var shop_price: String? = null

    fun goRequest(title: String?, price: String?) {
        shop_title = title
        shop_price = price

        val user = BootUser().setPhone("010-1234-5678")
        val extra = BootExtra().setCardQuota("0,2,3")

        val price = shop_price?.replace(",", "")?.replace("원", "")?.toDoubleOrNull() ?: 0.0
        val pg = "나이스페이"
        val method = "네이버페이"

        val items: MutableList<BootItem> = ArrayList()
        val item = BootItem().setName(shop_title ?: "Default Item Name").setId("ITEM_ID").setQty(1).setPrice(price)
        items.add(item)

        val payload = Payload()
        payload.setApplicationId(AppId)
            .setOrderName(shop_title ?: "Default Order Name")
            .setPg(pg)
            .setOrderId("1234")
            .setMethod(method)
            .setPrice(price)
            .setUser(user)
            .setExtra(extra).items = items

        val map: MutableMap<String, Any> = HashMap()
        map["1"] = "abcdef"
        map["2"] = "abcdef55"
        map["3"] = 1234
        payload.metadata = map

        Bootpay.init(fragmentManager, context)
            .setPayload(payload)
            .setEventListener(object : BootpayEventListener {
                override fun onCancel(data: String) {
                    Log.d("bootpay", "cancel: $data")
                }

                override fun onError(data: String) {
                    Log.d("bootpay", "error: $data")
                }

                override fun onClose(data: String) {
                    Log.d("bootpay", "close: $data")
                    Bootpay.removePaymentWindow()
                }

                override fun onIssued(data: String) {
                    Log.d("bootpay", "issued: $data")
                }

                override fun onConfirm(data: String): Boolean {
                    Log.d("bootpay", "confirm: $data")
                    return true
                }

                override fun onDone(data: String) {
                    Log.d("done", data)
                }
            }).requestPayment()
    }
}
