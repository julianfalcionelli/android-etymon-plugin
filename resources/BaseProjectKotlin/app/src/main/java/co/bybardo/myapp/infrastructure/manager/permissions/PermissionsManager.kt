/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.permissions

import io.reactivex.Completable

interface PermissionsManager {
    fun hasPermission(permission: String): Boolean
    fun hasPermissions(permissions: Array<String>): Boolean

    fun requestPermission(permission: String): Completable
    fun requestPermissions(permissions: Array<String>): Completable
}