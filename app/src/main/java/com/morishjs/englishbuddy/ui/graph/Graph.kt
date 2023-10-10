package com.morishjs.englishbuddy.ui.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.morishjs.englishbuddy.ui.main.RecorderUI

@Composable
fun Graph(
    navHostController: NavHostController
) {
    NavHost(navController = navHostController, startDestination = Route.Home) {
        composable(route = Route.Home) {
            RecorderUI()
        }
    }
}
