package com.app.bmicalculator.network.retrofit

import com.app.bmicalculator.utils.Const
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
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkingProvider {

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    var appInterceptor: Interceptor? = null

    @Singleton
    @Provides
    fun provideHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        if (appInterceptor == null)
            appInterceptor = MyAppInterceptor()

        httpClient.addInterceptor(appInterceptor!!)
        httpClient.addInterceptor(logging)
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        httpClient.retryOnConnectionFailure(true)
        return httpClient.build()
    }

    @Singleton
    @Provides
    fun provideApiProvider(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(Const.SERVER_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}


