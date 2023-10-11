package com.morishjs.englishbuddy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.morishjs.englishbuddy.ui.chat_rooms.ChatRoomsUI
import com.morishjs.englishbuddy.ui.chat_screen.RecorderUI

@Composable
fun Graph(
    navHostController: NavHostController
) {
    NavHost(navController = navHostController, startDestination = Screen.ChatRooms.route!!) {
        composable(
            route = "${Screen.Chat.route!!}/{id}", arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) {
            RecorderUI(navController = navHostController, id = it.arguments?.getLong("id")!!)
        }
        composable(route = Screen.ChatRooms.route) {
            ChatRoomsUI(navHostController)
        }
    }
}
