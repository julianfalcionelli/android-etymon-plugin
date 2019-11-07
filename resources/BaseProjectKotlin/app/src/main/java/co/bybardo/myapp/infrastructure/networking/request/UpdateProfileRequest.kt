/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.networking.request

data class UpdateProfileRequest(
    val name: String,
    val lastName: String
)