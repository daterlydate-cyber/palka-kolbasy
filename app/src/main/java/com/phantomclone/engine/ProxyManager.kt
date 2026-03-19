package com.phantomclone.engine

import com.phantomclone.data.model.Profile
import com.phantomclone.data.model.ProxyConfig
import com.phantomclone.data.model.ProxyType
import java.net.InetSocketAddress
import java.net.Proxy
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages per-profile proxy configuration.
 *
 * Provides a [java.net.Proxy] instance for each profile based on its
 * [ProxyConfig] settings (HTTP or SOCKS5).
 *
 * For OkHttp integration, see [com.phantomclone.network.ProxyInterceptor].
 */
@Singleton
class ProxyManager @Inject constructor() {

    /**
     * Configure system-level proxy for the given profile.
     *
     * Note: Setting a per-process Java proxy only affects Java networking
     * (HttpURLConnection, etc.). For OkHttp-based networking, use
     * [buildOkHttpProxy] when constructing the OkHttpClient.
     */
    fun configureForProfile(profile: Profile) {
        val proxy = buildProxy(profile.proxyConfig)
        // Store as active proxy — OkHttp clients will pick this up via buildOkHttpProxy()
        currentProxy = proxy
        currentAuth = if (profile.proxyConfig.username.isNotEmpty()) {
            profile.proxyConfig.username to profile.proxyConfig.password
        } else null
    }

    /**
     * Build a [java.net.Proxy] for the given [ProxyConfig].
     *
     * @return A configured [Proxy], or [Proxy.NO_PROXY] if type is NONE.
     */
    fun buildProxy(config: ProxyConfig): Proxy {
        return when (config.type) {
            ProxyType.NONE -> Proxy.NO_PROXY
            ProxyType.HTTP -> Proxy(
                Proxy.Type.HTTP,
                InetSocketAddress(config.host, config.port)
            )
            ProxyType.SOCKS5 -> Proxy(
                Proxy.Type.SOCKS,
                InetSocketAddress(config.host, config.port)
            )
        }
    }

    /** The currently active proxy (set when a profile is launched). */
    var currentProxy: Proxy = Proxy.NO_PROXY
        private set

    /** Current proxy authentication credentials (username to password), or null. */
    var currentAuth: Pair<String, String>? = null
        private set

    /** Reset to no proxy. */
    fun reset() {
        currentProxy = Proxy.NO_PROXY
        currentAuth = null
    }

    /**
     * Test connectivity through the given [ProxyConfig].
     *
     * @return true if the proxy is reachable, false otherwise.
     */
    suspend fun testProxy(config: ProxyConfig): Boolean {
        if (config.type == ProxyType.NONE) return true
        return try {
            val proxy = buildProxy(config)
            val socket = java.net.Socket(proxy)
            socket.connect(InetSocketAddress(config.host, config.port), 5000)
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
    }
}
