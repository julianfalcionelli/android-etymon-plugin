/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.networking

import co.bybardo.myapp.BuildConfig

object RestConstants {
    const val BASE_URL = BuildConfig.BASE_URL

    // TODO Set Auth Header
    const val HEADER_AUTH = "auth-header"
    // TODO Set Bearer Header
    const val HEADER_BEARER = "Bearer "
}
