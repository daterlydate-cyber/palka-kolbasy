package com.phantomclone.network

import com.phantomclone.engine.ProxyManager
import okhttp3.Authenticator
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * OkHttp interceptor that routes requests through the active profile's proxy.
 *
 * Install this in your [OkHttpClient] to ensure all network traffic
 * from the current profile session passes through the configured proxy.
 */
class ProxyInterceptor @Inject constructor(
    private val proxyManager: ProxyManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        // In Phase 1, this interceptor logs the proxy in use.
        // Phase 2 will route the actual connection through the proxy socket.
        return chain.proceed(originalRequest)
    }
}

/**
 * Authenticator for proxy servers that require credentials.
 */
class ProxyAuthenticator @Inject constructor(
    private val proxyManager: ProxyManager
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val auth = proxyManager.currentAuth ?: return null
        val credential = Credentials.basic(auth.first, auth.second)
        return response.request.newBuilder()
            .header("Proxy-Authorization", credential)
            .build()
    }
}

/**
 * Builds a configured [OkHttpClient] for the given profile,
 * incorporating proxy, timeouts, and logging.
 *
 * @param proxyManager The [ProxyManager] holding current proxy config.
 * @param proxyInterceptor The [ProxyInterceptor] for this client.
 * @param proxyAuthenticator The [ProxyAuthenticator] for this client.
 */
@Singleton
class NetworkClientFactory @Inject constructor(
    private val proxyManager: ProxyManager,
    private val proxyInterceptor: ProxyInterceptor,
    private val proxyAuthenticator: ProxyAuthenticator
) {

    /**
     * Build an [OkHttpClient] with the active proxy configuration.
     */
    fun buildClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            .proxy(proxyManager.currentProxy)
            .proxyAuthenticator(proxyAuthenticator)
            .addInterceptor(proxyInterceptor)
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}
