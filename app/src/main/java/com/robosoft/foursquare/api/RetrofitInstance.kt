package com.robosoft.foursquare.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.robosoft.foursquare.helper.Constants
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitInstance @Inject constructor() {
    companion object {
        private fun isInternetAvailable(context: Context): Boolean {
            var isConnected: Boolean = false // Initial Value
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnected)
                isConnected = true
            return isConnected
        }

        private fun getRetrofitInstance(context: Context): Retrofit {
            val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
            val cache = Cache(context.cacheDir, cacheSize)

            val onlineInterceptor = Interceptor { chain ->
                val response = chain.proceed(chain.request())
                val maxAge =
                    60 // read from cache for 60 seconds even if there is internet connection
                response.newBuilder()
                    .header("Cache-Control", "public, max-age=$maxAge")
                    .removeHeader("Pragma")
                    .build()
            }

            val offlineInterceptor = Interceptor { chain ->
                var request: Request = chain.request()
                if (!isInternetAvailable(context)) {
                    val maxStale = 60 * 60 * 24 * 30 // Offline cache available for 30 days
                    request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .removeHeader("Pragma")
                        .build()
                }
                chain.proceed(request)
            }

            val okHttpClient: OkHttpClient =
                OkHttpClient.Builder() // .addInterceptor(provideHttpLoggingInterceptor()) // For HTTP request & Response data logging
                    .addInterceptor(offlineInterceptor)
                    .addNetworkInterceptor(onlineInterceptor)
                    .cache(cache)
                    .build()

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
        }

        fun getApiService(context: Context): ApiService {
            return getRetrofitInstance(context).create(ApiService::class.java)
        }

    }
}