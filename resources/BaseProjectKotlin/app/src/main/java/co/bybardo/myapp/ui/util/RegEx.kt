/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.ui.util

import android.util.Patterns
import java.util.regex.Pattern

object RegEx {
    val EMAIL_REGEX = Patterns.EMAIL_ADDRESS
    val PASSWORD_REGEX = Pattern.compile("^.{6,12}\$")
    val NAME_REGEX = Pattern.compile("^.{1,60}\$")
    val PHONE = Patterns.PHONE
}