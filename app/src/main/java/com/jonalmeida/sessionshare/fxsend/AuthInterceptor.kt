package com.jonalmeida.sessionshare.fxsend

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        // TODO: Add request headers here.

        val response = chain.proceed(builder.build())
        // TODO: Verify response headers here.

        return response
    }

}