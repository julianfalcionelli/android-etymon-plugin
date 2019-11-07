/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.ui.activity.base

interface BaseContract {
    interface View {
        fun showProgress()
        fun hideProgress()
        fun showGenericError()
        fun showNoInternetGenericError()
    }

    interface Presenter {
        fun destroy()
    }
}
