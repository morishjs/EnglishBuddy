package com.morishjs.englishbuddy.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String?, val title: String? = null, val icon: ImageVector? = null) {
    object Chat : Screen("chat", "Chat")
    object ChatRooms: Screen("chat_rooms", "ChatRooms", icon = Icons.Outlined.List)
    object Settings: Screen("settings", "Settings", icon = Icons.Outlined.Settings)
}
