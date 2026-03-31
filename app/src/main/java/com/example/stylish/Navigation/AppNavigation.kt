package com.example.stylish.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.stylish.Presentation.Auth.LoginScreen
import com.example.stylish.presentation.UserPreferences.UserPreferencesViewModel
import com.example.stylish.presentation.onBoarding.Onboard1
import com.example.stylish.presentation.onBoarding.Onboard2
import com.example.stylish.presentation.onBoarding.Onboard3
import com.example.stylish.presentation.Auth.SignupScreen
import com.example.stylish.presentation.Auth.ForgetPasswordScreen
import com.example.stylish.presentation.products.ProductListScreen
import com.example.stylish.presentation.products.ProductDetailScreen
import com.example.stylish.presentation.cart.CartScreen
import com.example.stylish.presentation.Wishlist.WishlistScreen
import com.example.stylish.presentation.Search.SearchScreen
import com.example.stylish.presentation.Settings.SettingsScreen
import com.example.stylish.Presentation.Splash.SplashScreen


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    // Inject ViewModel using Hilt
    val userPreferencesViewModel: UserPreferencesViewModel = hiltViewModel()

    // Observe user preferences state
    val userPreferencesState by userPreferencesViewModel.state.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.SplashScreen
    ) {

        composable<Routes.LoginScreen> {
            LoginScreen(navHostController = navController)
        }

        composable<Routes.SignUpScreen> {
            SignupScreen(navHostController = navController)
        }

        composable<Routes.ForgetPasswordScreen> {
            ForgetPasswordScreen(navController)
        }

        composable<Routes.OnboardingScreen1> {
            Onboard1(navController)
        }

        composable<Routes.OnboardingScreen2> {
            Onboard2(navController)
        }

        composable<Routes.OnboardingScreen3> {
            Onboard3(navController)
        }

        composable<Routes.ProductListScreen> {
            ProductListScreen(navController = navController)
        }

        composable<Routes.ProductDetailScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<Routes.ProductDetailScreen>() // it convert to object
            ProductDetailScreen(
                navController = navController,
                productId = args.productId
            )
        }

        composable<Routes.SplashScreen> {

            SplashScreen(
                onFinish = {

                    val destination = when {
                        userPreferencesState.isLoggedIn -> Routes.ProductListScreen
                        userPreferencesState.isFirstTimeLogin -> Routes.OnboardingScreen1
                        else -> Routes.LoginScreen
                    }

                    navController.navigate(destination) {
                        popUpTo(Routes.SplashScreen) { inclusive = true }
                    }
                }
            )
        }

        composable<Routes.WishlistScreen> {
            WishlistScreen(navController = navController)
        }

        composable<Routes.CartScreen> {
            CartScreen(navController = navController)
        }

        composable<Routes.SearchScreen> {
            SearchScreen(navController = navController)
        }

        composable<Routes.SettingsScreen> {
            SettingsScreen(navController = navController)
        }
    }
}