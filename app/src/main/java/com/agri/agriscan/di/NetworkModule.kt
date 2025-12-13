package com.agri.agriscan.di

import com.agri.agriscan.BuildConfig
import com.agri.agriscan.data.remote.api.OpenAIApi
import com.agri.agriscan.data.remote.api.PlantNetApi
import com.agri.agriscan.util.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PlantNetRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenAIRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun providePlantNetInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val originalUrl = original.url

            // Add API key as query parameter
            val url = originalUrl.newBuilder()
                .addQueryParameter("api-key", BuildConfig.PLANTNET_API_KEY)
                .build()

            val request = original.newBuilder()
                .url(url)
                .build()

            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideOpenAIInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()

            // Add API key as header
            val request = original.newBuilder()
                .header("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
                .header("Content-Type", "application/json")
                .build()

            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    @PlantNetRetrofit
    fun providePlantNetOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        plantNetInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(plantNetInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @OpenAIRetrofit
    fun provideOpenAIOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        openAIInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(openAIInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @PlantNetRetrofit
    fun providePlantNetRetrofit(
        @PlantNetRetrofit okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.PLANTNET_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @OpenAIRetrofit
    fun provideOpenAIRetrofit(
        @OpenAIRetrofit okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.OPENAI_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun providePlantNetApi(
        @PlantNetRetrofit retrofit: Retrofit
    ): PlantNetApi {
        return retrofit.create(PlantNetApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOpenAIApi(
        @OpenAIRetrofit retrofit: Retrofit
    ): OpenAIApi {
        return retrofit.create(OpenAIApi::class.java)
    }
}