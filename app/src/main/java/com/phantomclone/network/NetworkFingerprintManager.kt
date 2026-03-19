package com.phantomclone.network

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages network-level fingerprint parameters to ensure consistency
 * across TLS handshakes and HTTP headers.
 *
 * In Phase 1, this class stores the per-profile network identity.
 * Phase 2 will implement JA3/JA4 TLS fingerprint customization
 * via a custom SSLSocketFactory.
 *
 * ## TLS Fingerprinting Background
 *
 * TLS fingerprints (JA3/JA4) are derived from the ClientHello message:
 * - Cipher suites offered
 * - TLS extensions list and order
 * - Elliptic curves supported
 * - Compression methods
 *
 * Different Android versions and device vendors use different TLS stacks
 * (Conscrypt, BoringSSL) which produce distinct fingerprints.
 *
 * Phase 2 plan:
 * 1. Implement a custom [javax.net.ssl.SSLSocketFactory] that reorders
 *    cipher suites and extensions to match the target device profile.
 * 2. Use OkHttp's `.sslSocketFactory()` to install the custom factory.
 */
@Singleton
class NetworkFingerprintManager @Inject constructor() {

    /**
     * DNS-over-HTTPS endpoints.
     * These are used when standard DNS might leak the user's queries.
     */
    val dohEndpoints = listOf(
        "https://cloudflare-dns.com/dns-query",
        "https://dns.google/resolve"
    )

    /**
     * Get a realistic Accept-Language header value for the given locale.
     */
    fun getAcceptLanguage(locale: String): String {
        val language = locale.substringBefore("_")
        val region = locale.substringAfter("_", "")
        return if (region.isNotEmpty()) {
            "$language-$region,$language;q=0.9,*;q=0.8"
        } else {
            "$language,*;q=0.8"
        }
    }

    /**
     * Get realistic HTTP Accept header for a mobile browser.
     */
    fun getAcceptHeader(): String =
        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"
}
