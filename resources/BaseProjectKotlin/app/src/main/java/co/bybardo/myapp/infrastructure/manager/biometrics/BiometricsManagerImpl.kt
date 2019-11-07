/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.biometrics

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings.ACTION_FINGERPRINT_ENROLL
import android.provider.Settings.ACTION_SECURITY_SETTINGS
import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricPrompt
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.fragment.app.FragmentActivity
import io.reactivex.Completable
import timber.log.Timber
import java.util.concurrent.Executor
import javax.inject.Inject

class BiometricsManagerImpl @Inject constructor(
    private val context: Context
) : BiometricsManager {
    private val executor = MainThreadExecutor()

    /*
     * Condition I: Check if the android version in device is greater than
     * Marshmallow, since fingerprint authentication is only supported
     * from Android 6.0.
     * Note: If your project's minSdkversion is 23 or higher,
     * then you won't need to perform this check.
     *
     * */
    private fun isSdkVersionSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    /*
     * Condition II: Check if the device has fingerprint sensors.
     * Note: If you marked android.hardware.fingerprint as something that
     * your app requires (android:required="true"), then you don't need
     * to perform this check.
     *
     * */
    private fun isHardwareSupported(): Boolean {
        val fingerprintManager = FingerprintManagerCompat.from(context)
        return fingerprintManager.isHardwareDetected
    }

    /*
     * Condition III: Fingerprint authentication can be matched with a
     * registered fingerprint of the user. So we need to perform this check
     * in order to enable fingerprint authentication
     *
     * */
    private fun isFingerprintAvailable(): Boolean {
        val fingerprintManager = FingerprintManagerCompat.from(context)
        return fingerprintManager.hasEnrolledFingerprints()
    }

    override fun hasFingerprintEnabled(): Boolean {
        return supportsFingerprint() &&
            isFingerprintAvailable()
    }

    override fun supportsFingerprint(): Boolean {
        return isSdkVersionSupported() &&
            isHardwareSupported()
    }

    override fun startFingerprintEnrollment(activity: FragmentActivity, requestCode: Int) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Intent(ACTION_FINGERPRINT_ENROLL)
        } else {
            Intent(ACTION_SECURITY_SETTINGS)
        }

        activity.startActivityForResult(intent, requestCode)
    }

    override fun authenticate(
        activity: FragmentActivity,
        title: String,
        subtitle: String?,
        description: String?,
        negativeButton: String
    ): Completable {
        return Completable.create { emitter ->
            val prompt = BiometricPrompt(
                activity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    @SuppressLint("SwitchIntDef")
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Timber.e("Auth Error. Code: $errorCode, $errString")
                        val exception = when (errorCode) {
                            BiometricConstants.ERROR_HW_NOT_PRESENT,
                            BiometricConstants.ERROR_HW_UNAVAILABLE,
                            BiometricConstants.ERROR_NO_BIOMETRICS ->
                                BiometricsNotAvailableException("Code: $errorCode, Message: $errString")
                            else -> BiometricCancelledException("Code: $errorCode, Message: $errString")
                        }

                        emitter.onError(exception)
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        Timber.d("Auth succeeded")
                        emitter.onComplete()
                    }
                })

            val promptInfoBuilder = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setNegativeButtonText(negativeButton)

            subtitle?.let {
                promptInfoBuilder.setSubtitle(it)
            }

            description?.let {
                promptInfoBuilder.setDescription(it)
            }

            prompt.authenticate(promptInfoBuilder.build())
        }
    }

    inner class MainThreadExecutor : Executor {
        private val handler = Handler(Looper.getMainLooper())

        override fun execute(runnable: Runnable) {
            handler.post(runnable)
        }
    }
}