package com.vaasudev.autofillotp

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.vaasudev.autofillotp.databinding.ActivityMainBinding

/** # Created by ~ `V J R`
 * ## OTP Screen - `Activity` */
class MainActivity : AppCompatActivity(), SmsContent {

    /** Declare Binding */
    private lateinit var binding: ActivityMainBinding

    /** Declare OTP Auto Fill Broadcast Receiver */
    private val smsBroadcastReceiver = ReadOtpBroadcast()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initBinding()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
    }

    /** Init Binding */
    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
    }

    /** Initialize All Functions */
    private fun init() {
        getAppSignature()
        initSmsRetrieverClient()
        initSmsReadBroadCast()
    }

    /** Get App Signature
     * - Getting app signature from current system */
    private fun getAppSignature() {
        val signature = getAppSignature(this)
        printLog("OTPAutoFill", "App Signature =====> $signature")
    }

    /** Initialize SMS Retriever Client
     * - Client is used to listen for incoming messages */
    private fun initSmsRetrieverClient() {
        val client: SmsRetrieverClient = SmsRetriever.getClient(this)
        val task = client.startSmsRetriever()
        task.addOnSuccessListener {
            printLog("OTPAutoFill", "SmsRetriever =====> Started successfully")
        }
        task.addOnFailureListener {
            printLog("OTPAutoFill", "SmsRetriever =====> Failed to start - ${it.toString()}")
        }
    }

    /** Initialize OTP Auto Fill Broadcast Receiver
     * - Receiver is used to read OTP from SMS*/
    private fun initSmsReadBroadCast() {
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter, RECEIVER_EXPORTED)

        smsBroadcastReceiver.smsContent = this@MainActivity
    }

    /** On Success Message Listener - `SmsContent Interface`
     * @see SmsContent*/
    override fun onSuccessMessageListener(message: String) {
        val otp = extractOtpFromMessage(message)
        printLog("OTPAutoFill", "SMS Read OTP =====> $otp")
        binding.etOtp.setText(otp)
    }

    /** Extract OTP from Received Message*/
    private fun extractOtpFromMessage(message: String): String {
        val otpPattern = Regex("(\\d{4,6})") // Modify as per OTP length
        val match = otpPattern.find(message)
        return match?.value ?: ""
    }
}