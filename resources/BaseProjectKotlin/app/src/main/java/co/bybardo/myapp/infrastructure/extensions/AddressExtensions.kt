/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.extensions

import android.location.Address

fun Address.getAddress() = getAddressLine(0)

fun Address.getCity() = getAddressLine(1)

fun Address.getCountry() = getAddressLine(2)
