package com.example.rentit.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rentit.presentation.main.MainView
import com.example.rentit.presentation.productdetail.ProductDetailScreen
import com.example.rentit.presentation.productdetail.reservation.request.ResvRequestScreen
import com.example.rentit.presentation.productdetail.reservation.request.complete.ResvRequestCompleteScreen
import com.example.rentit.presentation.productdetail.reservation.requesthistory.RequestHistoryScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProductDetailNavHost(productId: Int?) {
    val navHostController: NavHostController = rememberNavController()

    NavHost(navController =  navHostController, startDestination = NavigationRoutes.PRODUCT_DETAIL){
        composable(NavigationRoutes.PRODUCT_DETAIL) { ProductDetailScreen(navHostController, productId) }
        composable(NavigationRoutes.RESV_REQUEST) { ResvRequestScreen(navHostController, productId) }
        composable(
            route = NavigationRoutes.RESV_REQUEST_COMPLETE + "/{rentalStartDate}/{rentalEndDate}/{rentalPeriod}/{formattedTotalPrice}",
            arguments = listOf(
                navArgument("rentalStartDate") { type = NavType.StringType },
                navArgument("rentalEndDate") { type = NavType.StringType },
                navArgument("rentalPeriod") { type = NavType.IntType },
                navArgument("formattedTotalPrice") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val rentalStartDate = backStackEntry.arguments?.getString("rentalStartDate") ?: ""
            val rentalEndDate = backStackEntry.arguments?.getString("rentalEndDate") ?: ""
            val rentalPeriod = backStackEntry.arguments?.getInt("rentalPeriod") ?: 0
            val formattedTotalPrice = backStackEntry.arguments?.getString("formattedTotalPrice") ?: "0"
            ResvRequestCompleteScreen(navHostController, rentalStartDate, rentalEndDate, rentalPeriod, formattedTotalPrice) }
        composable(NavigationRoutes.RESV_REQUEST_HISTORY) { RequestHistoryScreen(navHostController, productId) }
        composable(
            route = NavigationRoutes.CHAT_NAV_HOST + "/{productId}/{reservationId}/{chatRoomId}",
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType },
                navArgument("reservationId") { type = NavType.IntType },
                navArgument("chatRoomId") { type = NavType.StringType })
        ) { backStackEntry ->
            val pId = backStackEntry.arguments?.getInt("productId")
            val rId = backStackEntry.arguments?.getInt("reservationId")
            val cId = backStackEntry.arguments?.getString("chatRoomId")
            ChatroomNavHost(pId, rId, cId)
        }
        composable(NavigationRoutes.MAIN) { MainView() }
    }
}
