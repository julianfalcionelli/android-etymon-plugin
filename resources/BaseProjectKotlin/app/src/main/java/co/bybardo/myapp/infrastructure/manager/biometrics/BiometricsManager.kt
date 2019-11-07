/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.biometrics

import androidx.fragment.app.FragmentActivity
import io.reactivex.Completable

interface BiometricsManager {
    fun authenticate(
        activity: FragmentActivity,
        title: String,
        subtitle: String?,
        description: String?,
        negativeButton: String
    ): Completable

    fun hasFingerprintEnabled(): Boolean

    fun supportsFingerprint(): Boolean

    fun startFingerprintEnrollment(activity: FragmentActivity, requestCode: Int)
}

open class BiometricsException(message: String?) : Exception(message)

class BiometricsNotAvailableException(message: String?) : BiometricsException("Biometrics not supported: $message")

class BiometricCancelledException(message: String?) : BiometricsException("Biometrics cancelled: $message")