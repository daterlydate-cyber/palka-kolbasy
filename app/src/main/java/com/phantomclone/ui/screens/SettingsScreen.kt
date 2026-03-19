package com.phantomclone.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.phantomclone.BuildConfig
import com.phantomclone.ui.theme.CyberCyan
import com.phantomclone.viewmodel.SettingsViewModel

/**
 * Settings screen — theme, export/import, about info.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val exportResult by viewModel.exportResult.collectAsStateWithLifecycle()
    val importResult by viewModel.importResult.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(exportResult) {
        exportResult?.let {
            snackbarHostState.showSnackbar("Exported to: $it")
            viewModel.clearExportResult()
        }
    }
    LaunchedEffect(importResult) {
        importResult?.let {
            if (it.success) {
                snackbarHostState.showSnackbar("Imported ${it.count} profiles")
            } else {
                snackbarHostState.showSnackbar("Import failed: ${it.error}")
            }
            viewModel.clearImportResult()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Data section
            SettingsSectionHeader("Data")

            SettingsItem(
                icon = Icons.Default.Upload,
                title = "Export All Profiles",
                subtitle = "Save all profiles as JSON",
                onClick = { viewModel.exportAllProfiles() }
            )

            // About section
            Spacer(Modifier.height(8.dp))
            SettingsSectionHeader("About")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Shield,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                "PhantomClone",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                "v${BuildConfig.VERSION_NAME} (Phase 1 MVP)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "AntiDetect App Cloner — Create isolated profiles with unique device fingerprints.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Architecture: MVVM + Clean Architecture\nDatabase: Room\nDI: Hilt\nUI: Jetpack Compose + Material 3",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            // Roadmap
            Spacer(Modifier.height(8.dp))
            SettingsSectionHeader("Roadmap")

            RoadmapCard(
                phase = "Phase 2",
                title = "Virtual Engine",
                items = listOf(
                    "Full APK classloader injection",
                    "Build.* property hooks",
                    "Component proxy system",
                    "File system virtualization"
                )
            )
            RoadmapCard(
                phase = "Phase 3",
                title = "Network & TLS",
                items = listOf(
                    "JA3/JA4 TLS fingerprint spoofing",
                    "WebRTC leak prevention",
                    "DNS-over-HTTPS routing",
                    "Per-profile network isolation"
                )
            )
            RoadmapCard(
                phase = "Phase 4",
                title = "Advanced Anti-Detect",
                items = listOf(
                    "Canvas fingerprint noise",
                    "Sensor data spoofing",
                    "Play Integrity bypass research",
                    "Magisk module integration"
                )
            )
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = CyberCyan,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = CyberCyan)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge)
                if (subtitle != null) {
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun RoadmapCard(
    phase: String,
    title: String,
    items: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    phase,
                    style = MaterialTheme.typography.labelSmall,
                    color = CyberCyan
                )
                Spacer(Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleSmall)
            }
            Spacer(Modifier.height(8.dp))
            items.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        Icons.Default.RadioButtonUnchecked,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        item,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
