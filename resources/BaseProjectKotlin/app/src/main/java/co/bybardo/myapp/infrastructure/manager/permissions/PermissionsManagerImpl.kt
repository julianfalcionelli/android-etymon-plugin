/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.permissions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers

class PermissionsManagerImpl(private val context: Context) : PermissionsManager {

    override fun hasPermission(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun hasPermissions(permissions: Array<String>): Boolean {
        permissions.forEach { permission ->
            if (hasPermission(permission)) {
                return true
            }
        }

        return false
    }

    override fun requestPermission(permission: String): Completable {
        return requestPermissions(arrayOf(permission))
    }

    override fun requestPermissions(permissions: Array<String>): Completable {
        return Completable.create { emitter ->
            val options = QuickPermissionsOptions(
                handleRationale = false,
                handlePermanentlyDenied = true,
                permanentDeniedMethod = {
                    emitter.onError(PermissionException.PermissionPermanentlyDeniedException())
                },
                permissionsDeniedMethod = {
                    emitter.onError(PermissionException.PermissionDeniedException())
                }
            )

            context.runWithPermissions(*permissions, options = options) {
                emitter.onComplete()
            }
        }.subscribeOn(AndroidSchedulers.mainThread())
    }
}