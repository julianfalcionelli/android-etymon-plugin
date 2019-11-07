/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.ui.activity.base

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import co.bybardo.myapp.R
import co.bybardo.myapp.ui.extensions.hide
import co.bybardo.myapp.ui.extensions.isGone
import co.bybardo.myapp.ui.extensions.isVisible
import co.bybardo.myapp.ui.extensions.show
import co.bybardo.myapp.ui.util.UIUtils
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity(), BaseContract.View {
    companion object {
        var sPreviousToast: Toast? = null
        var APP_OPEN = false
        var APP_VISIBLE = false
        var LAST_ACTIVITY_RESUMED: String? = null
    }

    private var localBackEnabled: Boolean = false
    private var progressDialog: View? = null
    private var comesFromBackground = false

    private var genericErrorAlert: AlertDialog? = null
    private var internetGenericErrorAlert: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        APP_OPEN = true
        enterTransition()
    }

    override fun onResume() {
        super.onResume()
        APP_VISIBLE = true
        LAST_ACTIVITY_RESUMED = javaClass.simpleName
    }

    override fun onPause() {
        APP_VISIBLE = false
        comesFromBackground = true
        super.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        exitTransition()
    }

    open fun hideToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar) ?: return
        toolbar.hide()
    }

    fun initializeToolbar(backEnabled: Boolean, title: String?) {
        localBackEnabled = backEnabled
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val customTitle = findViewById<TextView>(R.id.toolbar_title)

        if (toolbar == null) {
            return
        }

        setSupportActionBar(toolbar)
        toolbar.show()

        if (!title.isNullOrEmpty()) {
            if (customTitle != null) {
                customTitle.text = title
                supportActionBar?.title = ""
            } else {
                supportActionBar?.title = title
            }
        } else {
            supportActionBar?.title = ""
            if (customTitle != null) {
                customTitle.text = ""
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(localBackEnabled)
        supportActionBar?.setHomeButtonEnabled(localBackEnabled)
        if (localBackEnabled) {
            toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!localBackEnabled) {
            return false
        }
        when (item.itemId) {
            android.R.id.home -> onToolbarBackPressed()
        }
        return true
    }

    protected open fun onToolbarBackPressed() {
        onBackPressed()
    }

    protected open fun enterTransition() {
        /*override method on child's*/
    }

    protected open fun exitTransition() {
        /*override method on child's*/
    }

    fun showToast(string: String) {
        showToast(string, Toast.LENGTH_SHORT)
    }

    fun showToast(@StringRes stringRes: Int) {
        showToast(getString(stringRes), Toast.LENGTH_SHORT)
    }

    fun showToast(string: String, duration: Int) {
        if (sPreviousToast != null) {
            sPreviousToast!!.cancel()
        }

        sPreviousToast = Toast.makeText(applicationContext, string, duration)
        sPreviousToast!!.show()
    }

    override fun showProgress() {
        if (progressDialog == null) {
            val rootView = UIUtils.getRootView(this) as ViewGroup
            progressDialog = layoutInflater.inflate(R.layout.progressbar, rootView, false)
            rootView.addView(progressDialog)
        }

        if (progressDialog!!.isGone()) {
            progressDialog!!.show()
        }
    }

    override fun hideProgress() {
        if (progressDialog != null && progressDialog!!.isVisible()) {
            progressDialog!!.hide()
        }
    }

    override fun showGenericError() {
        if (genericErrorAlert == null) {
            // TODO Create Alert
            // genericErrorAlert =
        }

        if (!genericErrorAlert!!.isShowing) {
            genericErrorAlert!!.show()
        }
    }

    override fun showNoInternetGenericError() {
        if (internetGenericErrorAlert == null) {
            // TODO Create Alert
            // internetGenericErrorAlert =
        }

        if (internetGenericErrorAlert!!.isShowing) {
            internetGenericErrorAlert!!.show()
        }
    }
}
