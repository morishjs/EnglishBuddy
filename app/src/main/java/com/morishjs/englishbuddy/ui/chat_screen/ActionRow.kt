package com.morishjs.englishbuddy.ui.chat_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.morishjs.englishbuddy.ui.navigation.Screen
import com.morishjs.englishbuddy.ui.theme.Black

@Composable
fun ActionRow(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
    )
    {
        listOf(Screen.ChatRooms, Screen.Settings).forEach { item ->
            ActionButton(icon = item.icon!!)
        }
    }
}

@Composable
fun ActionButton(icon: ImageVector) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(240, 240, 240)
        ),
        onClick = { /* Handle button click here */ }) {
        Icon(icon, tint = Black, contentDescription = "", modifier = Modifier.size(24.dp))
    }
}