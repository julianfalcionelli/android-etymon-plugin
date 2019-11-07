/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.permissions

open class PermissionException : RuntimeException() {
    class PermissionDeniedException : PermissionException()

    class PermissionPermanentlyDeniedException : PermissionException()
}