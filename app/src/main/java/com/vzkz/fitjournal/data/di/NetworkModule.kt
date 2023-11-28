package com.vzkz.fitjournal.data.di

import android.content.Context
import androidx.room.Room
import com.vzkz.fitjournal.data.network.ExerciseApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vzkz.fitjournal.BuildConfig.BASE_URL
import com.vzkz.fitjournal.data.DataStoreRepositoryImpl
import com.vzkz.fitjournal.data.RepositoryImpl
import com.vzkz.fitjournal.data.database.UserDB
import com.vzkz.fitjournal.data.database.dao.UserDao
import com.vzkz.fitjournal.data.firebase.AuthService
import com.vzkz.fitjournal.data.firebase.FirestoreService
import com.vzkz.fitjournal.data.network.interceptor.AuthInterceptor
import com.vzkz.fitjournal.domain.DataStoreRepository
import com.vzkz.fitjournal.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideRepository(
        authService: AuthService,
        firestoreService: FirestoreService,
        @ApplicationContext context: Context,
        exerciseApiService: ExerciseApiService,
        roomDB: UserDao
    ): Repository {
        return RepositoryImpl(authService, firestoreService, context, exerciseApiService, roomDB)
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

    //Retrofit
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient{
        return OkHttpClient
            .Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    fun provideExerciseApiService(retrofit: Retrofit): ExerciseApiService {
        return retrofit.create(ExerciseApiService::class.java)
    }

    //Room
    private const val USER_DB_NAME = "user_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, UserDB::class.java, USER_DB_NAME).build()

    @Singleton
    @Provides
    fun provideUserDao(db: UserDB) = db.getUserDao()

}