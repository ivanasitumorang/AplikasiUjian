package com.azuka.aplikasiujian.di

import com.google.firebase.firestore.FirebaseFirestore
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
}