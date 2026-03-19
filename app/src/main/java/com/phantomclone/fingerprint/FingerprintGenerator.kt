package com.phantomclone.fingerprint

import com.phantomclone.data.model.DeviceFingerprint
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Generates realistic, internally-consistent device fingerprints.
 *
 * Each call to [generate] picks a random real device from [RealDeviceDatabase]
 * and fills in the unique identifiers (Android ID, IMEI, MAC, etc.) with
 * randomly generated but correctly formatted values.
 *
 * **Design principle:** All fields must be consistent — e.g., a Samsung Galaxy S24
 * will always have:
 * - `manufacturer = "Samsung"`
 * - matching Adreno GPU renderer
 * - appropriate 1080x2340 screen resolution
 * - Android 14 / SDK 34
 */
@Singleton
class FingerprintGenerator @Inject constructor() {

    /**
     * Generate a fresh [DeviceFingerprint] by selecting a random device
     * from the database and populating all unique identifiers.
     *
     * @return A fully populated, internally consistent [DeviceFingerprint].
     */
    fun generate(): DeviceFingerprint {
        val spec = RealDeviceDatabase.devices.random()
        val locale = spec.locales.random()
        val language = locale.substringBefore("_")
        val timezone = pickTimezone(locale)
        val carrier = spec.carriers.random()
        val androidId = generateAndroidId()
        val serial = generateSerial()
        val mac = generateMac()
        val imei = generateImei()
        val phoneNumber = generatePhoneNumber(locale)
        val googleAdId = UUID.randomUUID().toString()

        val buildTag = "release-keys"
        val buildFingerprint = buildBuildFingerprint(spec, buildTag)
        val buildDisplay = buildBuildDisplay(spec)
        val userAgent = buildUserAgent(spec, locale)

        return DeviceFingerprint(
            manufacturer = spec.manufacturer,
            brand = spec.brand,
            model = spec.model,
            product = spec.product,
            hardware = spec.hardware,
            board = spec.board,
            buildFingerprint = buildFingerprint,
            buildDisplay = buildDisplay,
            androidVersion = spec.androidVersion,
            sdkInt = spec.sdkInt,
            cpuAbi = spec.cpuAbi,
            cpuAbi2 = spec.cpuAbi2,
            screenWidth = spec.screenWidth,
            screenHeight = spec.screenHeight,
            dpi = spec.dpi,
            androidId = androidId,
            googleAdId = googleAdId,
            wifiMac = mac,
            imei = imei,
            phoneNumber = phoneNumber,
            carrier = carrier,
            timezone = timezone,
            locale = locale,
            language = language,
            userAgent = userAgent,
            glRenderer = spec.glRenderer,
            glVendor = spec.glVendor,
            sensors = spec.sensors,
            serial = serial
        )
    }

    // ------------------------------------------------------------------ Private helpers

    /**
     * Build a realistic `Build.FINGERPRINT` string matching real Android patterns.
     * Format: `brand/product/device:version/build_id/build_variant:type/tags`
     */
    private fun buildBuildFingerprint(spec: DeviceSpec, buildTag: String): String {
        val buildId = generateBuildId(spec)
        return "${spec.brand}/${spec.product}/${spec.hardware}:" +
                "${spec.androidVersion}/$buildId/" +
                "${Random.nextInt(100000, 999999)}:user/$buildTag"
    }

    /**
     * Build a realistic `Build.DISPLAY` string.
     */
    private fun buildBuildDisplay(spec: DeviceSpec): String {
        val buildId = generateBuildId(spec)
        return "$buildId.${Random.nextInt(100000, 999999)}"
    }

    /**
     * Generate a plausible build ID for the device (e.g., UP1A.231005.007).
     */
    private fun generateBuildId(spec: DeviceSpec): String {
        return when {
            spec.manufacturer == "Google" -> {
                // Pixel format: UP1A.YYMMDD.NNN
                val prefixes = listOf("UP1A", "UQ1A", "UD1A", "TQ3A", "TP1A")
                val prefix = prefixes.random()
                "$prefix.${230000 + Random.nextInt(10000)}.${Random.nextInt(100).toString().padStart(3, '0')}"
            }
            spec.manufacturer == "Samsung" -> {
                // Samsung format: UP1A.231005.007.S911BXXU5EXF7
                "S${spec.model.takeLast(4).uppercase()}XXU${Random.nextInt(1, 9)}EXF${Random.nextInt(1, 9)}"
            }
            else -> {
                // Generic AOSP-style
                "TP1A.220624.${Random.nextInt(10).toString().padStart(3, '0')}"
            }
        }
    }

    /**
     * Build a realistic WebView user-agent string for the device.
     * Format matches real Chrome on Android.
     */
    private fun buildUserAgent(spec: DeviceSpec, locale: String): String {
        val chromeVersion = "120.0.${Random.nextInt(6000, 6300)}.${Random.nextInt(100, 200)}"
        val webkitVersion = "537.36"
        return "Mozilla/5.0 (Linux; Android ${spec.androidVersion}; ${spec.model} Build/${generateBuildId(spec)}; wv) " +
                "AppleWebKit/$webkitVersion (KHTML, like Gecko) " +
                "Version/4.0 Chrome/$chromeVersion Mobile Safari/$webkitVersion"
    }

    /**
     * Generate a random 16-character hex Android ID.
     */
    private fun generateAndroidId(): String =
        (1..16).map { "0123456789abcdef".random() }.joinToString("")

    /**
     * Generate a random build serial number (alphanumeric, 12 chars).
     */
    private fun generateSerial(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..12).map { chars.random() }.joinToString("")
    }

    /**
     * Generate a random WiFi MAC address in the format XX:XX:XX:XX:XX:XX.
     * The first byte is forced to be a valid unicast address (LSB of first octet = 0).
     */
    private fun generateMac(): String {
        val bytes = (1..6).map { Random.nextInt(0, 256) }.toMutableList()
        // Ensure unicast (bit 0 of first byte = 0) and locally administered (bit 1 = 1)
        bytes[0] = (bytes[0] and 0xFE) or 0x02
        return bytes.joinToString(":") { it.toString(16).padStart(2, '0').uppercase() }
    }

    /**
     * Generate a valid 15-digit IMEI number using the Luhn algorithm.
     */
    fun generateImei(): String {
        // TAC prefix (Type Allocation Code) — use realistic Samsung/Qualcomm TACs
        val tacPrefixes = listOf(
            "35426910", "35978100", "35352310", "35845400",
            "86480000", "86612600", "35355600", "86887100"
        )
        val tac = tacPrefixes.random()
        val serial = (1..6).map { Random.nextInt(0, 10) }.joinToString("")
        val partial = tac + serial
        val checkDigit = luhnCheckDigit(partial)
        return partial + checkDigit
    }

    /**
     * Calculate the Luhn check digit for a numeric string.
     */
    private fun luhnCheckDigit(number: String): Int {
        var sum = 0
        var alternate = true
        for (i in number.length - 1 downTo 0) {
            var n = number[i].digitToInt()
            if (alternate) {
                n *= 2
                if (n > 9) n -= 9
            }
            sum += n
            alternate = !alternate
        }
        return (10 - (sum % 10)) % 10
    }

    /**
     * Generate a plausible phone number for the given locale.
     */
    private fun generatePhoneNumber(locale: String): String {
        val country = locale.substringAfter("_", "US")
        return when (country) {
            "US" -> "+1${Random.nextInt(200, 999)}${Random.nextInt(100, 999)}${Random.nextInt(1000, 9999)}"
            "GB" -> "+44${Random.nextInt(7000, 7999)}${Random.nextInt(100000, 999999)}"
            "DE" -> "+49${Random.nextInt(150, 179)}${Random.nextInt(1000000, 9999999)}"
            "FR" -> "+33${Random.nextInt(6, 7)}${Random.nextInt(10000000, 99999999)}"
            "CN" -> "+86${Random.nextInt(130, 199)}${Random.nextInt(10000000, 99999999)}"
            "JP" -> "+81${Random.nextInt(70, 90)}${Random.nextInt(10000000, 99999999)}"
            "KR" -> "+82${Random.nextInt(10, 19)}${Random.nextInt(10000000, 99999999)}"
            "RU" -> "+7${Random.nextInt(900, 999)}${Random.nextInt(1000000, 9999999)}"
            else -> "+1${Random.nextInt(200, 999)}${Random.nextInt(100, 999)}${Random.nextInt(1000, 9999)}"
        }
    }

    /**
     * Pick a representative timezone for the given locale.
     */
    private fun pickTimezone(locale: String): String {
        val country = locale.substringAfter("_", "US")
        return when (country) {
            "US" -> listOf("America/New_York", "America/Chicago", "America/Denver", "America/Los_Angeles").random()
            "GB" -> "Europe/London"
            "DE" -> "Europe/Berlin"
            "FR" -> "Europe/Paris"
            "CN" -> "Asia/Shanghai"
            "JP" -> "Asia/Tokyo"
            "KR" -> "Asia/Seoul"
            "AU" -> listOf("Australia/Sydney", "Australia/Melbourne").random()
            "CA" -> listOf("America/Toronto", "America/Vancouver").random()
            "RU" -> "Europe/Moscow"
            "IN" -> "Asia/Kolkata"
            else -> "UTC"
        }
    }
}
