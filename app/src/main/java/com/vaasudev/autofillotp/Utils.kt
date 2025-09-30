package com.vaasudev.autofillotp

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

fun printLog(tag: String = "Log Information", value: Any) {
    if (BuildConfig.DEBUG) {
        Log.i(tag, "$tag Log =====> 🧐🧐🧐 $value")
    }
}

fun getAppSignature(packageName: String, appSignature: String): String? {
    return try {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update("$packageName $appSignature".toByteArray(StandardCharsets.UTF_8))
        val hash =
            Base64.encodeToString(messageDigest.digest(), Base64.NO_PADDING or Base64.NO_WRAP)
        hash.take(11)
    } catch (e: Exception) {
        null
    }
}

fun getAppSignature(context: Context): String {
    val packageInfo = context.packageManager.getPackageInfo(
        context.packageName,
        PackageManager.GET_SIGNING_CERTIFICATES
    )
    val signature = packageInfo.signingInfo?.apkContentsSigners?.get(0)?.toCharsString()
    return getAppSignature(context.packageName, signature!!) ?: ""
}