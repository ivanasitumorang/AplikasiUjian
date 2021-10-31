package com.azuka.aplikasiujian.di

import com.azuka.aplikasiujian.repository.AuthenticationRepository
import com.azuka.aplikasiujian.repository.AuthenticationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent


//@Module
//@InstallIn(ViewModelComponent::class)
abstract class AbstractModule {
//    @Binds
//    abstract fun provideAuthenticationRepository(repo: AuthenticationRepositoryImpl): AuthenticationRepository
}