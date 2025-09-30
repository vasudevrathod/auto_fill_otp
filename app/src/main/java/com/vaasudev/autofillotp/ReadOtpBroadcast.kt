package com.vaasudev.autofillotp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

/** # Created by ~ `V J R`
 * ## Broadcast for Read OTP from SMS */
class ReadOtpBroadcast: BroadcastReceiver() {

    var smsContent: SmsContent? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {intent ->
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                val status: Status? = extras!![SmsRetriever.EXTRA_STATUS] as Status?
                when (status?.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        // Get SMS message contents
                        val message = extras.getString(SmsRetriever.EXTRA_SMS_MESSAGE) ?: ""
                        smsContent?.onSuccessMessageListener(message = message)
                    }

                    CommonStatusCodes.TIMEOUT -> {
                        //Toast.makeText(context, "SMS Retriever timed out", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

/** # SMS Content - `Interface`
 * - Success Message Listener when SMS is received and read from it */
interface SmsContent {
    fun onSuccessMessageListener(message: String)
}