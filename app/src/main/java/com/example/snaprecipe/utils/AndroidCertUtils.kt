package com.example.snaprecipe.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.security.MessageDigest

object AndroidCertUtils {

    fun getSignatureSha1(context: Context): String {
        return try {
            val packageName = context.packageName
            val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val info = context.packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )
                info.signingInfo?.apkContentsSigners
            } else {
                val info = context.packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES
                )
                @Suppress("DEPRECATION")
                info.signatures
            }

            if (signatures == null || signatures.isEmpty()) {
                return ""
            }

            val digest = MessageDigest.getInstance("SHA1")
                .digest(signatures[0].toByteArray())
            digest.joinToString("") { byte -> "%02X".format(byte) }
        } catch (_: Exception) {
            ""
        }
    }
}
