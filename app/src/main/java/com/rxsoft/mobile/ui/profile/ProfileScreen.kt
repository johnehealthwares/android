package com.rxsoft.mobile.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rxsoft.mobile.R
import com.rxsoft.mobile.ui.designsystem.components.AppTopAppBar
import com.rxsoft.mobile.ui.designsystem.token.ColorTokens
import com.rxsoft.mobile.ui.designsystem.token.ShapeTokens
import com.rxsoft.mobile.ui.designsystem.token.SpacingTokens

@Composable
fun ProfileScreen(
    name: String = "John Doe",
    phone: String = "+234 801 234 5678",
    rewardPoints: Int = 245,
    onPersonalDetails: () -> Unit = {},
    onIdentification: () -> Unit = {},
    onFamilyMembers: () -> Unit = {},
    onAccountCredit: () -> Unit = {}
) {
    Scaffold(
        containerColor = ColorTokens.shopBackground,
        topBar = {
            AppTopAppBar(title = "Profile")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = SpacingTokens.xl)
        ) {
            Spacer(Modifier.height(SpacingTokens.xl))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = ShapeTokens.xxl,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpacingTokens.xxl),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(R.drawable.profile_avatar),
                        contentDescription = "Profile avatar",
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(Modifier.height(SpacingTokens.md))
                    Text(name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(phone, color = Color.Gray)
                }
            }
            Spacer(Modifier.height(SpacingTokens.xl))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.shopAccent),
                shape = ShapeTokens.xxl,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpacingTokens.xl),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = .2f),
                        modifier = Modifier.size(52.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.Star, contentDescription = "Reward points", tint = Color.White)
                        }
                    }
                    Spacer(Modifier.width(SpacingTokens.lg))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Reward Points", color = Color.White)
                        Text(
                            rewardPoints.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
            Spacer(Modifier.height(SpacingTokens.xxxl))
            ProfileMenuItem(icon = Icons.Outlined.Person, title = "Personal Details", onClick = onPersonalDetails)
            ProfileMenuItem(icon = Icons.Outlined.Badge, title = "Saved Identification", onClick = onIdentification)
            ProfileMenuItem(icon = Icons.Outlined.Group, title = "Family Members", onClick = onFamilyMembers)
            ProfileMenuItem(icon = Icons.Outlined.AccountBalanceWallet, title = "Account Credit", onClick = onAccountCredit)
        }
    }
}

@Composable
private fun ProfileMenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = SpacingTokens.sm)
            .clickable(onClick = onClick)
            .semantics { contentDescription = title },
        shape = ShapeTokens.xl,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.xl),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = ColorTokens.shopAccent.copy(alpha = .15f),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = title, tint = ColorTokens.shopAccent)
                }
            }
            Spacer(Modifier.width(SpacingTokens.lg))
            Text(title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
            Icon(Icons.Outlined.KeyboardArrowRight, contentDescription = "$title, navigate", tint = Color.Gray)
        }
    }
}
