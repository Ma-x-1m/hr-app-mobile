package com.example.hr_app.data.api

import com.example.hr_app.data.local.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenStorage: TokenStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        if (path.endsWith("/auth/login") || path.endsWith("/auth/register")) {
            return chain.proceed(request)
        }

        val token = runBlocking { tokenStorage.getToken() }
        val authorized = if (token.isNullOrBlank()) {
            request
        } else {
            request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        }
        return chain.proceed(authorized)
    }
}
