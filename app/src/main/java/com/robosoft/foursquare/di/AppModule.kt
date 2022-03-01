package com.robosoft.foursquare.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.robosoft.foursquare.api.ApiService
import com.robosoft.foursquare.helper.Constants
import com.robosoft.foursquare.room.FourSquareDao
import com.robosoft.foursquare.room.FourSquareRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }

    @Provides
    fun providesOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val cacheSize = (10 * 1024 * 1024).toLong()
        val cache = Cache(context.cacheDir, cacheSize)
        val onlineInterceptor = Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val maxAge = 60
            response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Pragma")
                .build()
        }
        val offlineInterceptor = Interceptor { chain ->
            var request: Request = chain.request()
            if (!isInternetAvailable(context)) {
                val maxStale = 60 * 60 * 24 * 30
                request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .removeHeader("Pragma")
                    .build()
            }
            chain.proceed(request)
        }
        return OkHttpClient.Builder()
            .addInterceptor(offlineInterceptor)
            .addNetworkInterceptor(onlineInterceptor)
            .cache(cache)
            .build()
    }


    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideFusedLocation(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    fun provideRoomInstance(@ApplicationContext context: Context): FourSquareDao {
        return FourSquareRoomDatabase.getInstance(context).fourSquareDao
    }
}