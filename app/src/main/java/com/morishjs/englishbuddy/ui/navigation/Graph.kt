package com.morishjs.englishbuddy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.morishjs.englishbuddy.ui.main.RecorderUI

@Composable
fun Graph(
    navHostController: NavHostController
) {
    NavHost(navController = navHostController, startDestination = Screen.Chat.route!!) {
        composable(route = Screen.Chat.route) {
            RecorderUI(navHostController)
        }
    }
}
