/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.provider.Settings

object SystemUtils {
    fun getSystemInfo(context: Context): String {
        return "Version " + getAppVersionName(context) +
            "(" + getAppVersionCode(context) + ") - " +
            "OS Version: " + getOsVersion(context)
    }

    fun getOsVersion(context: Context): String {
        return android.os.Build.VERSION.RELEASE
    }

    fun getAppVersionName(context: Context): String? {
        val packageInfo: PackageInfo
        return try {
            packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    fun getAppVersionCode(context: Context): Long {
        val packageInfo: PackageInfo
        return try {
            packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                packageInfo.longVersionCode
            } else {
                packageInfo.versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            -1
        }
    }

    @SuppressLint("HardwareIds")
    fun getDeviceUniqueId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}