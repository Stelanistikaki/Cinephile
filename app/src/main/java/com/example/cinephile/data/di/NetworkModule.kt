package com.example.cinephile.data.di

import android.content.Context
import com.example.cinephile.R
import com.example.cinephile.data.remote.AuthInterceptor
import com.example.cinephile.data.remote.TheMovieDBApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * A Hilt module that configures and provides Retrofit, OkHttpClient, and the TMDB API service for network operations.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(authInterceptor).build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTheMovieDBApi(
        retrofit: Retrofit
    ): TheMovieDBApi {
        return retrofit.create(TheMovieDBApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTmdbApiToken(@ApplicationContext context: Context): String {
        return context.getString(R.string.tmdb_api_token)
    }
}