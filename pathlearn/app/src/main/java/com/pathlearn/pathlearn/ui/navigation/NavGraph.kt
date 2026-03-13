package com.pathlearn.pathlearn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pathlearn.pathlearn.ui.screens.auth.LoginScreen
import com.pathlearn.pathlearn.ui.screens.auth.RegisterScreen
import com.pathlearn.pathlearn.ui.screens.courses.CourseDetailScreen
import com.pathlearn.pathlearn.ui.screens.courses.CourseListScreen
import com.pathlearn.pathlearn.ui.screens.enrollments.MyCoursesScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object CourseList : Screen("course_list")
    object CourseDetail : Screen("course_detail/{courseId}") {
        fun createRoute(courseId: Int) = "course_detail/$courseId"
    }
    object MyCourses : Screen("my_courses")
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.CourseList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.CourseList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.CourseList.route) {
            CourseListScreen(
                onCourseClick = { courseId ->
                    navController.navigate(Screen.CourseDetail.createRoute(courseId))
                },
                onMyCoursesClick = {
                    navController.navigate(Screen.MyCourses.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.CourseDetail.route,
            arguments = listOf(
                navArgument("courseId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0

            CourseDetailScreen(
                courseId = courseId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.MyCourses.route) {
            MyCoursesScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onCourseClick = { courseId ->
                    navController.navigate(Screen.CourseDetail.createRoute(courseId))
                }
            )
        }
    }
}