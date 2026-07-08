package com.rxsoft.mobile.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.PointOfSale
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.rxsoft.mobile.ui.designsystem.components.AppOutlinedButton
import com.rxsoft.mobile.ui.designsystem.components.AppTextButton
import com.rxsoft.mobile.ui.designsystem.components.AppTopAppBar
import com.rxsoft.mobile.ui.designsystem.token.ShapeTokens
import com.rxsoft.mobile.ui.designsystem.token.SpacingTokens

@Composable
fun SettingsScreen(
    onSignOut: () -> Unit = {},
    externalVm: SettingsViewModel? = null
) {
    val resolvedVm = externalVm ?: hiltViewModel<SettingsViewModel>()
    val serverUrl by resolvedVm.serverUrl.collectAsState()
    val activeModules by resolvedVm.activeModules.collectAsState()
    val posConfig by resolvedVm.posConfigManager.config.collectAsState()
    var editingUrl by remember { mutableStateOf(false) }
    var urlInput by remember(serverUrl) { mutableStateOf(serverUrl) }
    var showSignOutDialog by remember { mutableStateOf(false) }

    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            shape = ShapeTokens.dialog,
            title = { Text("Sign Out") },
            text = { Text("Are you sure you want to sign out?") },
            confirmButton = {
                AppTextButton(
                    text = "Sign Out",
                    onClick = {
                        showSignOutDialog = false
                        onSignOut()
                    },
                )
            },
            dismissButton = {
                AppTextButton(
                    text = "Cancel",
                    onClick = { showSignOutDialog = false },
                )
            },
        )
    }

    Scaffold(
        topBar = {
            AppTopAppBar(title = "Settings")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = SpacingTokens.xl)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(SpacingTokens.lg))

            Text("Modules", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(SpacingTokens.md))

            ModuleToggleCard(
                title = AppModule.POS.title,
                description = AppModule.POS.description,
                icon = Icons.Outlined.PointOfSale,
                isActive = activeModules.contains(AppModule.POS),
                onToggle = { resolvedVm.toggleModule(AppModule.POS) }
            )
            Spacer(Modifier.height(SpacingTokens.sm))
            ModuleToggleCard(
                title = AppModule.SHOP.title,
                description = AppModule.SHOP.description,
                icon = Icons.Outlined.Medication,
                isActive = activeModules.contains(AppModule.SHOP),
                onToggle = { resolvedVm.toggleModule(AppModule.SHOP) }
            )
            Spacer(Modifier.height(SpacingTokens.sm))
            ModuleToggleCard(
                title = AppModule.INVENTORY.title,
                description = AppModule.INVENTORY.description,
                icon = Icons.Outlined.Inventory2,
                isActive = activeModules.contains(AppModule.INVENTORY),
                onToggle = { resolvedVm.toggleModule(AppModule.INVENTORY) }
            )
            Spacer(Modifier.height(SpacingTokens.sm))
            ModuleToggleCard(
                title = AppModule.SALES.title,
                description = AppModule.SALES.description,
                icon = Icons.Outlined.BarChart,
                isActive = activeModules.contains(AppModule.SALES),
                onToggle = { resolvedVm.toggleModule(AppModule.SALES) }
            )

            Spacer(Modifier.height(SpacingTokens.xxxl))
            Text("POS Configuration", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(SpacingTokens.md))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = ShapeTokens.xl,
            ) {
                Column(modifier = Modifier.padding(SpacingTokens.xl)) {
                    Text("Stock Location", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(SpacingTokens.xxs))
                    Text(
                        text = posConfig?.stockLocation?.name ?: "Not configured",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (posConfig?.stockLocation != null) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.error,
                    )
                    if (posConfig?.storeId != null) {
                        Spacer(Modifier.height(SpacingTokens.sm))
                        Text("Store ID", style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(SpacingTokens.xxs))
                        Text(
                            text = posConfig!!.storeId!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Spacer(Modifier.height(SpacingTokens.xxxl))
            Text("Server", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(SpacingTokens.md))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = ShapeTokens.xl,
            ) {
                Column(modifier = Modifier.padding(SpacingTokens.xl)) {
                    Text("API Base URL", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(SpacingTokens.sm))
                    if (editingUrl) {
                        OutlinedTextField(
                            value = urlInput,
                            onValueChange = { urlInput = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = ShapeTokens.md,
                        )
                        Spacer(Modifier.height(SpacingTokens.sm))
                        Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.sm)) {
                            AppOutlinedButton(
                                text = "Cancel",
                                onClick = { editingUrl = false; urlInput = serverUrl },
                            )
                            Button(onClick = {
                                resolvedVm.saveServerUrl(urlInput)
                                editingUrl = false
                            }) { Text("Save") }
                        }
                    } else {
                        Text(
                            text = serverUrl,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.height(SpacingTokens.sm))
                        AppTextButton(text = "Edit", onClick = { editingUrl = true })
                    }
                }
            }

            Spacer(Modifier.height(SpacingTokens.xxxl))
            Button(
                onClick = { showSignOutDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Sign Out")
            }

            Spacer(Modifier.height(SpacingTokens.xxxl))
        }
    }
}

@Composable
private fun ModuleToggleCard(
    title: String,
    description: String,
    icon: ImageVector,
    isActive: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .semantics { contentDescription = "$title, toggle" },
        shape = ShapeTokens.xl,
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.xl),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.width(SpacingTokens.lg))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Switch(checked = isActive, onCheckedChange = { onToggle() })
        }
    }
}
