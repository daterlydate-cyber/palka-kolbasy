package com.phantomclone.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.phantomclone.data.model.ProxyConfig
import com.phantomclone.data.model.ProxyType
import com.phantomclone.ui.theme.CyberCyan
import com.phantomclone.ui.theme.CyberPurple
import com.phantomclone.viewmodel.ProfileDetailViewModel

/**
 * Profile Detail / Edit screen.
 *
 * Shows and allows editing of:
 * - Profile name
 * - Device fingerprint summary (with regenerate option)
 * - Proxy settings
 * - Notes
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToFingerprint: (Long) -> Unit,
    viewModel: ProfileDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val profile = uiState.profile
    val scrollState = rememberScrollState()

    // Navigate back after save
    LaunchedEffect(uiState.saved) {
        if (uiState.saved) onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (uiState.isNew) "New Profile" else "Edit Profile")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.save() }) {
                        Text("Save", color = CyberCyan)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ---- Profile Name ----
            OutlinedTextField(
                value = profile.name,
                onValueChange = {
                    viewModel.onNameChange(it)
                    viewModel.clearNameError()
                },
                label = { Text("Profile Name") },
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                isError = uiState.nameError,
                supportingText = if (uiState.nameError) {
                    { Text("Name cannot be empty") }
                } else null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // ---- Fingerprint Section ----
            SectionCard(title = "Device Fingerprint", icon = Icons.Default.Fingerprint) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    FingerprintRow("Device", "${profile.fingerprint.manufacturer} ${profile.fingerprint.model}")
                    FingerprintRow("Android", "${profile.fingerprint.androidVersion} (SDK ${profile.fingerprint.sdkInt})")
                    FingerprintRow("Screen", "${profile.fingerprint.screenWidth}×${profile.fingerprint.screenHeight} ${profile.fingerprint.dpi}dpi")
                    FingerprintRow("Android ID", profile.fingerprint.androidId)
                    FingerprintRow("IMEI", profile.fingerprint.imei)
                    FingerprintRow("Carrier", profile.fingerprint.carrier)

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.regenerateFingerprint() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Regenerate")
                        }
                        if (!uiState.isNew) {
                            OutlinedButton(
                                onClick = { onNavigateToFingerprint(profile.id) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("View All")
                            }
                        }
                    }
                }
            }

            // ---- Proxy Settings ----
            SectionCard(title = "Proxy Settings", icon = Icons.Default.NetworkCheck) {
                ProxySettingsSection(
                    proxyConfig = profile.proxyConfig,
                    onConfigChange = viewModel::onProxyConfigChange
                )
            }

            // ---- Notes ----
            SectionCard(title = "Notes", icon = Icons.Default.Notes) {
                OutlinedTextField(
                    value = profile.notes,
                    onValueChange = viewModel::onNotesChange,
                    placeholder = { Text("Add notes about this profile…") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
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
                Icon(icon, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun FingerprintRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(0.6f),
            maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProxySettingsSection(
    proxyConfig: ProxyConfig,
    onConfigChange: (ProxyConfig) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Proxy type selector
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = proxyConfig.type.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Proxy Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                ProxyType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) },
                        onClick = {
                            onConfigChange(proxyConfig.copy(type = type))
                            expanded = false
                        }
                    )
                }
            }
        }

        if (proxyConfig.type != ProxyType.NONE) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = proxyConfig.host,
                    onValueChange = { onConfigChange(proxyConfig.copy(host = it)) },
                    label = { Text("Host") },
                    modifier = Modifier.weight(0.7f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = if (proxyConfig.port == 0) "" else proxyConfig.port.toString(),
                    onValueChange = { onConfigChange(proxyConfig.copy(port = it.toIntOrNull() ?: 0)) },
                    label = { Text("Port") },
                    modifier = Modifier.weight(0.3f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            OutlinedTextField(
                value = proxyConfig.username,
                onValueChange = { onConfigChange(proxyConfig.copy(username = it)) },
                label = { Text("Username (optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            var passwordVisible by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = proxyConfig.password,
                onValueChange = { onConfigChange(proxyConfig.copy(password = it)) },
                label = { Text("Password (optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}
