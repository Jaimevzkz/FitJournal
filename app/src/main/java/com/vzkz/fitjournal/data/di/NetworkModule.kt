package com.vzkz.fitjournal.data.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vzkz.fitjournal.data.DataStoreRepositoryImpl
import com.vzkz.fitjournal.data.RepositoryImpl
import com.vzkz.fitjournal.data.firebase.AuthService
import com.vzkz.fitjournal.data.firebase.FirestoreService
import com.vzkz.fitjournal.domain.DataStoreRepository
import com.vzkz.fitjournal.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideRepository(
        authService: AuthService,
        firestoreService: FirestoreService,
        @ApplicationContext context: Context
    ): Repository {
        return RepositoryImpl(authService, firestoreService, context)
    }

    @Singleton
    @Provides
    fun provideDataStoreRepository(
        @ApplicationContext app: Context
    ): DataStoreRepository = DataStoreRepositoryImpl(app)

    //Firebase
    @Singleton
    @Provides
    fun provideFireBaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFireStore(): FirebaseFirestore = Firebase.firestore
}