package com.phantomclone.fingerprint

/**
 * Specification for a real device model used as the base for fingerprint generation.
 *
 * @property manufacturer OEM name (e.g., "Samsung").
 * @property brand Brand field (e.g., "samsung").
 * @property model Model number (e.g., "SM-S928B").
 * @property product Product codename (e.g., "e1s").
 * @property hardware Hardware string (e.g., "qcom").
 * @property board Board name (e.g., "kalama").
 * @property androidVersion Android OS version string (e.g., "14").
 * @property sdkInt Android SDK level.
 * @property cpuAbi Primary ABI.
 * @property cpuAbi2 Secondary ABI.
 * @property screenWidth Logical screen width in pixels.
 * @property screenHeight Logical screen height in pixels.
 * @property dpi Screen DPI.
 * @property glRenderer OpenGL ES renderer string.
 * @property glVendor OpenGL ES vendor.
 * @property sensors List of sensor names shipped with this device.
 * @property carriers List of common carriers for this device.
 * @property locales Common locale/region combinations for this device.
 */
data class DeviceSpec(
    val manufacturer: String,
    val brand: String,
    val model: String,
    val product: String,
    val hardware: String,
    val board: String,
    val androidVersion: String,
    val sdkInt: Int,
    val cpuAbi: String,
    val cpuAbi2: String,
    val screenWidth: Int,
    val screenHeight: Int,
    val dpi: Int,
    val glRenderer: String,
    val glVendor: String,
    val sensors: List<String>,
    val carriers: List<String>,
    val locales: List<String>
)

/**
 * Database of 50+ real Android device specifications.
 *
 * Used by [FingerprintGenerator] to produce internally consistent fingerprints.
 * Each entry reflects a real device's actual Build.* fields, GPU, screen, and sensors.
 */
object RealDeviceDatabase {

    private val COMMON_SENSORS = listOf(
        "Accelerometer", "Gyroscope", "Proximity sensor",
        "Ambient light sensor", "Magnetic field sensor",
        "Gravity sensor", "Linear acceleration sensor",
        "Rotation vector sensor", "Step counter", "Step detector"
    )

    private val SAMSUNG_SENSORS = COMMON_SENSORS + listOf(
        "Barometer", "Hall sensor", "Samsung Game Rotation Vector"
    )

    private val PIXEL_SENSORS = COMMON_SENSORS + listOf(
        "Barometer", "Hinge angle sensor"
    )

    private val CARRIERS_US = listOf("T-Mobile", "AT&T", "Verizon", "Sprint", "US Cellular")
    private val CARRIERS_EU = listOf("Vodafone", "Orange", "T-Mobile", "O2", "Three")
    private val CARRIERS_ASIA = listOf("China Mobile", "China Unicom", "SoftBank", "NTT Docomo", "SKT")
    private val CARRIERS_GLOBAL = CARRIERS_US + CARRIERS_EU

    private val LOCALES_EN = listOf("en_US", "en_GB", "en_CA", "en_AU")
    private val LOCALES_MIXED = listOf("en_US", "zh_CN", "ja_JP", "ko_KR", "de_DE", "fr_FR", "es_ES")

    /** The full list of real device specs. */
    val devices: List<DeviceSpec> = listOf(
        // ---- Samsung Galaxy S24 Ultra ----
        DeviceSpec(
            manufacturer = "Samsung", brand = "samsung", model = "SM-S928B",
            product = "e1sxx", hardware = "qcom", board = "kalama",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1440, screenHeight = 3088, dpi = 505,
            glRenderer = "Adreno (TM) 750", glVendor = "Qualcomm",
            sensors = SAMSUNG_SENSORS, carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- Samsung Galaxy S24+ ----
        DeviceSpec(
            manufacturer = "Samsung", brand = "samsung", model = "SM-S926B",
            product = "e2sxx", hardware = "qcom", board = "kalama",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2340, dpi = 393,
            glRenderer = "Adreno (TM) 750", glVendor = "Qualcomm",
            sensors = SAMSUNG_SENSORS, carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- Samsung Galaxy S24 ----
        DeviceSpec(
            manufacturer = "Samsung", brand = "samsung", model = "SM-S921B",
            product = "e0sxx", hardware = "qcom", board = "kalama",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2340, dpi = 416,
            glRenderer = "Adreno (TM) 750", glVendor = "Qualcomm",
            sensors = SAMSUNG_SENSORS, carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- Samsung Galaxy S23 Ultra ----
        DeviceSpec(
            manufacturer = "Samsung", brand = "samsung", model = "SM-S918B",
            product = "q5q", hardware = "qcom", board = "kalama",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1440, screenHeight = 3088, dpi = 500,
            glRenderer = "Adreno (TM) 740", glVendor = "Qualcomm",
            sensors = SAMSUNG_SENSORS, carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- Samsung Galaxy S23 ----
        DeviceSpec(
            manufacturer = "Samsung", brand = "samsung", model = "SM-S911B",
            product = "dm2q", hardware = "qcom", board = "crow",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2340, dpi = 416,
            glRenderer = "Adreno (TM) 740", glVendor = "Qualcomm",
            sensors = SAMSUNG_SENSORS, carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- Samsung Galaxy S22 Ultra ----
        DeviceSpec(
            manufacturer = "Samsung", brand = "samsung", model = "SM-S908B",
            product = "b0q", hardware = "qcom", board = "b0q",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1440, screenHeight = 3088, dpi = 500,
            glRenderer = "Adreno (TM) 730", glVendor = "Qualcomm",
            sensors = SAMSUNG_SENSORS, carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- Samsung Galaxy S22 ----
        DeviceSpec(
            manufacturer = "Samsung", brand = "samsung", model = "SM-S901B",
            product = "r0q", hardware = "qcom", board = "r0q",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2340, dpi = 425,
            glRenderer = "Adreno (TM) 730", glVendor = "Qualcomm",
            sensors = SAMSUNG_SENSORS, carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- Samsung Galaxy A54 5G ----
        DeviceSpec(
            manufacturer = "Samsung", brand = "samsung", model = "SM-A546B",
            product = "a54xxx", hardware = "exynos1380", board = "exynos1380",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2340, dpi = 390,
            glRenderer = "Mali-G68", glVendor = "ARM",
            sensors = SAMSUNG_SENSORS, carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- Samsung Galaxy A34 5G ----
        DeviceSpec(
            manufacturer = "Samsung", brand = "samsung", model = "SM-A346B",
            product = "a34xeea", hardware = "mt6877", board = "mt6877",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2408, dpi = 390,
            glRenderer = "Mali-G57 MC2", glVendor = "ARM",
            sensors = COMMON_SENSORS, carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- Google Pixel 8 Pro ----
        DeviceSpec(
            manufacturer = "Google", brand = "google", model = "Pixel 8 Pro",
            product = "husky", hardware = "husky", board = "husky",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1344, screenHeight = 2992, dpi = 489,
            glRenderer = "Mali-G715", glVendor = "ARM",
            sensors = PIXEL_SENSORS, carriers = CARRIERS_US,
            locales = LOCALES_EN
        ),
        // ---- Google Pixel 8 ----
        DeviceSpec(
            manufacturer = "Google", brand = "google", model = "Pixel 8",
            product = "shiba", hardware = "shiba", board = "shiba",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2400, dpi = 428,
            glRenderer = "Mali-G715", glVendor = "ARM",
            sensors = PIXEL_SENSORS, carriers = CARRIERS_US,
            locales = LOCALES_EN
        ),
        // ---- Google Pixel 7 Pro ----
        DeviceSpec(
            manufacturer = "Google", brand = "google", model = "Pixel 7 Pro",
            product = "cheetah", hardware = "cheetah", board = "cheetah",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1440, screenHeight = 3120, dpi = 512,
            glRenderer = "Mali-G710", glVendor = "ARM",
            sensors = PIXEL_SENSORS, carriers = CARRIERS_US,
            locales = LOCALES_EN
        ),
        // ---- Google Pixel 7 ----
        DeviceSpec(
            manufacturer = "Google", brand = "google", model = "Pixel 7",
            product = "panther", hardware = "panther", board = "panther",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2400, dpi = 416,
            glRenderer = "Mali-G710", glVendor = "ARM",
            sensors = PIXEL_SENSORS, carriers = CARRIERS_US,
            locales = LOCALES_EN
        ),
        // ---- Google Pixel 6 Pro ----
        DeviceSpec(
            manufacturer = "Google", brand = "google", model = "Pixel 6 Pro",
            product = "raven", hardware = "raven", board = "raven",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1440, screenHeight = 3120, dpi = 512,
            glRenderer = "Mali-G78", glVendor = "ARM",
            sensors = PIXEL_SENSORS, carriers = CARRIERS_US,
            locales = LOCALES_EN
        ),
        // ---- Xiaomi 14 Ultra ----
        DeviceSpec(
            manufacturer = "Xiaomi", brand = "Xiaomi", model = "24030PN60G",
            product = "dulcinea", hardware = "qcom", board = "pineapple",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1260, screenHeight = 2800, dpi = 522,
            glRenderer = "Adreno (TM) 750", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_ASIA + CARRIERS_EU,
            locales = LOCALES_MIXED
        ),
        // ---- Xiaomi 14 ----
        DeviceSpec(
            manufacturer = "Xiaomi", brand = "Xiaomi", model = "23127PN0CC",
            product = "houhou", hardware = "qcom", board = "pineapple",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1200, screenHeight = 2670, dpi = 460,
            glRenderer = "Adreno (TM) 750", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_ASIA + CARRIERS_EU,
            locales = LOCALES_MIXED
        ),
        // ---- Xiaomi 13 Pro ----
        DeviceSpec(
            manufacturer = "Xiaomi", brand = "Xiaomi", model = "2210132C",
            product = "nuwa", hardware = "qcom", board = "taro",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1440, screenHeight = 3200, dpi = 522,
            glRenderer = "Adreno (TM) 740", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_ASIA + CARRIERS_EU,
            locales = LOCALES_MIXED
        ),
        // ---- Xiaomi Redmi Note 12 Pro ----
        DeviceSpec(
            manufacturer = "Xiaomi", brand = "Redmi", model = "22101316C",
            product = "rubens", hardware = "mt6877", board = "mt6877",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2400, dpi = 395,
            glRenderer = "Mali-G57 MC2", glVendor = "ARM",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_ASIA + CARRIERS_EU,
            locales = LOCALES_MIXED
        ),
        // ---- OnePlus 12 ----
        DeviceSpec(
            manufacturer = "OnePlus", brand = "OnePlus", model = "CPH2573",
            product = "op6f99l1", hardware = "qcom", board = "pineapple",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1440, screenHeight = 3168, dpi = 510,
            glRenderer = "Adreno (TM) 750", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_GLOBAL,
            locales = LOCALES_MIXED
        ),
        // ---- OnePlus 11 ----
        DeviceSpec(
            manufacturer = "OnePlus", brand = "OnePlus", model = "CPH2449",
            product = "salami", hardware = "qcom", board = "taro",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1440, screenHeight = 3216, dpi = 525,
            glRenderer = "Adreno (TM) 740", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_GLOBAL,
            locales = LOCALES_MIXED
        ),
        // ---- OPPO Find X7 Ultra ----
        DeviceSpec(
            manufacturer = "OPPO", brand = "OPPO", model = "PHZ110",
            product = "OP5916L1", hardware = "qcom", board = "pineapple",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1440, screenHeight = 3168, dpi = 510,
            glRenderer = "Adreno (TM) 750", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_ASIA,
            locales = LOCALES_MIXED
        ),
        // ---- Vivo X100 Pro ----
        DeviceSpec(
            manufacturer = "vivo", brand = "vivo", model = "V2309A",
            product = "V2309A", hardware = "mt6989", board = "mt6989",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1260, screenHeight = 2800, dpi = 524,
            glRenderer = "Immortalis-G715", glVendor = "ARM",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_ASIA,
            locales = listOf("zh_CN", "en_US")
        ),
        // ---- Huawei P60 Pro ----
        DeviceSpec(
            manufacturer = "HUAWEI", brand = "HUAWEI", model = "MNA-AL00",
            product = "MNA-AL00", hardware = "kirin9000s", board = "kirin9000s",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1220, screenHeight = 2700, dpi = 451,
            glRenderer = "Maleoon 910", glVendor = "Hisilicon",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_ASIA,
            locales = listOf("zh_CN", "en_US")
        ),
        // ---- Sony Xperia 1 V ----
        DeviceSpec(
            manufacturer = "Sony", brand = "Sony", model = "XQ-DQ72",
            product = "pdx234", hardware = "qcom", board = "kalama",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1644, screenHeight = 3840, dpi = 643,
            glRenderer = "Adreno (TM) 740", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_GLOBAL,
            locales = LOCALES_MIXED
        ),
        // ---- Motorola Edge 40 Pro ----
        DeviceSpec(
            manufacturer = "motorola", brand = "motorola", model = "XT2301-4",
            product = "rtwo_g", hardware = "qcom", board = "msmnile",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2400, dpi = 395,
            glRenderer = "Adreno (TM) 650", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_US + CARRIERS_EU,
            locales = LOCALES_EN
        ),
        // ---- Nothing Phone (2) ----
        DeviceSpec(
            manufacturer = "Nothing", brand = "Nothing", model = "A065",
            product = "Pong", hardware = "qcom", board = "taro",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2412, dpi = 394,
            glRenderer = "Adreno (TM) 740", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- Realme GT5 Pro ----
        DeviceSpec(
            manufacturer = "realme", brand = "realme", model = "RMX3888",
            product = "RMX3888", hardware = "qcom", board = "pineapple",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1260, screenHeight = 2800, dpi = 522,
            glRenderer = "Adreno (TM) 750", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_ASIA + CARRIERS_EU,
            locales = LOCALES_MIXED
        ),
        // ---- ASUS ROG Phone 8 Pro ----
        DeviceSpec(
            manufacturer = "asus", brand = "asus", model = "AI2401",
            product = "AI2401_D", hardware = "qcom", board = "pineapple",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2400, dpi = 446,
            glRenderer = "Adreno (TM) 750", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_GLOBAL,
            locales = LOCALES_MIXED
        ),
        // ---- Samsung Galaxy Z Fold5 ----
        DeviceSpec(
            manufacturer = "Samsung", brand = "samsung", model = "SM-F946B",
            product = "q5q", hardware = "qcom", board = "kalama",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1812, screenHeight = 2176, dpi = 374,
            glRenderer = "Adreno (TM) 740", glVendor = "Qualcomm",
            sensors = SAMSUNG_SENSORS, carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- Samsung Galaxy Z Flip5 ----
        DeviceSpec(
            manufacturer = "Samsung", brand = "samsung", model = "SM-F731B",
            product = "b5q", hardware = "qcom", board = "kalama",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2640, dpi = 425,
            glRenderer = "Adreno (TM) 740", glVendor = "Qualcomm",
            sensors = SAMSUNG_SENSORS, carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- Google Pixel 6a ----
        DeviceSpec(
            manufacturer = "Google", brand = "google", model = "Pixel 6a",
            product = "bluejay", hardware = "bluejay", board = "bluejay",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2400, dpi = 429,
            glRenderer = "Mali-G78", glVendor = "ARM",
            sensors = PIXEL_SENSORS, carriers = CARRIERS_US,
            locales = LOCALES_EN
        ),
        // ---- Xiaomi Redmi 12 5G ----
        DeviceSpec(
            manufacturer = "Xiaomi", brand = "Redmi", model = "23076RN4BI",
            product = "sky", hardware = "qcom", board = "crow",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2400, dpi = 395,
            glRenderer = "Adreno (TM) 619", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_ASIA,
            locales = LOCALES_MIXED
        ),
        // ---- OnePlus Nord 3 ----
        DeviceSpec(
            manufacturer = "OnePlus", brand = "OnePlus", model = "CPH2493",
            product = "ovaltine", hardware = "mt6983", board = "mt6983",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2412, dpi = 450,
            glRenderer = "Immortalis-G715", glVendor = "ARM",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_GLOBAL,
            locales = LOCALES_MIXED
        ),
        // ---- Motorola Moto G84 ----
        DeviceSpec(
            manufacturer = "motorola", brand = "motorola", model = "XT2347-2",
            product = "bangkk", hardware = "qcom", board = "crow",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2400, dpi = 400,
            glRenderer = "Adreno (TM) 619", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_US + CARRIERS_EU,
            locales = LOCALES_EN
        ),
        // ---- Nokia G42 5G ----
        DeviceSpec(
            manufacturer = "HMD Global", brand = "Nokia", model = "Nokia G42 5G",
            product = "liewe_eea", hardware = "qcom", board = "crow",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 720, screenHeight = 1612, dpi = 270,
            glRenderer = "Adreno (TM) 619", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_EU,
            locales = LOCALES_EN
        ),
        // ---- Samsung Galaxy M54 5G ----
        DeviceSpec(
            manufacturer = "Samsung", brand = "samsung", model = "SM-M546B",
            product = "m54xeea", hardware = "exynos1380", board = "exynos1380",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2340, dpi = 390,
            glRenderer = "Mali-G68", glVendor = "ARM",
            sensors = SAMSUNG_SENSORS, carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- Xiaomi POCO X5 Pro 5G ----
        DeviceSpec(
            manufacturer = "Xiaomi", brand = "POCO", model = "22101320G",
            product = "redwood", hardware = "qcom", board = "crow",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2400, dpi = 395,
            glRenderer = "Adreno (TM) 695", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_ASIA + CARRIERS_EU,
            locales = LOCALES_MIXED
        ),
        // ---- Realme 11 Pro+ ----
        DeviceSpec(
            manufacturer = "realme", brand = "realme", model = "RMX3741",
            product = "RMX3741", hardware = "mt6877", board = "mt6877",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2412, dpi = 394,
            glRenderer = "Mali-G68 MC4", glVendor = "ARM",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_ASIA + CARRIERS_EU,
            locales = LOCALES_MIXED
        ),
        // ---- Samsung Galaxy A24 ----
        DeviceSpec(
            manufacturer = "Samsung", brand = "samsung", model = "SM-A245F",
            product = "a24xeea", hardware = "mt6877", board = "mt6877",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2340, dpi = 406,
            glRenderer = "Mali-G57 MC2", glVendor = "ARM",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- OnePlus Nord CE3 Lite 5G ----
        DeviceSpec(
            manufacturer = "OnePlus", brand = "OnePlus", model = "CPH2465",
            product = "pickle", hardware = "qcom", board = "crow",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 720, screenHeight = 1612, dpi = 269,
            glRenderer = "Adreno (TM) 619", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_GLOBAL,
            locales = LOCALES_MIXED
        ),
        // ---- Vivo Y100A ----
        DeviceSpec(
            manufacturer = "vivo", brand = "vivo", model = "V2246",
            product = "V2246", hardware = "mt6895", board = "mt6895",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2408, dpi = 400,
            glRenderer = "Mali-G610 MC6", glVendor = "ARM",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_ASIA,
            locales = listOf("zh_CN", "en_US")
        ),
        // ---- Infinix Note 30 Pro ----
        DeviceSpec(
            manufacturer = "Infinix", brand = "Infinix", model = "X678B",
            product = "X678B-GL", hardware = "mt6895", board = "mt6895",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2400, dpi = 396,
            glRenderer = "Mali-G610 MC6", glVendor = "ARM",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_ASIA + CARRIERS_EU,
            locales = LOCALES_MIXED
        ),
        // ---- Tecno Camon 20 Pro ----
        DeviceSpec(
            manufacturer = "TECNO", brand = "TECNO", model = "CK7n",
            product = "CK7n-GL", hardware = "mt6789", board = "mt6789",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2400, dpi = 395,
            glRenderer = "Mali-G57 MC2", glVendor = "ARM",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_ASIA,
            locales = LOCALES_MIXED
        ),
        // ---- Samsung Galaxy A14 5G ----
        DeviceSpec(
            manufacturer = "Samsung", brand = "samsung", model = "SM-A146B",
            product = "a14xeea", hardware = "mt6833", board = "mt6833",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2408, dpi = 408,
            glRenderer = "Mali-G57 MC2", glVendor = "ARM",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- Xiaomi Redmi A2+ ----
        DeviceSpec(
            manufacturer = "Xiaomi", brand = "Redmi", model = "23028RNCAG",
            product = "sky", hardware = "mt6765", board = "mt6765",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 720, screenHeight = 1600, dpi = 268,
            glRenderer = "PowerVR Rogue GE8320", glVendor = "Imagination Technologies",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_ASIA,
            locales = LOCALES_MIXED
        ),
        // ---- Samsung Galaxy S21 FE 5G ----
        DeviceSpec(
            manufacturer = "Samsung", brand = "samsung", model = "SM-G990B2",
            product = "r9q", hardware = "qcom", board = "lahaina",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2340, dpi = 401,
            glRenderer = "Adreno (TM) 660", glVendor = "Qualcomm",
            sensors = SAMSUNG_SENSORS, carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- Google Pixel 5a ----
        DeviceSpec(
            manufacturer = "Google", brand = "google", model = "Pixel 5a",
            product = "barbet", hardware = "barbet", board = "barbet",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2400, dpi = 409,
            glRenderer = "Adreno (TM) 620", glVendor = "Qualcomm",
            sensors = PIXEL_SENSORS, carriers = CARRIERS_US,
            locales = LOCALES_EN
        ),
        // ---- Xiaomi 12 Pro ----
        DeviceSpec(
            manufacturer = "Xiaomi", brand = "Xiaomi", model = "2201122C",
            product = "zeus", hardware = "qcom", board = "taro",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1440, screenHeight = 3200, dpi = 522,
            glRenderer = "Adreno (TM) 730", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_ASIA + CARRIERS_EU,
            locales = LOCALES_MIXED
        ),
        // ---- Motorola Moto G73 5G ----
        DeviceSpec(
            manufacturer = "motorola", brand = "motorola", model = "XT2237-2",
            product = "bronco_g", hardware = "mt6877", board = "mt6877",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2400, dpi = 396,
            glRenderer = "Mali-G57 MC2", glVendor = "ARM",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_US + CARRIERS_EU,
            locales = LOCALES_EN
        ),
        // ---- Honor Magic5 Pro ----
        DeviceSpec(
            manufacturer = "HONOR", brand = "HONOR", model = "PGT-N19",
            product = "PGT-N19", hardware = "qcom", board = "taro",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1312, screenHeight = 2848, dpi = 460,
            glRenderer = "Adreno (TM) 740", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_GLOBAL,
            locales = LOCALES_MIXED
        ),
        // ---- iQOO 12 ----
        DeviceSpec(
            manufacturer = "vivo", brand = "iQOO", model = "V2309A",
            product = "V2309A_EX_A", hardware = "qcom", board = "pineapple",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1440, screenHeight = 3200, dpi = 520,
            glRenderer = "Adreno (TM) 750", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_ASIA,
            locales = listOf("zh_CN", "en_US")
        ),
        // ---- Samsung Galaxy A73 5G ----
        DeviceSpec(
            manufacturer = "Samsung", brand = "samsung", model = "SM-A736B",
            product = "a73xeea", hardware = "qcom", board = "bengal",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2400, dpi = 393,
            glRenderer = "Adreno (TM) 619", glVendor = "Qualcomm",
            sensors = SAMSUNG_SENSORS, carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- Xiaomi POCO F5 Pro ----
        DeviceSpec(
            manufacturer = "Xiaomi", brand = "POCO", model = "23013PC75G",
            product = "marble_global", hardware = "qcom", board = "taro",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1440, screenHeight = 3200, dpi = 522,
            glRenderer = "Adreno (TM) 740", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_ASIA + CARRIERS_EU,
            locales = LOCALES_MIXED
        ),
        // ---- OnePlus 10 Pro ----
        DeviceSpec(
            manufacturer = "OnePlus", brand = "OnePlus", model = "NE2213",
            product = "op515bl1", hardware = "qcom", board = "taro",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1440, screenHeight = 3216, dpi = 525,
            glRenderer = "Adreno (TM) 730", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_GLOBAL,
            locales = LOCALES_MIXED
        ),
        // ---- Realme GT Neo 5 ----
        DeviceSpec(
            manufacturer = "realme", brand = "realme", model = "RMX3706",
            product = "RMX3706", hardware = "qcom", board = "crow",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2412, dpi = 394,
            glRenderer = "Adreno (TM) 695", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_ASIA + CARRIERS_EU,
            locales = LOCALES_MIXED
        ),
        // ---- Samsung Galaxy S20 FE 5G ----
        DeviceSpec(
            manufacturer = "Samsung", brand = "samsung", model = "SM-G781B",
            product = "r8q", hardware = "qcom", board = "bengal",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2400, dpi = 407,
            glRenderer = "Adreno (TM) 619", glVendor = "Qualcomm",
            sensors = SAMSUNG_SENSORS, carriers = CARRIERS_GLOBAL,
            locales = LOCALES_EN
        ),
        // ---- Xiaomi Redmi Note 13 Pro+ ----
        DeviceSpec(
            manufacturer = "Xiaomi", brand = "Redmi", model = "23090RA98G",
            product = "zircon", hardware = "mt6989", board = "mt6989",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1220, screenHeight = 2712, dpi = 453,
            glRenderer = "Immortalis-G715", glVendor = "ARM",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_ASIA + CARRIERS_EU,
            locales = LOCALES_MIXED
        ),
        // ---- ASUS Zenfone 10 ----
        DeviceSpec(
            manufacturer = "asus", brand = "asus", model = "AI2302",
            product = "AI2302_A", hardware = "qcom", board = "kalama",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2400, dpi = 446,
            glRenderer = "Adreno (TM) 740", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_GLOBAL,
            locales = LOCALES_MIXED
        ),
        // ---- Fairphone 5 ----
        DeviceSpec(
            manufacturer = "Fairphone", brand = "Fairphone", model = "FP5",
            product = "FP5", hardware = "qcom", board = "crow",
            androidVersion = "13", sdkInt = 33,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1224, screenHeight = 2770, dpi = 450,
            glRenderer = "Adreno (TM) 619", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS,
            carriers = CARRIERS_EU,
            locales = LOCALES_EN
        ),
        // ---- Sony Xperia 5 V ----
        DeviceSpec(
            manufacturer = "Sony", brand = "Sony", model = "XQ-DE72",
            product = "pdx236", hardware = "qcom", board = "kalama",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2520, dpi = 449,
            glRenderer = "Adreno (TM) 740", glVendor = "Qualcomm",
            sensors = COMMON_SENSORS + listOf("Barometer"),
            carriers = CARRIERS_GLOBAL,
            locales = LOCALES_MIXED
        ),
        // ---- Google Pixel 8a ----
        DeviceSpec(
            manufacturer = "Google", brand = "google", model = "Pixel 8a",
            product = "akita", hardware = "akita", board = "akita",
            androidVersion = "14", sdkInt = 34,
            cpuAbi = "arm64-v8a", cpuAbi2 = "armeabi-v7a",
            screenWidth = 1080, screenHeight = 2400, dpi = 429,
            glRenderer = "Mali-G715", glVendor = "ARM",
            sensors = PIXEL_SENSORS, carriers = CARRIERS_US,
            locales = LOCALES_EN
        )
    )
}
