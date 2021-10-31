package com.azuka.aplikasiujian.di

import com.azuka.aplikasiujian.data.authentication.AuthenticationDataSource
import com.azuka.aplikasiujian.data.firestore.FirestoreDataSource
import com.azuka.aplikasiujian.repository.AuthenticationRepository
import com.azuka.aplikasiujian.repository.AuthenticationRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent


/**
 * Created by ivanaazuka on 8/15/21.
 * Android Engineer
 */

@Module
@InstallIn(ActivityRetainedComponent::class)
object Module {

    @Provides
    fun provideDatabase(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    fun provideAuthenticationRepository(
        authDataSource: AuthenticationDataSource,
        firestoreDataSource: FirestoreDataSource
    ): AuthenticationRepository =
        AuthenticationRepositoryImpl(authDataSource, firestoreDataSource)
}