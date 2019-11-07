/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.auth

import co.bybardo.myapp.infrastructure.manager.auth.exceptions.InvalidUserCredentialsException
import co.bybardo.myapp.infrastructure.manager.auth.exceptions.UserDuplicatedException
import co.bybardo.myapp.infrastructure.manager.auth.exceptions.UserNotFoundException
import co.bybardo.myapp.infrastructure.manager.auth.exceptions.UserOperationException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.auth.UserProfileChangeRequest
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FirebaseAuthenticationManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthenticationManager {

    override fun createUser(email: String, password: String): Completable {
        return Completable.create { e ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { e.onComplete() }
                .addOnFailureListener { error ->
                    e.onError(handleFirebaseError(error))
                }
        }.subscribeOn(Schedulers.io())
    }

    override fun updateUser(firstName: String, lastName: String): Completable {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName("$firstName $lastName")
            .build()

        val firebaseUser = firebaseAuth.currentUser ?: return Completable.error(UserOperationException())

        return Completable.create { e ->
            firebaseUser.updateProfile(profileUpdates)
                .addOnSuccessListener { e.onComplete() }
                .addOnFailureListener { error ->
                    e.onError(handleFirebaseError(error))
                }
        }.subscribeOn(Schedulers.io())
    }

    override fun updateEmail(newEmail: String): Completable {
        return Completable.create { e ->
            if (firebaseAuth.currentUser!!.email == newEmail) {
                e.onComplete()
            }

            firebaseAuth.currentUser!!.updateEmail(newEmail)
                .addOnSuccessListener { e.onComplete() }
                .addOnFailureListener { error ->
                    e.onError(handleFirebaseError(error))
                }
        }.subscribeOn(Schedulers.io())
    }

    override fun updatePassword(oldPassword: String, newPassword: String): Completable {
        return Completable.create { e ->
            firebaseAuth.currentUser!!.reauthenticate(EmailAuthProvider
                .getCredential(firebaseAuth.currentUser!!.email!!, oldPassword))
                .addOnSuccessListener {
                    firebaseAuth.currentUser!!.updatePassword(newPassword)
                        .addOnSuccessListener { e.onComplete() }
                        .addOnFailureListener { error ->
                            e.onError(handleFirebaseError(error))
                        }
                }
                .addOnFailureListener { error ->
                    e.onError(handleFirebaseError(error))
                }
        }.subscribeOn(Schedulers.io())
    }

    override fun checkIfUserExists(email: String): Single<Boolean> {
        return Single.create<Boolean> { e ->
            firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnSuccessListener { result: SignInMethodQueryResult ->
                    e.onSuccess(result.signInMethods != null && result.signInMethods!!.size > 0)
                }
                .addOnFailureListener { error ->
                    e.onError(handleFirebaseError(error))
                }
        }.subscribeOn(Schedulers.io())
    }

    override fun getUserEmail(): Single<String> {
        return Single.create<String> { e ->
            if (!isLogged()) {
                e.onError(UserNotFoundException())
            }

            val email = firebaseAuth.currentUser!!.email
            if (email != null) {
                e.onSuccess(email)
            } else {
                e.onError(UserNotFoundException())
            }
        }.subscribeOn(Schedulers.io())
    }

    override fun getToken(): Single<String> {
        return Single.create<String> { e ->
            if (!isLogged()) {
                e.onError(UserNotFoundException())
            }

            firebaseAuth.currentUser
                ?.getIdToken(false)
                ?.addOnSuccessListener { result ->
                    val token = result.token
                    if (token != null) {
                        e.onSuccess(token)
                    } else {
                        e.onError(UserNotFoundException())
                    }
                }
                ?.addOnFailureListener { error ->
                    e.onError(handleFirebaseError(error))
                }
        }.subscribeOn(Schedulers.io())
    }

    override fun getUid(): Single<String> {
        return Single.create<String> { e ->
            if (!isLogged()) {
                e.onError(UserNotFoundException())
            }

            e.onSuccess(firebaseAuth.currentUser!!.uid)
        }.subscribeOn(Schedulers.io())
    }

    override fun isLogged(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun sendVerificationEmail(): Completable {
        return Completable.create { e ->
            if (!isLogged()) {
                e.onError(UserNotFoundException())
            }

            firebaseAuth.currentUser
                ?.sendEmailVerification()
                ?.addOnSuccessListener { e.onComplete() }
                ?.addOnFailureListener { error ->
                    e.onError(handleFirebaseError(error))
                }
        }.subscribeOn(Schedulers.io())
    }

    override fun login(email: String, password: String): Completable {
        return Completable.create { e ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    e.onComplete() }
                .addOnFailureListener { error ->
                    e.onError(handleFirebaseError(error))
                }
        }.subscribeOn(Schedulers.io())
    }

    override fun sendResetPasswordEmail(email: String): Completable {
        return Completable.create { e ->
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener { e.onComplete() }
                .addOnFailureListener { error ->
                    e.onError(handleFirebaseError(error))
                }
        }.subscribeOn(Schedulers.io())
    }

    private fun handleFirebaseError(firebaseError: Throwable): Throwable {
        return when (firebaseError) {
            is FirebaseAuthUserCollisionException -> UserDuplicatedException()
            is FirebaseAuthActionCodeException -> UserOperationException()
            is FirebaseAuthEmailException -> UserOperationException()
            is FirebaseAuthInvalidCredentialsException -> InvalidUserCredentialsException()
            is FirebaseAuthInvalidUserException -> UserNotFoundException()
            is FirebaseAuthWeakPasswordException -> UserOperationException()
            is FirebaseNetworkException -> UserOperationException()
            is FirebaseTooManyRequestsException -> UserOperationException()
            else -> UserOperationException()
        }
    }

    override fun hasConfirmedEmail(): Single<Boolean> {
        return Single.create<Boolean> { e ->
            firebaseAuth.currentUser!!.reload()
                .addOnSuccessListener {
                    e.onSuccess(firebaseAuth.currentUser!!.isEmailVerified)
                }
                .addOnFailureListener { error ->
                    e.onError(handleFirebaseError(error))
                }
        }.subscribeOn(Schedulers.io())
    }

    override fun logout(): Completable {
        return Completable.fromAction {
            firebaseAuth.signOut()
        }
    }
}