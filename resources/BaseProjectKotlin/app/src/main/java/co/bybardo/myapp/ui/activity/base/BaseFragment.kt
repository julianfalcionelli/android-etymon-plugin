/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.ui.activity.base

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment(), BaseContract.View {
    protected fun requireBaseActivity(): BaseActivity = requireActivity() as BaseActivity

    override fun showProgress() = requireBaseActivity().showProgress()

    override fun hideProgress() = requireBaseActivity().hideProgress()

    override fun showGenericError() = requireBaseActivity().showGenericError()

    override fun showNoInternetGenericError() = requireBaseActivity().showNoInternetGenericError()
}