package com.phantomclone.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.phantomclone.data.model.DeviceFingerprint
import com.phantomclone.ui.theme.CyberCyan
import com.phantomclone.viewmodel.ProfileDetailViewModel

/**
 * Fingerprint Viewer screen — shows all fingerprint properties
 * grouped by category with copy-to-clipboard support.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FingerprintViewerScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val fp = uiState.profile.fingerprint
    val clipboard = LocalClipboardManager.current

    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fingerprint Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Device section
            item {
                FingerprintGroup(
                    title = "Device",
                    icon = Icons.Default.PhoneAndroid,
                    entries = buildList {
                        add("Manufacturer" to fp.manufacturer)
                        add("Brand" to fp.brand)
                        add("Model" to fp.model)
                        add("Product" to fp.product)
                        add("Hardware" to fp.hardware)
                        add("Board" to fp.board)
                        add("Serial" to fp.serial)
                    },
                    onCopy = { label, value ->
                        clipboard.setText(AnnotatedString(value))
                        snackbarMessage = "Copied: $label"
                    }
                )
            }

            // Build info section
            item {
                FingerprintGroup(
                    title = "Build Info",
                    icon = Icons.Default.Build,
                    entries = buildList {
                        add("Android Version" to fp.androidVersion)
                        add("SDK Level" to fp.sdkInt.toString())
                        add("Build Fingerprint" to fp.buildFingerprint)
                        add("Build Display" to fp.buildDisplay)
                        add("CPU ABI" to fp.cpuAbi)
                        add("CPU ABI2" to fp.cpuAbi2)
                    },
                    onCopy = { label, value ->
                        clipboard.setText(AnnotatedString(value))
                        snackbarMessage = "Copied: $label"
                    }
                )
            }

            // Screen section
            item {
                FingerprintGroup(
                    title = "Screen",
                    icon = Icons.Default.ScreenshotMonitor,
                    entries = buildList {
                        add("Resolution" to "${fp.screenWidth}×${fp.screenHeight}")
                        add("DPI" to fp.dpi.toString())
                    },
                    onCopy = { label, value ->
                        clipboard.setText(AnnotatedString(value))
                        snackbarMessage = "Copied: $label"
                    }
                )
            }

            // Network section
            item {
                FingerprintGroup(
                    title = "Network & Identity",
                    icon = Icons.Default.NetworkCheck,
                    entries = buildList {
                        add("Android ID" to fp.androidId)
                        add("Google Ad ID" to fp.googleAdId)
                        add("WiFi MAC" to fp.wifiMac)
                        add("IMEI" to fp.imei)
                        add("Phone Number" to fp.phoneNumber)
                        add("Carrier" to fp.carrier)
                    },
                    onCopy = { label, value ->
                        clipboard.setText(AnnotatedString(value))
                        snackbarMessage = "Copied: $label"
                    }
                )
            }

            // Locale section
            item {
                FingerprintGroup(
                    title = "Locale & Time",
                    icon = Icons.Default.Language,
                    entries = buildList {
                        add("Timezone" to fp.timezone)
                        add("Locale" to fp.locale)
                        add("Language" to fp.language)
                    },
                    onCopy = { label, value ->
                        clipboard.setText(AnnotatedString(value))
                        snackbarMessage = "Copied: $label"
                    }
                )
            }

            // Browser / WebView section
            item {
                FingerprintGroup(
                    title = "Browser / WebView",
                    icon = Icons.Default.Public,
                    entries = buildList {
                        add("User Agent" to fp.userAgent)
                        add("GL Renderer" to fp.glRenderer)
                        add("GL Vendor" to fp.glVendor)
                    },
                    onCopy = { label, value ->
                        clipboard.setText(AnnotatedString(value))
                        snackbarMessage = "Copied: $label"
                    }
                )
            }

            // Sensors section
            item {
                FingerprintGroup(
                    title = "Sensors",
                    icon = Icons.Default.Sensors,
                    entries = fp.sensors.mapIndexed { i, s -> "Sensor ${i + 1}" to s },
                    onCopy = { label, value ->
                        clipboard.setText(AnnotatedString(value))
                        snackbarMessage = "Copied: $label"
                    }
                )
            }
        }
    }
}

@Composable
private fun FingerprintGroup(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    entries: List<Pair<String, String>>,
    onCopy: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(title, style = MaterialTheme.typography.titleMedium)
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }

            if (expanded) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                entries.forEach { (label, value) ->
                    if (value.isNotEmpty()) {
                        FingerprintEntryRow(
                            label = label,
                            value = value,
                            onCopy = { onCopy(label, value) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FingerprintEntryRow(
    label: String,
    value: String,
    onCopy: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                maxLines = 3
            )
        }
        IconButton(
            onClick = onCopy,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                Icons.Default.ContentCopy,
                contentDescription = "Copy",
                modifier = Modifier.size(16.dp),
                tint = CyberCyan
            )
        }
    }
}
