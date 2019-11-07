/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.networking

import com.google.gson.annotations.SerializedName

class MyAppServerError {
    @SerializedName("error_code")
    val mErrorCode: Int? = null

    @SerializedName("message")
    val mErrorMessage: String? = null
}