package com.almaz.closedsociety.data.security

import android.content.Context
import android.util.Base64
import android.content.SharedPreferences

class SignatureManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("closed_society_prefs", Context.MODE_PRIVATE)

    fun saveSignatureTemplate(signatureData: ByteArray) {
        prefs.edit()
            .putString("signature_template", Base64.encodeToString(signatureData, Base64.DEFAULT))
            .apply()
    }

    fun getSignatureTemplate(): ByteArray? {
        val base64 = prefs.getString("signature_template", null)
        return base64?.let { Base64.decode(it, Base64.DEFAULT) }
    }

    fun saveUserId(uuid: String) {
        prefs.edit().putString("user_uuid", uuid).apply()
    }

    fun getUserId(): String? {
        return prefs.getString("user_uuid", null)
    }

    fun clearAllData() {
        prefs.edit().clear().apply()
    }
}