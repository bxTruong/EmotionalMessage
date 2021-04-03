package com.truongbx.emotionalmessage.notification

import com.truongbx.emotionalmessage.notification.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InitRetrofit {
    companion object {
        private var retrofit: Retrofit? = null
        fun getClient(): Retrofit? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
    }
}